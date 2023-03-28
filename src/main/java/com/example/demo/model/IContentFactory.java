package com.example.demo.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface IContentFactory {

    //methods
    int createContent(String tableName) throws JSONException, IOException;
    Content createContentFromJson(JSONObject jsonObject) throws JSONException;
    String getFieldType(JSONObject fieldsObject);
    boolean isContentAlreadyInDatabaseWithAirtableId(Content content);
}
