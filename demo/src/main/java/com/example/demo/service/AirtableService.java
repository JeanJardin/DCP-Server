package com.example.demo.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class AirtableService implements IAirtableService{

    //Variables
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    String accessToken = "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96";
    String baseId = "applto3j1obQLAVnr";
    HttpClient httpClient = HttpClientBuilder.create().build();
    //String tableName = "Testimonials";

    @Override
    public HttpGet request(String tableName) throws IOException, JSONException {
        HttpGet request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/");
        request.setHeader("Authorization", "Bearer " + "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96");
        JSONObject outerObject =  getResponse(tableName, request);
        request.setHeader("Authorization", "Bearer " + accessToken);
        return request;
    }
    @Override
    public JSONObject getResponse(String tableName, HttpGet request) throws JSONException, IOException {

        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        JSONObject outerObject = new JSONObject(response);
        JSONArray jsonArray = outerObject.getJSONArray("records");
        for (int i = 0, size = jsonArray.length(); i < size; i++)
        {
            JSONObject objectInArray = jsonArray.getJSONObject(i);

            return objectInArray;
        }
        return null;
    }

    public List<JSONObject> getResponseList(String tableName, HttpGet request) throws JSONException, IOException {
        List<JSONObject> jsonObjectList = new ArrayList<>();
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        JSONObject outerObject = new JSONObject(response);
        JSONArray jsonArray = outerObject.getJSONArray("records");
        for (int i = 0, size = jsonArray.length(); i < size; i++)
        {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            jsonObjectList.add(objectInArray);
        }
        return jsonObjectList;
    }
}
