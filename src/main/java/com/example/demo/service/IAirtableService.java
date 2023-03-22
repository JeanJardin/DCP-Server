package com.example.demo.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IAirtableService {


    //Methods
   // HttpGet request(String tableName) throws JSONException, IOException, NoSuchAlgorithmException;
    List<JSONObject> getResponseList(String tableName,String baseId,String accessToken) throws JSONException, IOException;
    /*
    JSONObject getResponse(String tableName, HttpGet request) throws JSONException, NoSuchAlgorithmException, IOException;

     */
}
