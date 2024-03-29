package com.example.demo.service;

import com.example.demo.controllers.ContentController;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SchedulerManager {

    @Autowired
    private ContentController contentController;

    //TODO Initial delay is added to avoid interference during development
    // when production mode remove the initial delay. -A.B
    @Scheduled(fixedDelay = 1800000, initialDelay = 1800000) // 30 minutes
    public void reload() throws JSONException, IOException {
        contentController.reloadContentAll();
    }
}
