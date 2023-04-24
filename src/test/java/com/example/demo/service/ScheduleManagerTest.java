package com.example.demo.service;

import com.example.demo.controllers.ContentController;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.times;

public class ScheduleManagerTest {
    @InjectMocks
    private SchedulerManager myClass;
    @Mock
    private ContentController contentController;
    /**
     * Test that the reload works and call the methode correctly
     * @throws JSONException
     * @throws IOException
     */
    @Test
    public void reloadTest() throws JSONException, IOException {
        MockitoAnnotations.openMocks(this);
        myClass.reload();
        Mockito.verify(contentController, times(1)).reloadContentAll(); // vérification que la méthode a bien été appelée une fois
    }
}
