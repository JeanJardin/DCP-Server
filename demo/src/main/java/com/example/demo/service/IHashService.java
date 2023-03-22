package com.example.demo.service;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

public interface IHashService {

    //methods
    String hashContent(JSONObject jsonObject) throws NoSuchAlgorithmException, JSONException;
    String hashBinaryContent(String resourceUrl);
    boolean compareHashSignature(String hashMongoDB, String hashTablet);

}
