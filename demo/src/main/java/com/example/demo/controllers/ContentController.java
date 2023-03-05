package com.example.demo.controllers;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContentController {
    private ContentRepository productRepository;
    public ContentController(ContentRepository productRepository) {
        this.productRepository = productRepository;
    }
    @GetMapping("/content")
    public ResponseEntity<List<Content>> getAllContents()
    {
        return ResponseEntity.ok(this.productRepository.findAll());
    }
}
