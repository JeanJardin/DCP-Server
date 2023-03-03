package com.example.demo.service;

import org.json.JSONObject;

public interface IHashService {

    //methods
    String hashContent(JSONObject jsonObject);
    String hashBinaryContent(String resourceUrl);

}
