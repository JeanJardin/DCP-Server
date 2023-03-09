package com.example.demo.controllers;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import com.example.demo.service.ContentService;
import com.example.envUtils.DotenvConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/contentAll")
    public ResponseEntity<List<Content>> getAllContents() {
        return ResponseEntity.ok(this.contentService.getContentRepository().findAll());
    }

    @GetMapping("/addContent")
    public String addContent() {
        Content content = new Content();
        content.setContentHash("ADAKWDAKWD");
        content.setContentJson(null);
        content.setBinaryContent(new byte[]{0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64});
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
