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
    @Test
    public void reload_shouldCallReloadContentAllMethodOnce() throws JSONException, IOException {
        // Arrange
        MockitoAnnotations.openMocks(this); // initialisation des mocks
        // Act
        myClass.reload();
        // Assert
        Mockito.verify(contentController, times(1)).reloadContentAll(); // vérification que la méthode a bien été appelée une fois
    }
}
