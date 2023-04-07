package com.example.demo.service;

import org.json.JSONException;

import java.io.IOException;

public interface IAirtableService {


    String[] getAirtableTabNames() throws JSONException, IOException;
}
