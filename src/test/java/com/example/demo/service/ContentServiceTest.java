package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
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

    @Test
    void addContentTest(){
        Content content = new Content();
        content.setJsonHash(null);
        content.setBinaryHashes(null);
        content.setAirtableID("airtableID");
        content.setJsonHash("jsonHash");
        contentService.addContent(content);
        verify(contentRepository).save(content);
    }


    @Test
    void getContentByAirtableIDTest(){
        String id = "WingardiumLeviosa";
        contentService.getContentByAirtableId(id);
        verify(contentRepository).findByAirtableID(id);
    }


    @Test
    void deleteAllContentTest(){
        contentService.deleteAllContent();
        verify(contentRepository, times(1)).deleteAll();
    }

    @Test
    void getContentRepositoryTest(){
        ContentRepository result = contentService.getContentRepository();
        assertNotNull(result);
        assertEquals(contentRepository.getClass(), result.getClass());

    }

    /*
    @Test
    void updateContentTest(){
        ContentRepository mockContentRepository = mock(ContentRepository.class);
        ContentService contentService = new ContentService(mockContentRepository);
        Content contentFromAirtable = new Content();
        contentFromAirtable.setAirtableID("123");
        contentFromAirtable.setJsonHash("hash");

        Optional<Content> mockContentDB = Optional.of(new Content());
        when(mockContentRepository.findByAirtableID("123")).thenReturn(mockContentDB);

        boolean result = contentService.updateContent(contentFromAirtable);
        assertTrue(result);
    }

*/

    @Test
    void startPeriodicCheckTest(){
        ContentService.startPeriodicCheck(1);
        try {
            Thread.sleep(3 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}