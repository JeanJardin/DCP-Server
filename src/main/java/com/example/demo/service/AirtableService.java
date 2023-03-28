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

    //Fields
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private HttpClient httpClient;
    private HttpGet request;
    private List<JSONObject> jsonObjectList;

    /*
    TEST COMMIT
     */

    /**
     * Method to get the JSONObject through the airtable get
     *
     * @param tableName name of the table we want in the airtable database
     * @return a list of jsonObject correspond to the content
     */
    @Override
    public List<JSONObject> getResponseList(String tableName, String baseId, String accessToken) throws JSONException, IOException {

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

        request.setHeader("Authorization", "Bearer " + accessToken);
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        System.out.println(response);
        return new JSONObject(response);
    }


    public List<JSONObject> createJsonObject(String tableName, String baseID, String accessToken) throws JSONException, IOException {
        String offset = null;
        JSONObject jsonObject;
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

    @Override
    public String[] getAirtableTabNames() throws JSONException, IOException {
        httpClient = HttpClientBuilder.create().build();
        request = new HttpGet("https://api.airtable.com/v0/meta/bases/" + DotenvConfig.get("BASE_ID") + "/tables");
        request.setHeader("Authorization", "Bearer " + DotenvConfig.get("ACCESS_TOKEN"));

        String response = EntityUtils.toString(httpClient.execute(request).getEntity());

        JSONObject jsonObject = new JSONObject(response);
        JSONArray tables = jsonObject.getJSONArray("tables");


        return getTableNamesFromJsonArray(tables);
    }

    private String[] getTableNamesFromJsonArray(JSONArray tables) throws JSONException {
        String[] tableNames = new String[tables.length()];

        for (int i = 0; i < tables.length(); i++) {
            JSONObject table = tables.getJSONObject(i);
            String tableName = table.getString("name");
            tableNames[i] = tableName;
        }

        return tableNames;
    }

    private String[] separateUrls(String urls) {
        if (urls == null || urls.isEmpty()) {
            return new String[]{};
        }
        return urls.split(",");
    }

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


    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public HttpGet getRequest() {
        return request;
    }

    public void setRequest(HttpGet request) {
        this.request = request;
    }

    public List<JSONObject> getJsonObjectList() {
        return jsonObjectList;
    }

    public void setJsonObjectList(List<JSONObject> jsonObjectList) {
        this.jsonObjectList = jsonObjectList;
    }


}
