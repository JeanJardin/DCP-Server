package com.example.demo.model;

import org.json.JSONException;

import java.io.IOException;

public interface IContentFactory {

    //methods
    int createContent(String tableName) throws JSONException, IOException;
}
