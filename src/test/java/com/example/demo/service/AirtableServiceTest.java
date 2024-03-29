package com.example.demo.service;

import com.example.envUtils.DotenvConfig;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class AirtableServiceTest {


    @Test
    void getResponseListTest() throws JSONException, IOException {
        String tableName = "ContainerCodes";
        AirtableService airtableService = new AirtableService();
        List<JSONObject> expectedJSONObject = airtableService.getResponseList(tableName, DotenvConfig.get("BASE_ID"), DotenvConfig.get("ACCESS_TOKEN"));

        assertNotNull(expectedJSONObject);
        assertTrue(expectedJSONObject.size() > 0);
    }


    @Test
    void getResponseTest(){

    }


    @Test
    void createJSonObjectTest(){

    }


    @Test
    void getAirtableTabNamesTest(){

    }


    @Test
    void getTableNamesFromJSonArrayTest(){

    }

    @Test
    void separateUrlTest(){

    }


    @Test
    void findHttpsTest(){

    }

    @Test
    void reformattedUrlTest(){

    }


    /*
    @Test
    public void getResponseList() throws JSONException, IOException {
        String tableName = "ContainerCodes";
        List<JSONObject> actualJSONObject = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        HttpUriRequest request = new HttpGet(DotenvConfig.get("HTTP_AIRTABLE_TABLE") + DotenvConfig.get("BASE_ID") + "/" + tableName);
        request.setHeader("Authorization", "Bearer " + DotenvConfig.get("ACCESS_TOKEN"));

        AirtableService airtableService = new AirtableService();
        List<JSONObject> expectedJSONObject = airtableService.getResponseList(tableName, DotenvConfig.get("BASE_ID"),DotenvConfig.get("ACCESS_TOKEN"));
        System.out.println(expectedJSONObject);

        JSONObject outerObject = new JSONObject("{\"records\":[{\"id\":\"rec5rJvdQbfe4cABd\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":3,\"Code\":\"3456\",\"Team\":\"Team 3\"}},{\"id\":\"recBEOFXxFxRqQGAW\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":5,\"Code\":\"5678\",\"Team\":\"Team 5\"}},{\"id\":\"recBkeTrizx9uuj6M\",\"createdTime\":\"2021-04-26T09:18:47.000Z\",\"fields\":{\"Team number\":8,\"Code\":\"8901\",\"Team\":\"Team 8\"}},{\"id\":\"recO8LGJF0I9cseB0\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":2,\"Code\":\"2345\",\"Team\":\"Team 2\"}},{\"id\":\"recT02znG1Z11SOf9\",\"createdTime\":\"2021-04-26T09:06:06.000Z\",\"fields\":{\"Team number\":1,\"Code\":\"1234\",\"Team\":\"Team 1\"}},{\"id\":\"recUaIRMOyfuUT8nb\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":6,\"Code\":\"6789\",\"Team\":\"Team 6\"}},{\"id\":\"recZLDbu3XIqkTIAv\",\"createdTime\":\"2021-04-26T09:18:47.000Z\",\"fields\":{\"Team number\":9,\"Code\":\"9123\",\"Team\":\"Team 9\"}},{\"id\":\"recboKXZUrrYto0Vc\",\"createdTime\":\"2021-04-26T09:18:39.000Z\",\"fields\":{\"Team number\":4,\"Code\":\"4567\",\"Team\":\"Team 4\"}},{\"id\":\"recrGXkCRDZ10HFrA\",\"createdTime\":\"2021-04-26T09:18:46.000Z\",\"fields\":{\"Team number\":7,\"Code\":\"7890\",\"Team\":\"Team 7\"}}]}\n");
        JSONArray jsonArray = outerObject.getJSONArray("records");

        for (int i = 0, size = jsonArray.length(); i < size; i++)
        {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            actualJSONObject.add(objectInArray);
        }

        assertEquals(mapper.readTree(String.valueOf(expectedJSONObject)), mapper.readTree(String.valueOf(actualJSONObject)));
    }


    /*
    @Test
    public void testGetResponse() throws Exception {
        // Given
        String tableName = "myTable";
        String baseId = "myBaseId";
        String accessToken = "myAccessToken";
        String offset = "myOffset";
        String responseString = "{\"records\": [], \"offset\": \"myNewOffset\"}";
        HttpEntity entity = mock(HttpEntity.class);
        HttpResponse httpResponse = mock(HttpResponse.class);
        given(httpResponse.getEntity()).willReturn(entity);
        given(entity.getContent()).willReturn(new ByteArrayInputStream(responseString.getBytes()));
        HttpClient httpClient = mock(HttpClient.class);
        given(httpClient.execute(any(HttpGet.class))).willReturn(httpResponse);
        JSONObject expectedResponse = new JSONObject(responseString);

        // When
        JSONObject actualResponse = getResponse(tableName, baseId, accessToken, offset);

        // Then
        verify(httpClient).execute(request);
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }


    */












    /**
     * Test method that verify the tab names from Airtable
     * @throws IOException
     * @throws JSONException
     */
    @Test
    void getTableNamesFromJsonArray() throws IOException, JSONException {
        String[] expectedNames = {"Testimonials","Terminals","WordsClouds","Boxes","Arrows","Videos","Dictionnary","Transitions","ContainerPlaces","ContainerCodes","Pages"};
        JSONObject jsonObject = null;

        File f = new File("src/test/java/com/example/demo/testData/tables.json");
        if (f.exists()){
            InputStream is = new FileInputStream(f);
            String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
            jsonObject = new JSONObject(jsonTxt);
        }
        
        JSONArray tables = jsonObject.getJSONArray("tables");

        String[] actualNames = new String[tables.length()];


        for (int i = 0; i < tables.length(); i++) {
            JSONObject table = tables.getJSONObject(i);
            String tableName = table.getString("name");
            actualNames[i] = tableName;
        }

        for (int i = 0; i < actualNames.length; i++) {
            assertEquals(expectedNames[i],actualNames[i]);
        }
    }
}