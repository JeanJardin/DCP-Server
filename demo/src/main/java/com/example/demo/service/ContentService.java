package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public Content getContentById(String id) {
      Content foundContent =  contentRepository.findById(id).get();
        return foundContent;
    }

    public void saveContent(Content content) {
        contentRepository.save(content);
    }
    //TODO getContentByAirtableId
    public void getContentByAirtableId(int id) {
       // contentRepository.save(content);
    }

    public ContentRepository getContentRepository() {
        return contentRepository;
    }

    public void setContentRepository(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }
}
