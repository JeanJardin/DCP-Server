package com.example.demo;

import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, JSONException {
        AirtableService airtableService = new AirtableService();

        airtableService.getResponseList("Testimonials" );

      //  String str = airtableService.getResponse("Testimonial");
    }
}
