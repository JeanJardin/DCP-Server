package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class ContentServiceTest {
    @Mock
    private ContentRepository contentRepository;
    private AutoCloseable autoCloseable;
    private ContentService contentService;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        contentService = new ContentService(contentRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

/*    @Test
    void addContent() {
        Content content = new Content();
        content.setContentJson(null);
        content.setBinaryHash("hash");
        content.setAirtableID("airtableID");
        content.setJsonHash("jsonHash");
        contentService.addContent(content);
        verify(contentRepository).save(content);
    }*/

    @Test
    void getContentByAirtableId() {
        String id = "WingardiumLeviosa";
        contentService.getContentByAirtableId(id);
        verify(contentRepository).findByAirtableID(id);
    }
}