package com.example.demo.controllers;

import com.example.demo.model.ContentFactory;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.envUtils.DotenvConfig;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

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
     * Retrieves all contents from the repository and returns them as a list in the response body.
     *
     * @return a ResponseEntity<List<Content>> containing all contents
     */
/*    @GetMapping("/contentAll")
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(this.contentService.getContentRepository().findAll());
    }*/

    /**
     * Creates a new content of type "Videos" using the content factory.
     *
     * @return a message indicating the completion of the creation process
     */

    @GetMapping("/reloadAll")
    public String reloadContentAll() throws JSONException, IOException {
        //delete all data
        //contentService.deleteAllContent();
        //get all sections name from the airtable
        String [] tabNames = airtableService.getAirtableTabNames();

        for (String name : tabNames) {
            System.out.println("Creating " + name + " contents");
            contentFactory.createContent(name);
        }

        return "Finished !";
    }

    /**
     * This method is used to download all the content from Airtable for each table and store it into the database.
     * @param accessToken the access token used for authentication
     * @throws AccessDeniedException if the provided access token is invalid
     */
    @GetMapping("/downloadAllContent")
    public void test(@RequestParam("accessToken") String accessToken) throws AccessDeniedException {
        String expectedAccessToken = DotenvConfig.get("ACCESS_TOKEN");
        if (accessToken.equals(expectedAccessToken)) {
            String mySecretKey = DotenvConfig.get("MY_SECRET_KEY");
            System.out.println(mySecretKey);
        } else {
            throw new AccessDeniedException("Invalid access token");
        }
    }


}
