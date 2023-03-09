package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    public ContentService() {
    }

    public Content getContentById(String id) {
      Content foundContent =  contentRepository.findById(id).get();
        return foundContent;
    }

    public void addContent(Content content) {
        contentRepository.save(content);
    }
    public  void addListOfContentsToDB(List<Content> contents) {
        for (Content content: contents) {
           contentRepository.save(content);
        }
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
