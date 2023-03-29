package com.example.demo.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public interface IAirtableService {

    List<JSONObject> getResponseList(String tableName,String baseId,String accessToken) throws JSONException, IOException;
    String[] getAirtableTabNames() throws JSONException, IOException;
}
