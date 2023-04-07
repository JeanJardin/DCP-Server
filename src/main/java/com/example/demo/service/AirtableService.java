package com.example.demo.service;

import com.example.envUtils.DotenvConfig;
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
 * This class provides methods to interact with an Airtable database and retrieve data from it.
 */
@Service
public class AirtableService implements IAirtableService {

    private static HttpClient httpClient;
    private static HttpGet request;
    private static List<JSONObject> jsonObjectList;

    /**
     * Method to get the JSONObject through the airtable get
     *
     * @param tableName name of the table we want in the airtable database
     * @return a list of jsonObject correspond to the content
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

    JSONObject getResponse(String tableName, String baseId, String accessToken, String offset) throws IOException, JSONException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        if (offset != null) {
            request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName + "?offset=" + offset);
        } else {
            request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName);
        }

        request.setHeader("Authorization",accessToken);
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        System.out.println(response);
        return new JSONObject(response);
    }


    /**
     * This method retrieves all records from a given Airtable table and returns them as a list of JSON objects.
     *
     * @param tableName the name of the Airtable table to retrieve records from
     * @param baseID the ID of the Airtable base containing the specified table
     * @param accessToken the access token used for authentication
     * @return a list of JSON objects representing the retrieved records
     * @throws JSONException if there is an error processing the retrieved JSON data
     * @throws IOException if there is an error making the HTTP request
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
     * This method retrieves a list of Airtable table names associated with the specified base ID using the Airtable API.
     *
     * @return an array of Airtable table names associated with the specified base ID
     * @throws JSONException if there is an error processing the retrieved JSON data
     * @throws IOException if there is an error making the HTTP request
     */
    @Override
    public String[] getAirtableTabNames() throws JSONException, IOException {
        httpClient = HttpClientBuilder.create().build();
        request = new HttpGet(DotenvConfig.get("HTTP_AIRTABLE_TABLES") + DotenvConfig.get("BASE_ID") + "/tables");
        request.setHeader("Authorization",DotenvConfig.get("ACCESS_TOKEN"));

        String response = EntityUtils.toString(httpClient.execute(request).getEntity());

        JSONObject jsonObject = new JSONObject(response);
        System.out.println("JSON OBJECT IS" +jsonObject);
        System.out.println("ACCESS_TOKEN IS :"+DotenvConfig.get("ACCESS_TOKEN"));
        System.out.println("BASE_ID IS :"+DotenvConfig.get("BASE_ID"));
        JSONArray tables = jsonObject.getJSONArray("tables");

        return getTableNamesFromJsonArray(tables);
    }

    /**
     * This method retrieves an array of Airtable table names from a JSON array of Airtable tables.
     *
     * @param tables the JSON array of Airtable tables to extract table names from
     * @return an array of Airtable table names extracted from the input JSON array
     * @throws JSONException if there is an error processing the input JSON data
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
     * This method separates a comma-separated list of URLs into individual URL strings and returns them as an array.
     *
     * @param urls the comma-separated list of URLs to separate
     * @return an array of individual URL strings
     */
    private String[] separateUrls(String urls) {
        if (urls == null || urls.isEmpty()) {
            return new String[]{};
        }
        return urls.split(",");
    }

    /**
     * This method recursively searches for all HTTPS URLs in a JSON object and its nested JSON objects and arrays.
     *
     * @param jsonObject the JSON object to search for HTTPS URLs in
     * @return a list of all HTTPS URLs found in the input JSON object and its nested JSON objects and arrays
     * @throws JSONException if there is an error processing the input JSON data
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

    /**
     * Reformats the given URL string by removing "[" and "]" characters, removing all whitespace characters, replacing all backslashes with forward slashes, replacing any sequence of more than one forward slash with just one, and replacing any sequence of one or more forward slashes followed by a colon with just two forward slashes.
     *
     * @param url The URL string to be reformatted.
     * @return The reformatted URL string.
     */
    private String reformattedUrl(String url) {
        // Replace [ and ] in the URL
        url = url.replaceAll("\\[", "");
        url = url.replaceAll("]", "");
        // Replace the " " in the URL
        url = url.replaceAll("\"", "");
        // Replace all occurrences of backslashes with forward slashes
        url = url.replaceAll("\\\\", "/");
        // Replace any sequence of more than one forward slash with just one
        url = url.replaceAll("/{2,}", "/");
        // Replace any sequence of one or more forward slashes followed by a colon with just two forward slashes
        url = url.replaceAll("(?<=https:)/+", "//");
        return url;
    }
}
