package com.example.demo;

import com.example.demo.service.AirtableService;
import com.example.demo.service.HashService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTests {

    @Test
    public void testHashJson() {
        HashService hashService = new HashService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "John");
            jsonObject.put("age", 30);
            jsonObject.put("city", "New York");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String digest = hashService.hashContent(jsonObject);
        assertEquals("f16ad52c441d68260fa08eac10f01803", digest);
    }

    @Test
    public void testHashBinary() {
        HashService hashService = new HashService();
        String digest = hashService.hashBinaryContent("https://scolcast.ch/system/files/episodes/videos/d2239ff867d5d59ef4be6cab28848f7d6daf6da9/original/3.phrases_additionnelles_enneigement.mp4");
        assertEquals("d4ba482b58b71960a32e5c1de85c58d4", digest);
    }

    @Test
    public void testCompareHashSignature() {
        HashService hashService = new HashService();
        String hashMongoDB = "f16ad52c441d68260fa08eac10f01803";
        hashService.compareHashSignature(hashMongoDB, "f16ad52c441d68260fa08eac10f01803");
    }

    @Test
    public void testRequestResponse() throws IOException {
        // Given
        HttpUriRequest request = new HttpGet("https://api.airtable.com/v0/" + "applto3j1obQLAVnr" + "/" + "Testimonials");
        request.setHeader("Authorization", "Bearer " + "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96");

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }

    @Test
    public void getResponseList() throws JSONException, NoSuchAlgorithmException, IOException {
        String tableName = "ContainerCodes";
        List<JSONObject> actualJSONObject = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        HttpGet request = new HttpGet("https://api.airtable.com/v0/" + "applto3j1obQLAVnr" + "/" + tableName);
        request.setHeader("Authorization", "Bearer " + "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96");

        AirtableService airtableService = new AirtableService();
        List<JSONObject> expectedJSONObject = airtableService.getResponseList(tableName, request);

        JSONObject outerObject = new JSONObject("{\"records\":[{\"id\":\"rec9kck7RLCNmdQvp\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":7,\"Code\":\"7890\",\"Team\":\"Team 7\"}},{\"id\":\"recBEhzSG9CNnoZjY\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":1,\"Code\":\"1234\",\"Team\":\"Team 1\"}},{\"id\":\"recCOXRhOGSggpjr0\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":6,\"Code\":\"6789\",\"Team\":\"Team 6\"}},{\"id\":\"recHpSbZ35lcGpTEk\",\"createdTime\":\"2021-04-26T09:18:47.000Z\",\"fields\":{\"Team number\":9,\"Code\":\"9123\",\"Team\":\"Team 9\"}},{\"id\":\"recN5YvIQjS0qILF2\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":3,\"Code\":\"3456\",\"Team\":\"Team 3\"}},{\"id\":\"recT2ZXuUz4KPUbZ1\",\"createdTime\":\"2021-04-26T09:18:39.000Z\",\"fields\":{\"Team number\":4,\"Code\":\"4567\",\"Team\":\"Team 4\"}},{\"id\":\"recjYtTWiHaVQ0uaB\",\"createdTime\":\"2021-04-26T09:18:47.000Z\",\"fields\":{\"Team number\":8,\"Code\":\"8901\",\"Team\":\"Team 8\"}},{\"id\":\"recji3FsxNaDMmREL\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":5,\"Code\":\"5678\",\"Team\":\"Team 5\"}},{\"id\":\"recwM0GeF8lVyYpFP\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":2,\"Code\":\"2345\",\"Team\":\"Team 2\"}}]}\n");
        JSONArray jsonArray = outerObject.getJSONArray("records");

        for (int i = 0, size = jsonArray.length(); i < size; i++)
        {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            actualJSONObject.add(objectInArray);
        }

        assertEquals(mapper.readTree(String.valueOf(expectedJSONObject)), mapper.readTree(String.valueOf(actualJSONObject)));
    }
}
