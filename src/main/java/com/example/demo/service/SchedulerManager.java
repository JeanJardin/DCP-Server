package com.example.demo.service;

import com.example.demo.controllers.ContentController;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 This component class is responsible for managing scheduled tasks. It provides a method to reload content in the
 application at regular intervals using Spring's @Scheduled annotation.
 */
@Component
public class SchedulerManager {
    /**

     Reloads the content in the application at regular intervals. The method is scheduled to run every 30 minutes
     using Spring's @Scheduled annotation.
     * @throws JSONException if there is an issue parsing JSON data
     * @throws IOException if there is an issue with reading from or writing to a file or stream
     */
    @Autowired
    private ContentController contentController;

    //TODO Initial delay is added to avoid interference during development
    // when production mode remove the initial delay. -A.B
    @Scheduled(fixedDelay = 1800000, initialDelay = 1800000) // 30 minutes
    public void reload() throws JSONException, IOException {
        contentController.reloadContentAll();
    }
}
