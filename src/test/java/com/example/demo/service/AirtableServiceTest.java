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