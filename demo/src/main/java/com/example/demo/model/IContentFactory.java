package com.example.demo.model;

import org.json.JSONException;

import javax.swing.text.AbstractDocument;
import java.io.IOException;

public interface IContentFactory {

    //methods
    Content[] createContent(String tableName) throws JSONException, IOException;
}
