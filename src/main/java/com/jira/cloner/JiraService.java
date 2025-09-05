package com.jira.cloner;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class JiraService {

    private String authHeader;

    public JiraService() {
        String auth = Config.JIRA_EMAIL + ":" + Config.JIRA_API_TOKEN;
        this.authHeader = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private String sendRequest(String method, String endpoint, String payload) throws Exception {
        URL url = new URL(Config.BASE_URL + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Authorization", authHeader);
        conn.setRequestProperty("Content-Type", "application/json");

        if (payload != null) {
            conn.setDoOutput(true);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode < 400) ? conn.getInputStream() : conn.getErrorStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();

        if (responseCode >= 400) {
            throw new Exception("Failed request: " + response);
        }

        return response.toString();
    }

    // Create a cloned cycle
    public String cloneCycle(String clonedCycleId, String newCycleName) throws Exception {
        String payload = "{"
                + "\"clonedCycleId\":\"" + clonedCycleId + "\","
                + "\"name\":\"" + newCycleName + "\","
                + "\"projectId\":\"" + Config.PROJECT_ID + "\","
                + "\"versionId\":\"" + Config.VERSION_ID + "\""
                + "}";

        return sendRequest("POST", "/rest/zapi/latest/cycle", payload);
    }
}
