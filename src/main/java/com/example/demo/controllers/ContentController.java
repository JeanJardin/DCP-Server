package com.example.demo.controllers;

import com.example.demo.model.Content;
import com.example.demo.model.ContentFactory;
import com.example.demo.repository.ContentRepository;
import com.example.demo.service.ContentService;
import com.example.demo.service.HashService;
import com.example.envUtils.DotenvConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * The ContentController class provides REST endpoints for managing contents.
 * It is responsible for creating, retrieving, and adding content to the system.
 */
@RestController
public class ContentController {

    private final ContentFactory contentFactory;
    private final ContentService contentService;

    /**
     * Constructs a new instance of the ContentController class with the specified parameters.
     *
     * @param contentService the service that manages content
     * @param contentFactory the factory used for creating content
     */
    @Autowired
    public ContentController(ContentService contentService, ContentFactory contentFactory) {
        this.contentService = contentService;
        this.contentFactory = contentFactory;
    }

    /**
     * Retrieves all contents from the repository and returns them as a list in the response body.
     *
     * @return a ResponseEntity<List<Content>> containing all contents
     */
    @GetMapping("/contentAll")
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(this.contentService.getContentRepository().findAll());
    }

    /**
     * Creates a new content of type "Videos" using the content factory.
     *
     * @return a message indicating the completion of the creation process
     */

    @GetMapping("/reloadAll")
    public String reloadContentAll() {

        //get all sections name from the airtable


        contentFactory.createContent("Videos");
        return "Finished !";
    }
    @GetMapping("/addContent")
    public String addContent() {
        Content content = new Content();
        content.setJsonHash("ADAKWDAKWD");
        content.setContentJson(null);
        try {
            contentService.getContentRepository().save(content);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
        }
        return "added !";
    }

    @GetMapping("/downloadAllContent")
    public void test(@RequestParam("accessToken") String accessToken) throws AccessDeniedException {
        String expectedAccessToken = DotenvConfig.get("ACCESS_TOKEN");
        if (accessToken.equals(expectedAccessToken)) {
            String mySecretKey = DotenvConfig.get("MY_SECRET_KEY");
            System.out.println(mySecretKey);
            // Your code logic goes here


        } else {
            throw new AccessDeniedException("Invalid access token");
        }
    }

    // input idairtable output concatenated hashes ADD ALSO ACCESS_TOKEN in parameters to allow only valid calls
    // input void output void => reload data from server => download everything again

}
