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
    public List<JSONObject> getResponseList(String tableName,String baseId,String accessToken) throws JSONException, IOException {

        httpClient = HttpClientBuilder.create().build();
        request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName);
        request.setHeader("Authorization", "Bearer " + accessToken);
        jsonObjectList = new ArrayList<>();
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        JSONObject outerObject = new JSONObject(response);
        JSONArray jsonArray = outerObject.getJSONArray("records");
        for (int i = 0, size = jsonArray.length(); i < size; i++) {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            jsonObjectList.add(objectInArray);
        }
        System.out.println("Elements founds : "+jsonObjectList.size());
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
