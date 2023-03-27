package com.example.demo;

import com.example.demo.service.AirtableService;
import com.example.envUtils.DotenvConfig;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        AirtableService airtableService = new AirtableService();
        //airtableService.getResponseList("Boxes", DotenvConfig.get("BASE_ID"),DotenvConfig.get("ACCESS_TOKEN"));
        System.err.println(airtableService.createJsonObject("Boxes", DotenvConfig.get("BASE_ID"), DotenvConfig.get("ACCESS_TOKEN")));
    }
}
