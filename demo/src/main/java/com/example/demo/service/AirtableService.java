package com.example.demo.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
@Service
public class AirtableService implements IAirtableService{

    //Variables
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    String accessToken = "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96";
    String baseId = "applto3j1obQLAVnr";


    @Override
    public HttpGet request(String tableName) throws IOException, JSONException {
        HttpGet request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/");
        request.setHeader("Authorization", "Bearer " + "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96");
        request.setHeader("Authorization", "Bearer " + accessToken);
        List<JSONObject> outerObject =  getResponseList(tableName);
        return request;
    }


    public List<JSONObject> getResponseList(String tableName) throws JSONException, IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/"+tableName);
        request.setHeader("Authorization", "Bearer " + accessToken);
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
