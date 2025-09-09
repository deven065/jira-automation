package com.jira.cloner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ZephyrScaleClient {
    public boolean cloneCycle() {
        try {
            String jsonBody = String.format(
                    "{ \"name\": \"%s\", \"clonedCycleId\": \"%s\", \"projectKey\": \"%s\" }",
                    ConfigScale.NEW_CYCLE_NAME,
                    ConfigScale.CLONED_CYCLE_ID,
                    ConfigScale.PROJECT_KEY
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ConfigScale.BASE_URL + "/testcycles"))
                    .header("Authorization", "Bearer " + ConfigScale.BEARER_TOKEN)
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
