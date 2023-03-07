package com.example.demo.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface IAirtableService {


    //Methods
    HttpGet request() throws JSONException, IOException, NoSuchAlgorithmException;

    JSONObject getResponse(String tableName, HttpGet request) throws JSONException, NoSuchAlgorithmException, IOException;
}
