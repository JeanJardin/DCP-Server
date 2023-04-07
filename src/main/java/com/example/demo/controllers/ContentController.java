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


    /**
     * Reloads all the content from Airtable by retrieving the list of airtable tab names and creating content for each tab using the contentFactory
     *
     * @return  a String indicating the process was finished successfully.
     * @throws JSONException if there's an error parsing JSON.
     * @throws IOException if there's an error with I/O operations.
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
     *This methode handles GET request to retrive the binary hasehs associated with a specific airtable ID
     *
     * @param airtableID the airtable ID for which to retrieve the binary hashes
     * @return a ResponseEntitxy containing a list of binary hashes, with HTTP status 200 if successful or HTTP status 404 if the airtable ID is not found
     * @throws JSONException if there is an error parsing the JSON response from Content Service
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
     * Return the JSon Hash corresponding to the specified Airtable ID
     *
     * @param airtableID the Airtable ID of the content to retrieve the JSON hash for
     * @return a ResponseEntity containing the JSON hash as a String if the content is found
     * @throws JSONException if there is an error parsing the JSON hash
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
