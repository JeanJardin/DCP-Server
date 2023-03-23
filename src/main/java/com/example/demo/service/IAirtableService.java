package com.example.demo.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IAirtableService {

    List<JSONObject> getResponseList(String tableName,String baseId,String accessToken) throws JSONException, IOException;
    String[] getAirtableTabNames() throws JSONException, IOException;
    }
