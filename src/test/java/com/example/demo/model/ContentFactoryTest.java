package com.example.demo.model;

import com.example.demo.repository.ContentRepository;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.demo.service.HashService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

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



    @Test
    void testCreateContentFromJson() throws JSONException {
        // Given
        String airtableID = "1";
        String videoURL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
        String fileURL = "https://www.example.com/file.pdf";
        List<String> listOfUrls = new ArrayList<>();
        listOfUrls.add(videoURL);
        listOfUrls.add(fileURL);
        String json = "{\"id\": \"" + airtableID + "\", \"VideoURL\": \"" + videoURL + "\", \"File\": \"" + fileURL + "\"}";
        JSONObject jsonObject = new JSONObject(json);
        Content content = new Content();
        content.setAirtableID(airtableID);
        content.setJsonHash("hash");
        content.addBinaryHashToList("hashVideoURL");
        content.addBinaryHashToList("hashFile");

        Mockito.when(airtableService.findHttps(jsonObject)).thenReturn(listOfUrls);
        Mockito.when(hashService.hashContent(jsonObject)).thenReturn("hash");
        Mockito.when(hashService.hashBinaryContent(videoURL)).thenReturn("hashVideoURL");
        Mockito.when(hashService.hashBinaryContent(fileURL)).thenReturn("hashFile");

        // When
        Content result = contentFactory.createContentFromJson(jsonObject);

        // Then
        Assertions.assertEquals(content, result);
    }


    /*
    @Test
    void testReformattedUrl() {
        // Given
        String url = "[http:\\\\example.com\\test]";
        String expectedResult = "http://example.com/test";

        // When
        String result = contentFactory.reformattedUrl(url);

        // Then
        Assertions.assertEquals(expectedResult, result);
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

    /*
    @Test
    public void testIsContentAlreadyInDatabaseWithAirtableId() {
        // Given
        String airtableId = "123456";
        String jsonHash = "abcde";
        Content content = new Content();
        content.setAirtableID(airtableId);
        content.setJsonHash(jsonHash);
        ContentRepository contentRepository = mock(ContentRepository.class);
        ContentService contentService = new ContentService(contentRepository);
        given(contentRepository.existsByAirtableID(airtableId)).willReturn(true);
        ContentService contentService = new ContentService(contentService);

        // When
        boolean result = contentService.isContentAlreadyInDatabaseWithAirtableId(content);

        // Then
        assertTrue(result);
    }

    */


}