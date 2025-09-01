package com.jira.cloner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JiraService {

    private final String baseUrl;
    private final String email;
    private final String apiToken;
    private final String projectKey;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public JiraService(String baseUrl, String email, String apiToken, String projectKey) {
        this.baseUrl = baseUrl;
        this.email = email;
        this.apiToken = apiToken;
        this.projectKey = projectKey;
    }

    private String authHeader() {
        String encoded = Base64.getEncoder().encodeToString((email + ":" + apiToken).getBytes());
        return "Basic " + encoded;
    }

    public Map<String, String> getAllVersions() throws Exception {
        String url = baseUrl + "/rest/api/3/project/" + projectKey + "/versions";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", authHeader())
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) throw new Exception("Failed to fetch versions: " + response.body());

        Version[] versions = mapper.readValue(response.body(), Version[].class);
        Map<String, String> map = new LinkedHashMap<>();
        for (Version v : versions) map.put(v.getName(), v.getId());
        return map;
    }

    public String getVersionId(String versionName) throws Exception {
        Map<String, String> versions = getAllVersions();
        if (!versions.containsKey(versionName)) throw new Exception("Version not found: " + versionName);
        return versions.get(versionName);
    }

    // Actual Zephyr cycle cloning
    public void cloneCycle(String sourceCycleId, String targetVersionId, String newCycleName) throws Exception {
        String url = baseUrl + "/rest/zapi/latest/cycle";
        String json = "{ \"name\": \"" + newCycleName + "\", \"projectId\": " + projectKey + ", \"versionId\": " + targetVersionId + ", \"cloneCycleId\": " + sourceCycleId + " }";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Authorization", authHeader())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 201)
            throw new Exception("Failed to clone cycle: " + response.body());

        System.out.println("Cycle cloned successfully: " + newCycleName);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Version {
        private String name;
        private String id;

        public String getName() { return name; }
        public String getId() { return id; }
    }
}
