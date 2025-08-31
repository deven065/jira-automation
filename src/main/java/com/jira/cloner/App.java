package com.jira.cloner;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Simple test app to connect with mock Jira server and fetch folders
 */
public class App {
    public static void main(String[] args) {
        String url = "http://localhost:3000/folders"; // mock server endpoint

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Make GET request
            HttpGet request = new HttpGet(url);
            ClassicHttpResponse response = (ClassicHttpResponse) client.execute(request);

            System.out.println("HTTP Status: " + response.getCode());

            // Read response
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent())
            );
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // Parse JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json.toString());

            System.out.println("\n📂 Received JSON Data:");
            System.out.println(root.toPrettyString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
