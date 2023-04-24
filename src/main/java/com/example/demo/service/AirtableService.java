package com.example.demo.service;

import com.example.envUtils.DotenvConfig;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Service class that interacts with the Airtable API to retrieve data and perform operations on it.
 */

@Service
public class AirtableService implements IAirtableService {

    // Instance variables for the Airtable access credentials
    Dotenv dotenv = Dotenv.configure().load();
    String ACCESS_TOKEN = dotenv.get("ACCESS_TOKEN");
    String BASE_ID = dotenv.get("BASE_ID");
    String HTTP_AIRTABLE_TABLES = dotenv.get("HTTP_AIRTABLE_TABLES");

    // Instance variables for the HTTP client and request objects
    private static HttpClient httpClient;
    private static HttpGet request;

    // List to store JSONObjects retrieved from Airtable
    private static List<JSONObject> jsonObjectList;

    /**
     * Makes a GET request to the Airtable API to retrieve a list of JSONObjects for a given table.
     * <p>
     * The list has a maximum of 200 records.
     *
     * @param tableName   The name of the table to retrieve records from.
     * @param baseId      The ID of the base that the table belongs to.
     * @param accessToken The access token to use for authorization.
     * @return A list of JSONObjects representing the records in the table.
     * @throws JSONException If there is an error parsing the JSON response from Airtable.
     * @throws IOException   If there is an error making the HTTP request to Airtable.
     */
    public static List<JSONObject> getResponseList(String tableName, String baseId, String accessToken) throws JSONException, IOException {

        httpClient = HttpClientBuilder.create().build();
        request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName + "?maxRecords=200");
        request.setHeader("Authorization", "Bearer " + accessToken);
        jsonObjectList = new ArrayList<>();
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        System.out.println(response);
        JSONObject outerObject = new JSONObject(response);
        JSONArray jsonArray = outerObject.getJSONArray("records");
        for (int i = 0, size = jsonArray.length(); i < size; i++) {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            jsonObjectList.add(objectInArray);
        }
        System.out.println("Elements founds : " + jsonObjectList.size());
        System.out.println(jsonObjectList);
        return jsonObjectList;
    }
    /**

     Makes a GET request to the Airtable API to retrieve a JSONObject for a given table, starting from a given offset.

     @param tableName The name of the table to retrieve records from.

     @param baseId The ID of the base that the table belongs to.

     @param accessToken The access token to use for authorization.

     @param offset The offset to start retrieving records from.

     @return A JSONObject representing the response from the Airtable API.

     @throws IOException If there is an error making the HTTP request to Airtable.

     @throws JSONException If there is an error parsing the JSON response from Airtable.
     */
    JSONObject getResponse(String tableName, String baseId, String accessToken, String offset) throws IOException, JSONException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (offset != null) {
            request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName + "?offset=" + offset);
        } else {
            request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName);
        }

        request.setHeader("Authorization", accessToken);
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        System.out.println(response);
        return new JSONObject(response);
    }
    /**

     Fetches all records from a specific table in Airtable API and returns a list of JSON objects.

     It makes multiple HTTP requests if there are more than 100 records to retrieve.

     @param tableName the name of the table to fetch records from

     @param baseID the ID of the base containing the table

     @param accessToken the authorization token for Airtable API

     @return a list of JSON objects containing the records from the specified table

     @throws JSONException if there's an error parsing the JSON response

     @throws IOException if there's an error making the HTTP request
     */
    public List<JSONObject> createJsonObject(String tableName, String baseID, String accessToken) throws JSONException, IOException {
        String offset = null;
        List<JSONObject> jsonObjectList = new ArrayList<>();
        do {
            var response = getResponse(tableName, baseID, accessToken, offset);
            JSONArray jsonArray = response.getJSONArray("records");
            for (int i = 0, size = jsonArray.length(); i < size; i++) {
                JSONObject objectInArray = jsonArray.getJSONObject(i);
                jsonObjectList.add(objectInArray);
            }
            offset = response.optString("offset", null);

        } while (offset != null);

        return jsonObjectList;
    }
    /**

     Gets an array of all the table names in the Airtable base.
     @return an array of table names
     @throws JSONException if there is an error parsing the JSON response
     @throws IOException if there is an error sending or receiving the HTTP request/response
     */
    @Override
    public String[] getAirtableTabNames() throws JSONException, IOException {
        httpClient = HttpClientBuilder.create().build();
        request = new HttpGet(HTTP_AIRTABLE_TABLES + BASE_ID + "/tables");
        request.setHeader("Authorization", ACCESS_TOKEN);

        String response = EntityUtils.toString(httpClient.execute(request).getEntity());

        JSONObject jsonObject = new JSONObject(response);
        System.out.println("JSON OBJECT IS" + jsonObject);
        System.out.println("ACCESS_TOKEN IS :" + ACCESS_TOKEN);
        System.out.println("BASE_ID IS :" + BASE_ID);
        JSONArray tables = jsonObject.getJSONArray("tables");

        return getTableNamesFromJsonArray(tables);
    }
    /**

     Extracts table names from a JSONArray of tables.

     @param tables the JSONArray of tables

     @return an array of table names

     @throws JSONException if there is an error parsing the JSON data
     */
    private String[] getTableNamesFromJsonArray(JSONArray tables) throws JSONException {
        String[] tableNames = new String[tables.length()];

        for (int i = 0; i < tables.length(); i++) {
            JSONObject table = tables.getJSONObject(i);
            String tableName = table.getString("name");
            tableNames[i] = tableName;
        }

        return tableNames;
    }

    /**

     Recursively searches a JSONObject for values starting with "https://" and returns them in a list.

     @param jsonObject the JSONObject to search

     @return a list of all values starting with "https://"

     @throws JSONException if there is an error parsing the JSONObject
     */
    public List<String> findHttps(JSONObject jsonObject) throws JSONException {
        List<String> result = new ArrayList<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                result.addAll(findHttps((JSONObject) value));
            } else if (value instanceof JSONArray) {
                JSONArray array = (JSONArray) value;
                for (int i = 0; i < array.length(); i++) {
                    Object arrayValue = array.get(i);
                    if (arrayValue instanceof JSONObject) {
                        result.addAll(findHttps((JSONObject) arrayValue));
                    }
                }
            } else if (value instanceof String && ((String) value).startsWith("https://")) {
                result.add((String) value);
            }
        }
        return result;
    }
}
