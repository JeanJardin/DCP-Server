package com.example.demo.Controller;

import com.example.demo.controllers.ContentController;
import com.example.demo.model.ContentFactory;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

    @SpringBootTest
    public class ContentControllerTest {

        @Autowired
        private ContentController contentController;

        @MockBean
        private ContentService contentService;

        @MockBean
        private ContentFactory contentFactory;

        @MockBean
        private AirtableService airtableService;

        @Before
        public void setUp() {
            contentController = new ContentController(contentService, contentFactory, airtableService);
        }
    /*
        @Test
        public void testReloadContentAll() throws Exception {
            // Given
            String[] tabNames = new String[]{"tab1", "tab2", "tab3"};
            given(airtableService.getAirtableTabNames()).willReturn(tabNames);

            // When
            String result = contentController.reloadContentAll();

            // Then
            verify(airtableService).getAirtableTabNames();
            verify(contentFactory, times(3)).createContent(anyString());
            assertThat(result).isEqualTo("Finished !");

        }



        @Test
        public void testGetBinaryHashesFromAirtableID() throws JSONException {
            // Given
            String airtableID = "rec123456789";
            List<String> binaryHashes = Arrays.asList("hash1", "hash2", "hash3");
            Content content = new Content();
            content.setBinaryHashes(binaryHashes);
            Optional<Content> contentFound = Optional.of(content);
            given(contentService.getContentByAirtableId(airtableID)).willReturn(contentFound);

            // When
            ResponseEntity<List<String>> responseEntity = contentController.getBinaryHashesFromAirtableID(airtableID);

            // Then
            assertEquals(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertEquals(responseEntity.getBody()).isEqualTo(binaryHashes);
        }


        @Test
        public void testGetJsonHashFromAirtableID() throws Exception {
            // Given
            String airtableID = "rec123456789";
            String expectedJsonHash = "hash123";

            Content content = new Content();
            content.setAirtableID(airtableID);
            content.setJsonHash(expectedJsonHash);

            given(contentService.getContentByAirtableId(airtableID)).willReturn(Optional.of(content));

            // When
            ResponseEntity<String> response = contentController.getJsonHashFromAirtableID(airtableID);
            assertEquals(response.getStatusCode(),HttpStatus.OK);
            // Then
            //assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            // assertThat(response.getBody()).isEqualTo(expectedJsonHash);

        }
*/


    }




