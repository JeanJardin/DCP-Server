package com.example.demo.service;

import com.example.demo.model.Content;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface IContentService {
     void addContent(Content content);
     Content getContentByAirtableId(String id);

}
