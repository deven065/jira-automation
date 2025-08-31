package com.jira.cloner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

public class JiraCloner {

    private static final String BASE_URL = "http://localhost:3000/folders";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Step 1: Fetch all folders
            HttpGet get = new HttpGet(BASE_URL);
            var response = client.execute(get);
            JsonNode folders = mapper.readTree(response.getEntity().getContent());

            // Step 2: Find Regression Suite under RELEASED
            JsonNode releasedFolder = folders.get(0).get("children").get(1); // RELEASED
            JsonNode regressionSuite = releasedFolder.get("children").get(0); // Regression Suite

            System.out.println("Found Regression Suite: " + regressionSuite);

            // Step 3: Clone Regression Suite into UNRELEASED
            JsonNode unreleasedFolder = folders.get(0).get("children").get(0);

            // Append suffix "Regression R5"
            var clonedSuite = mapper.createObjectNode();
            clonedSuite.put("id", 999); // mock id
            clonedSuite.put("name", regressionSuite.get("name").asText() + " Regression R5");

            // Copy children (cycles)
            var clonedChildren = mapper.createArrayNode();
            for (JsonNode cycle : regressionSuite.get("children")) {
                var newCycle = mapper.createObjectNode();
                newCycle.put("id", 1000 + cycle.get("id").asInt());
                newCycle.put("name", cycle.get("name").asText() + " Regression R5");
                clonedChildren.add(newCycle);
            }
            clonedSuite.set("children", clonedChildren);

            // Add to UNRELEASED children
            ((com.fasterxml.jackson.databind.node.ArrayNode) unreleasedFolder.get("children")).add(clonedSuite);

            // Step 4: Update server via PATCH
            HttpPatch patch = new HttpPatch(BASE_URL + "/1"); // update All Releases root
            patch.setHeader("Content-Type", "application/json");
            patch.setEntity(new StringEntity(mapper.writeValueAsString(folders.get(0)), ContentType.APPLICATION_JSON));

            client.execute(patch);
            System.out.println("Cloning completed! Check http://localhost:3000/folders/2");
        }
    }
}
