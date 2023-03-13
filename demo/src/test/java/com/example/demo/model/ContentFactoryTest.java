package com.example.demo.model;

import com.example.demo.repository.ContentRepository;
import com.example.demo.service.ContentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class ContentFactoryTest {

    @Mock
    private ContentRepository contentRepository;
    private AutoCloseable autoCloseable;
    private ContentFactory contentFactory;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createContent() {
        //when

    }

    @Test
    void createContentFromJson() {
    }

    @Test
    void getFieldType() {
    }

    @Test
    void isContentAlreadyInDatabase() {
    }
}