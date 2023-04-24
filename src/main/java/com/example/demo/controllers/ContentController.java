package com.example.demo.controllers;

import com.example.demo.model.Content;
import com.example.demo.model.ContentFactory;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * Constructor for the ContentController class.
     *
     * @param contentService  The ContentService instance to be used by the controller.
     * @param contentFactory  The ContentFactory instance to be used by the controller.
     * @param airtableService The AirtableService instance to be used by the controller.
     */
    @Autowired
    public ContentController(ContentService contentService, ContentFactory contentFactory, AirtableService airtableService) {
        this.contentService = contentService;
        this.contentFactory = contentFactory;
        this.airtableService = airtableService;
    }

    /**
     * Retrieves contents from Airtable and creates them in the system.
     *
     * @return A String indicating the operation is finished.
     * @throws JSONException if there is an error while creating the JSON object.
     * @throws IOException   if there is an error while reading data from Airtable.
     */
    @GetMapping("/reloadAll")
    public String reloadContentAll() throws JSONException, IOException {
        String[] tabNames = airtableService.getAirtableTabNames();
        for (String name : tabNames) {
            System.out.println("Creating " + name + " contents");
            contentFactory.createContent(name);
        }
        return "Finished !";
    }

    /**
     * Retrieves the binary hashes of a content associated with a given Airtable ID.
     *
     * @param airtableID The Airtable ID of the content.
     * @return A ResponseEntity containing the list of binary hashes of the content.
     * @throws JSONException if there is an error while creating the JSON object.
     */
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

    /**
     * Retrieves the JSON hash of a content associated with a given Airtable ID.
     *
     * @param airtableID The Airtable ID of the content.
     * @return A ResponseEntity containing the JSON hash of the content.
     * @throws JSONException if there is an error while creating the JSON object.
     */
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
