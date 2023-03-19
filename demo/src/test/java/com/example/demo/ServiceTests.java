package com.example.demo;

import com.example.demo.model.Content;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.demo.service.HashService;
import com.example.envUtils.DotenvConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        String tableName = "Testimonials";
        // Given
        HttpUriRequest request = new HttpGet(DotenvConfig.get("HTTP_AIRTABLE_TABLE") + DotenvConfig.get("BASE_ID") + "/" + tableName);
        request.setHeader("Authorization", "Bearer " + DotenvConfig.get("ACCESS_TOKEN"));

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

        HttpUriRequest request = new HttpGet(DotenvConfig.get("HTTP_AIRTABLE_TABLE") + DotenvConfig.get("BASE_ID") + "/" + tableName);
        request.setHeader("Authorization", "Bearer " + DotenvConfig.get("ACCESS_TOKEN"));

        AirtableService airtableService = new AirtableService();
        List<JSONObject> expectedJSONObject = airtableService.getResponseList(tableName);

        JSONObject outerObject = new JSONObject("{\"records\":[{\"id\":\"rec9kck7RLCNmdQvp\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":7,\"Code\":\"7890\",\"Team\":\"Team 7\"}},{\"id\":\"recBEhzSG9CNnoZjY\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":1,\"Code\":\"1234\",\"Team\":\"Team 1\"}},{\"id\":\"recCOXRhOGSggpjr0\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":6,\"Code\":\"6789\",\"Team\":\"Team 6\"}},{\"id\":\"recHpSbZ35lcGpTEk\",\"createdTime\":\"2021-04-26T09:18:47.000Z\",\"fields\":{\"Team number\":9,\"Code\":\"9123\",\"Team\":\"Team 9\"}},{\"id\":\"recN5YvIQjS0qILF2\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":3,\"Code\":\"3456\",\"Team\":\"Team 3\"}},{\"id\":\"recT2ZXuUz4KPUbZ1\",\"createdTime\":\"2021-04-26T09:18:39.000Z\",\"fields\":{\"Team number\":4,\"Code\":\"4567\",\"Team\":\"Team 4\"}},{\"id\":\"recjYtTWiHaVQ0uaB\",\"createdTime\":\"2021-04-26T09:18:47.000Z\",\"fields\":{\"Team number\":8,\"Code\":\"8901\",\"Team\":\"Team 8\"}},{\"id\":\"recji3FsxNaDMmREL\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":5,\"Code\":\"5678\",\"Team\":\"Team 5\"}},{\"id\":\"recwM0GeF8lVyYpFP\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":2,\"Code\":\"2345\",\"Team\":\"Team 2\"}}]}\n");
        JSONArray jsonArray = outerObject.getJSONArray("records");

        for (int i = 0, size = jsonArray.length(); i < size; i++)
        {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            actualJSONObject.add(objectInArray);
        }

        assertEquals(mapper.readTree(String.valueOf(expectedJSONObject)), mapper.readTree(String.valueOf(actualJSONObject)));
    }

        @Test
        public void testCreateContent() throws JSONException, IOException {

            AirtableService airtableService;
            HashService hashService = new HashService();
            ContentService contentService = new ContentService();


            // create sample JSON data to simulate response from Airtable service
            JSONObject jsonObject1 = new JSONObject("{\"id\": \"rec123\", \"fields\": {\"VideoURL\": \"https://example.com/video1.mp4\"}}");
            JSONObject jsonObject2 = new JSONObject("{\"id\": \"rec456\", \"fields\": {\"File\": \"https://example.com/file1.pdf\"}}");
            List<JSONObject> jsonObjectList = new ArrayList<>();
            jsonObjectList.add(jsonObject1);
            jsonObjectList.add(jsonObject2);

            // create sample Content object and hash values to simulate behavior of hashService
            Content content = new Content();
            content.setBinaryHash("abc123");
            content.setJsonHash("def456");
            when(hashService.hashBinaryContent(anyString())).thenReturn("abc123");
            when(hashService.hashContent((JSONObject) any(JSONObject.class))).thenReturn("def456");

            /*
            // mock behavior of checkIfHashIsAlreadyInDB() method to return false
            when(contentService.checkIfHashIsAlreadyInDB(any(Content.class))).thenReturn(false);

            // simulate behavior of addContent() method
            doNothing().when(contentService).addContent(any(Content.class));

            // simulate behavior of getResponseList() method of AirtableService
            when(airtableService.getResponseList(eq("TestTable"))).thenReturn(jsonObjectList);

            // call method under test
            Content[] result = contentServiceImpl.createContent("TestTable");

            // verify behavior and assert results
            verify(hashService, times(2)).hashBinaryContent(anyString());
            verify(hashService, times(2)).hashContent(any(JSONObject.class));
            verify(contentService, times(2)).checkIfHashIsAlreadyInDB(any(Content.class));
            verify(contentService, times(2)).addContent(any(Content.class));
            Assert.assertEquals(0, result.length);
            */

        }

}
