package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {
    //Variables

    @Autowired
    private ContentRepository contentRepository;

    public Content getContentById(String id) {
        contentRepository.findById(id).get();
        return null;
    }


    public void saveContent(Content content) {
        contentRepository.save(content);
    }
}
