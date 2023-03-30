package com.example.demo.controllers;

import com.example.demo.model.Content;
import com.example.demo.model.ContentFactory;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.envUtils.DotenvConfig;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

/**
 * The ContentController class provides REST endpoints for managing contents.
 * It is responsible for creating, retrieving, and adding content to the system.
 */
@RestController
public class ContentController {

    private final ContentFactory contentFactory;
    private final ContentService contentService;

    private final AirtableService airtableService;

    /**
     * Constructs a new instance of the ContentController class with the specified parameters.
     *
     * @param contentService  the service that manages content
     * @param contentFactory  the factory used for creating content
     * @param airtableService
     */
    @Autowired
    public ContentController(ContentService contentService, ContentFactory contentFactory, AirtableService airtableService) {
        this.contentService = contentService;
        this.contentFactory = contentFactory;
        this.airtableService = airtableService;
    }

    @GetMapping("/reloadAll")
    public String reloadContentAll() throws JSONException, IOException {
        String[] tabNames = airtableService.getAirtableTabNames();
        for (String name : tabNames) {
            System.out.println("Creating " + name + " contents");
            contentFactory.createContent(name);
        }
        return "Finished !";
    }

    @GetMapping("/getJsonHashFromAirtableID")
    public String getJsonHashFromAirtableID(@RequestParam("airtableID") String airtableID) throws JSONException, IOException {
        Optional<Content> contentFound = contentService.getContentByAirtableId(airtableID);
        Content contentToReturn = new Content();
        if(contentFound.isPresent()){
            contentToReturn = contentFound.get();
            return contentToReturn.getJsonHash();
        }else{
            return null;
        }
    }
    @GetMapping("/getBinaryHashesFromAirtableID")
    public List<String> getBinaryHashesFromAirtableID(@RequestParam("airtableID") String airtableID) throws JSONException, IOException {

        Optional<Content> contentFound = contentService.getContentByAirtableId(airtableID);
        Content contentToReturn = new Content();
        if(contentFound.isPresent()){
            contentToReturn = contentFound.get();
            System.out.println("Found");
            return contentToReturn.getBinaryHashes();
        }else{
            return null;
        }

    }

    @GetMapping("/test")
    public void test(@RequestParam("accessToken") String accessToken) throws AccessDeniedException {
        String expectedAccessToken = DotenvConfig.get("ACCESS_TOKEN");
        if (accessToken.equals(expectedAccessToken)) {
            String mySecretKey = DotenvConfig.get("MY_SECRET_KEY");
            System.out.println(mySecretKey);
            // Your code logic goes here
            //for each table in the airtable, get the elements and store it into the database
        } else {
            throw new AccessDeniedException("Invalid access token");
        }
    }
}
