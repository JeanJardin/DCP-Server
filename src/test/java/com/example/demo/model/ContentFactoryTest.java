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

import static com.example.demo.controllers.CheckSumController.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
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
        contentFactory = new ContentFactory(contentService, airtableService, hashService);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

 /*   @Test
    void createContentFromJsonVideoField()  {
        // Mock the JSONObject with required fields
        JSONObject jsonObject = mock(JSONObject.class);
        JSONObject fieldsObject = mock(JSONObject.class);


        try {
            when(jsonObject.getJSONObject("fields")).thenReturn(fieldsObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        when(jsonObject.optString("id")).thenReturn("airtableId");
        when(fieldsObject.has("VideoURL")).thenReturn(true);
        when(fieldsObject.optString("VideoURL")).thenReturn("https://example.com/video.mp4");

        // Mock the HashService
        HashService hashService = mock(HashService.class);
        when(hashService.hashBinaryContent(anyString())).thenReturn("binaryHash");
        when(hashService.hashContent(jsonObject)).thenReturn("jsonHash");

        // Create the ContentFactory and invoke the method
        ContentFactory contentFactory = new ContentFactory(mock(ContentService.class), mock(AirtableService.class), hashService);
        Content content = contentFactory.createContentFromJson(jsonObject);

        // Verify the content object is correctly initialized
        assertEquals("binaryHash", content.getBinaryHash());
        assertEquals("jsonHash", content.getJsonHash());
        assertEquals("airtableId", content.getAirtableID());
    }*/
  /*  @Test
    void createContentFromJsonFileField()  {
        // Mock the JSONObject with required fields
        JSONObject jsonObject = mock(JSONObject.class);
        JSONObject fieldsObject = mock(JSONObject.class);


        try {
            when(jsonObject.getJSONObject("fields")).thenReturn(fieldsObject);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        when(jsonObject.optString("id")).thenReturn("airtableId");
        when(fieldsObject.has("File")).thenReturn(true);
        when(fieldsObject.optString("VideoURL")).thenReturn("http://example.com/video.mp4");

        // Mock the HashService
        HashService hashService = mock(HashService.class);
        when(hashService.hashBinaryContent(anyString())).thenReturn("binaryHash");
        when(hashService.hashContent(jsonObject)).thenReturn("jsonHash");

        // Create the ContentFactory and invoke the method
        ContentFactory contentFactory = new ContentFactory(mock(ContentService.class), mock(AirtableService.class), hashService);
        Content content = contentFactory.createContentFromJson(jsonObject);

        // Verify the content object is correctly initialized
        assertNull(content.getBinaryHash());
        assertEquals("jsonHash", content.getJsonHash());
        assertEquals("airtableId", content.getAirtableID());
    }*/



    @Test
    public void getFieldVideoURL() {
        JSONObject fieldsObject1 = mock(JSONObject.class);
        when(fieldsObject1.has("VideoURL")).thenReturn(true);

        // Test the getFieldType() method with the mock JSONObject
        String fieldType1 = contentFactory.getFieldType(fieldsObject1);

        // Assert the result
        assertEquals("VideoURL", fieldType1);
    }

    @Test
    public void getFieldFile() {
        // Create a mock JSONObject with a "File" field
        JSONObject fieldsObject2 = mock(JSONObject.class);
        when(fieldsObject2.has("File")).thenReturn(true);

        // Test the getFieldType() method with the mock JSONObject
        String fieldType2 = contentFactory.getFieldType(fieldsObject2);

        // Assert the result
        assertEquals("File", fieldType2);
    }

    @Test
    public void getFielNoField() {
        // Create a mock JSONObject with no video fields
        JSONObject fieldsObject3 = mock(JSONObject.class);
        when(fieldsObject3.has("VideoURL")).thenReturn(false);
        when(fieldsObject3.has("File")).thenReturn(false);

        // Test the getFieldType() method with the mock JSONObject
        String fieldType3 = contentFactory.getFieldType(fieldsObject3);

        // Assert the result
        assertEquals("NoField", fieldType3);
    }


    /*@Test
    void isContentAlreadyInDatabaseUsingBinaryHash() {
        // Create a mock Content object
        Content content = mock(Content.class);
        when(content.getBinaryHash()).thenReturn("binaryHash");
        when(content.getJsonHash()).thenReturn("jsonHash");

        // Create a mock ContentRepository object
        ContentRepository contentRepository = mock(ContentRepository.class);

        // Mock the behavior of the ContentService
        ContentService contentService = mock(ContentService.class);
        when(contentService.getContentRepository()).thenReturn(contentRepository);
        when(contentRepository.existsByBinaryHash("binaryHash")).thenReturn(true);
        when(contentRepository.existsByJsonHash("jsonHash")).thenReturn(false);

        // Create a ContentFactory object
        ContentFactory contentFactory = new ContentFactory(contentService, airtableService, hashService);

        // Test the isContentAlreadyInDatabase() method
        boolean result = contentFactory.isContentAlreadyInDatabase(content);

        // Assert the result
        assertTrue(result);
    }*/
/*

    @Test
    void isContentAlreadyInDatabaseUsingJsonHash() {
        // Create a mock Content object
        Content content = mock(Content.class);
        when(content.getBinaryHash()).thenReturn("binaryHash");
        when(content.getJsonHash()).thenReturn("jsonHash");

        // Create a mock ContentRepository object
        ContentRepository contentRepository = mock(ContentRepository.class);

        // Mock the behavior of the ContentService
        ContentService contentService = mock(ContentService.class);
        when(contentService.getContentRepository()).thenReturn(contentRepository);
        when(contentRepository.existsByBinaryHash("binaryHash")).thenReturn(false);
        when(contentRepository.existsByJsonHash("jsonHash")).thenReturn(true);

        // Create a ContentFactory object
        ContentFactory contentFactory = new ContentFactory(contentService, airtableService, hashService);

        // Test the isContentAlreadyInDatabase() method
        boolean result = contentFactory.isContentAlreadyInDatabase(content);

        // Assert the result
        assertTrue(result);
    }
*/


    //Periodic check test


    //test the start of the check
/*    @Test
    public void StartPeriodicCheckTest(){
        int intervalMinutes=5;
        startPeriodicCheck(intervalMinutes);
        assertNotNull(schedule);
    }*/

    //test th Stop periodicCheck
   /* @Test
    public void StopPeriodicCheckTest(){
        int intervalMinutes = 5;
        startPeriodicCheck(intervalMinutes);
        stopPeriodicCheck();
        assertTrue(schedule.isShutdown());
    }*/
}