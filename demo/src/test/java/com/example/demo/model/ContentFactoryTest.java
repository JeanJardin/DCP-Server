package com.example.demo.model;

import com.example.demo.repository.ContentRepository;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.demo.service.HashService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContentFactoryTest {

    @Mock
    private ContentRepository contentRepository;
    private AutoCloseable autoCloseable;
    private ContentService contentService;
    private HashService hashService;
    private ContentFactory contentFactory;
    private AirtableService airtableService;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        contentService = new ContentService(contentRepository);
        hashService = new HashService();
        airtableService = new AirtableService();
        // we implement them because we are not using them with @AutoWired
        contentFactory = new ContentFactory(contentService,airtableService,hashService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void createContent() {

    }

    @Test
    void createContentFromJson() {
    }

    @Test
    public void testGetFieldVideoURL(){
        JSONObject fieldsObject1 = mock(JSONObject.class);
        when(fieldsObject1.has("VideoURL")).thenReturn(true);

        // Test the getFieldType() method with the mock JSONObject
        String fieldType1 = contentFactory.getFieldType(fieldsObject1);

        // Assert the result
        assertEquals("VideoURL", fieldType1);
    }
    @Test
    public void testGetFieldFile(){
        // Create a mock JSONObject with a "File" field
        JSONObject fieldsObject2 = mock(JSONObject.class);
        when(fieldsObject2.has("File")).thenReturn(true);

        // Test the getFieldType() method with the mock JSONObject
        String fieldType2 = contentFactory.getFieldType(fieldsObject2);

        // Assert the result
        assertEquals("File", fieldType2);
    }
    @Test
    public void testGetFielNoField(){
        // Create a mock JSONObject with no video fields
        JSONObject fieldsObject3 = mock(JSONObject.class);
        when(fieldsObject3.has("VideoURL")).thenReturn(false);
        when(fieldsObject3.has("File")).thenReturn(false);

        // Test the getFieldType() method with the mock JSONObject
        String fieldType3 = contentFactory.getFieldType(fieldsObject3);

        // Assert the result
        assertEquals("NoField", fieldType3);
    }


    @Test
    void isContentAlreadyInDatabase() {

    }
}