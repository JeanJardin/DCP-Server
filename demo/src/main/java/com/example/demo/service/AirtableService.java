package com.example.demo.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

public class AirtableService implements IAirtableService{

    //Variables
    String accesToken = null;
    String baseId = null;
    HttpClient httpClient = null;

    @Override
    public HttpGet request() {
        return null;
    }

    @Override
    public JSONObject getResponse(String tableName) {
        return null;
    }
}
