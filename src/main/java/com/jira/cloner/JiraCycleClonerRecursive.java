package com.jira.cloner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JiraCycleClonerRecursive {

    // ============ CONFIGURATION ============
    private static final String JIRA_BASE_URL = "https://your-jira-instance";
    private static final String USERNAME = "your-username";
    private static final String PASSWORD = "your-password";
    private static final String PROJECT_ID = "10001";
    private static final String RELEASED_FOLDER_ID = "2001";
    private static final String UNRELEASED_FOLDER_ID = "2002";
    private static final String VERSION_ID = "3001";
    private static final String SUFFIX = " Regression R5";
    // =======================================

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Step 1: Recursively clone the whole Reg Suite folder structure
            cloneFolderRecursively(client, RELEASED_FOLDER_ID, UNRELEASED_FOLDER_ID);
        }
    }

    // Clone all cycles/folders and subfolders recursively
    private static void cloneFolderRecursively(CloseableHttpClient client, String srcFolderId, String targetParentFolderId) throws Exception {
        String cyclesUrl = JIRA_BASE_URL + "/rest/zapi/latest/cycle?projectId=" + PROJECT_ID + "&folderId=" + srcFolderId;
        HttpGet get = new HttpGet(cyclesUrl);

        get.addHeader("Authorization", "Basic " + getAuthHeader());
        get.addHeader("Accept", "application/json");

        try (CloseableHttpResponse response = client.execute(get)) {
            int statusCode = response.getCode();
            if (statusCode != 200) {
                throw new RuntimeException("Failed to fetch cycles, HTTP error code : " + statusCode);
            }
            String result = readResponse(response);
            JsonNode json = mapper.readTree(result);

            if (!json.has("searchObjectList")) {
                System.out.println("⚠️ No cycles found under folder: " + srcFolderId);
                return;
            }

            for (JsonNode cycle : json.get("searchObjectList")) {
                String cycleId = cycle.get("cycleId").asText();
                String cycleName = cycle.get("name").asText();
                System.out.println("🔄 Cloning cycle: " + cycleName);

                // Step 2: Clone the current cycle/folder
                String newFolderId = cloneCycleGetId(client, cycleId, cycleName + SUFFIX, targetParentFolderId);

                // Step 3: If the cycle/folder has children, clone them recursively into the new folder
                if (cycle.has("childFolders")) {
                    for (JsonNode childFolder : cycle.get("childFolders")) {
                        String childFolderId = childFolder.get("cycleId").asText();
                        cloneFolderRecursively(client, childFolderId, newFolderId);
                    }
                }
            }
        }
    }

    // Clone a cycle/folder and return the id of the new folder
    private static String cloneCycleGetId(CloseableHttpClient client, String cycleId, String newName, String parentFolderId) throws Exception {
        String cloneUrl = JIRA_BASE_URL + "/rest/zapi/latest/cycle/" + cycleId + "/clone";
        HttpPost post = new HttpPost(cloneUrl);

        post.addHeader("Authorization", "Basic " + getAuthHeader());
        post.addHeader("Content-Type", "application/json");

        String payload = "{ " +
                "\"name\": \"" + newName + "\"," +
                "\"projectId\": \"" + PROJECT_ID + "\"," +
                "\"folderId\": \"" + parentFolderId + "\"," +
                "\"versionId\": \"" + VERSION_ID + "\"" +
                "}";

        post.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));

        try (CloseableHttpResponse response = client.execute(post)) {
            int statusCode = response.getCode();
            if (statusCode != 200 && statusCode != 201) {
                throw new RuntimeException("❌ Clone failed for " + newName + " HTTP code: " + statusCode);
            }
            String result = readResponse(response);
            JsonNode responseJson = mapper.readTree(result);
            String newCycleId = responseJson.has("id") ? responseJson.get("id").asText() :
                    responseJson.has("cycleId") ? responseJson.get("cycleId").asText() :
                            null;
            if (newCycleId == null) {
                throw new RuntimeException("⚠️ Could not retrieve new cloned folder ID.");
            }
            System.out.println("✅ Cloned successfully → " + newName + " (new folder ID=" + newCycleId + ")");
            return newCycleId;
        }
    }

    private static String getAuthHeader() {
        return Base64.getEncoder()
                .encodeToString((USERNAME + ":" + PASSWORD).getBytes(StandardCharsets.UTF_8));
    }

    private static String readResponse(CloseableHttpResponse response) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        return sb.toString();
    }
}
