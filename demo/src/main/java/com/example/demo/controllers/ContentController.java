package com.example.demo.controllers;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContentController {
    private ContentRepository contentRepository;
    public ContentController(ContentRepository productRepository) {
        this.contentRepository = productRepository;
    }
    @GetMapping("/contentAll")
    public ResponseEntity<List<Content>> getAllContents()
    {
        return ResponseEntity.ok(this.contentRepository.findAll());
    }
    @PostMapping("/addContent")
    public String  addContent(@RequestBody Content content)
    {
        contentRepository.save(content);
        return "added content to mongoDB";
    }
}
