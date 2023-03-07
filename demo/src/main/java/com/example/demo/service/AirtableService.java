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

public class AirtableService implements IAirtableService{

    //Variables
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    String accesToken = "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96";
    String baseId = "applto3j1obQLAVnr";
    HttpClient httpClient = HttpClientBuilder.create().build();
    String tableName = "Testimonials";


    @Override
    public HttpGet request() throws IOException, JSONException, NoSuchAlgorithmException {
        HttpGet request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/");
        JSONObject outerObject =  getResponse(tableName, request);
        return request ;
    }


    @Override
    public JSONObject getResponse(String tableName, HttpGet request) throws JSONException, NoSuchAlgorithmException, IOException {

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
}
