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
import java.util.List;

@RestController
public class ContentController {

    private final ContentFactory contentFactory;
    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService,ContentFactory contentFactory) {
        this.contentService = contentService;
        this.contentFactory = contentFactory;
    }

    @GetMapping("/contentAll")
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(this.contentService.getContentRepository().findAll());
    }

    @GetMapping("/createContent")
    public String createContent() {
        try {
            contentFactory.createContent("Testimonials");
        } catch (JSONException e) {
            return ("Problem occured "+e.getMessage().toString());
        } catch (IOException e) {
            return ("Problem occured "+e.getMessage().toString());
        }
        return "Finished !";
    }

    @GetMapping("/addContent")
    public String addContent() {
        Content content = new Content();
        content.setContentHash("ADAKWDAKWD");
        content.setContentJson(null);
        try {
            contentService.getContentRepository().save(content);
        } catch (Exception e) {
            System.out.println(e.getMessage().toString());
        }
        return "added !";
    }

    @GetMapping("/test")
    public void test() {
        String mySecretKey = DotenvConfig.get("MY_SECRET_KEY");
        System.out.println(mySecretKey);
    }

}
