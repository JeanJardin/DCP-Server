package com.example.demo;

import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.envUtils.DotenvConfig;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        AirtableService airtableService = new AirtableService();

        airtableService.getResponseList("Testimonials", DotenvConfig.get("BASE_ID"),DotenvConfig.get("ACCESS_TOKEN"));

      //  String str = airtableService.getResponse("Testimonial");
    }
}
