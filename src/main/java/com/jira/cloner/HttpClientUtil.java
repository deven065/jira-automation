package com.jira.cloner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class HttpClientUtil {
    private HttpClient client;
    private String authHeader;

    public HttpClientUtil(String email, String apiToken) {
        client = HttpClient.newHttpClient();
        String encodedAuth = Base64.getEncoder().encodeToString((email + ":" + apiToken).getBytes());
        authHeader = "Basic " + encodedAuth;
    }

    public String get(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) throw new Exception("GET Error: " + response.body());
        return response.body();
    }

    public String post(String url, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authHeader)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) throw new Exception("POST Error: " + response.body());
        return response.body();
    }
}
