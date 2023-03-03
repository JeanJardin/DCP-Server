package com.example.demo.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

public interface IAirtableService {


    //Methods
    HttpGet request();
    JSONObject getResponse(String tableName);

}
