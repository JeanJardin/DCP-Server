package com.example.demo.service;

import com.example.demo.model.Content;

import java.util.Optional;

public interface IContentService {
     void addContent(Content content);
     Optional<Content> getContentByAirtableId(String id);

    void deleteAllContent();
}
