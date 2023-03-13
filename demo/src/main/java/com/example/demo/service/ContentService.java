package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public void addContent(Content content) {
        contentRepository.save(content);
    }


    public Content getContentByAirtableId(String id) {
       return contentRepository.findByAirtableID(id);
    }

    public ContentRepository getContentRepository() {
        return contentRepository;
    }

}
