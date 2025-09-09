package com.jira.cloner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class ZephyrSquadClient {
    private static final String ENDPOINT = "/rest/zapi/latest/cycle";

    public boolean cloneCycle() {
        try {
            String auth = ConfigSquad.EMAIL + ":" + ConfigSquad.API_TOKEN;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

            String jsonBody = String.format(
                    "{ \"clonedCycleId\": %d, \"name\": \"%s\", \"projectId\": %d, \"versionId\": %d }",
                    ConfigSquad.CLONED_CYCLE_ID,
                    ConfigSquad.NEW_CYCLE_NAME,
                    ConfigSquad.PROJECT_ID,
                    ConfigSquad.VERSION_ID
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ConfigSquad.BASE_URL + ENDPOINT))
                    .header("Authorization", "Basic " + encodedAuth)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
            System.out.println("Response: " + response.body());

            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
