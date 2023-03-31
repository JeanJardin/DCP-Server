package com.example.demo.controllers;

import com.example.demo.model.Content;
import com.example.demo.model.ContentFactory;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping(path = "/getBinaryHashesFromAirtableID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getBinaryHashesFromAirtableID(@RequestParam("airtableID") String airtableID) throws JSONException {
        Optional<Content> contentFound = contentService.getContentByAirtableId(airtableID);
        System.out.println("Called ! ");
        if (contentFound.isPresent()) {
            Content content = contentFound.get();
            return ResponseEntity.status(200).body(content.getBinaryHashes());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/getJsonHashFromAirtableID", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getJsonHashFromAirtableID(@RequestParam("airtableID") String airtableID) throws JSONException {
        Optional<Content> contentFound = contentService.getContentByAirtableId(airtableID);
        if (contentFound.isPresent()) {
            Content content = contentFound.get();
            return ResponseEntity.ok(content.getJsonHash());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
