package com.example.demo.Controller;

import com.example.demo.controllers.ContentController;
import com.example.demo.model.ContentFactory;
import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        @Test
        public void reloadContentAllTest() throws Exception {
            String[] tabNames = new String[]{"tab1", "tab2", "tab3"};
            given(airtableService.getAirtableTabNames()).willReturn(tabNames);
            String result = contentController.reloadContentAll();
            verify(airtableService).getAirtableTabNames();
            verify(contentFactory, times(3)).createContent(anyString());
            assertTrue(result.equals("Finished !"));
        }

    }




