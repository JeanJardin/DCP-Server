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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

        when(airtableService.findHttps(jsonObject)).thenReturn(listOfUrls);
        when(hashService.hashContent(jsonObject)).thenReturn("hash");
        when(hashService.hashBinaryContent(videoURL)).thenReturn("hashVideoURL");
        when(hashService.hashBinaryContent(fileURL)).thenReturn("hashFile");

        Content result = contentFactory.createContentFromJson(jsonObject);

        assertEquals(content, result);
    }



    @Test
    void testReformattedUrl() {
        String url = "[http:\\\\example.com\\test]";
        String expectedResult = "http://example.com/test";
        String result = contentFactory.reformattedUrl(url);
        assertEquals(expectedResult, result);
    }


    @Test
    public void getFieldFileTest() {
        // Create a mock JSONObject with a "File" field
        JSONObject fieldsObject2 = mock(JSONObject.class);
        when(fieldsObject2.has("File")).thenReturn(true);

        // Test the getFieldType() method with the mock JSONObject
        String fieldType2 = contentFactory.getFieldType(fieldsObject2);

        // Assert the result
        assertEquals("File", fieldType2);
    }

    @Test
    public void getFielNoFieldTest() {
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
    public void testIsContentAlreadyInDatabaseWithAirtableId() {
        // Given
        Content content = new Content();
        content.setAirtableID("rec123");
        given(contentService.getContentRepository().existsByAirtableID("rec123")).willReturn(true);

        // When
        boolean result = contentFactory.isContentAlreadyInDatabaseWithAirtableId(content);

        // Then
        assertTrue(result);
        verify(contentService.getContentRepository(), times(1)).existsByAirtableID("rec123");
    }
}