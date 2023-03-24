package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * CheckSumController class that implements the IChecksumController interface.
 * It provides functionality for checking the integrity of content using a checksum.
 */
public class CheckSumController implements IChecksumController{
    /**
     * {@inheritDoc}
     * This method checks the integrity of the content using a checksum.
     * @param contentID the ID of the content to check
     * @return a ResponseEntity<Boolean> indicating whether the content is intact or not
     */
    @Override
    public ResponseEntity<Boolean> checkContentIntegrity(String contentID) {


        return null;
    }


    //##################################################################################################################
    //Periodic check
    public static ScheduledExecutorService schedule;

    public static void main(String[] args) {
        startPeriodicCheck(1);
    }
    public static void startPeriodicCheck(int intervalMinutes) {
        schedule = Executors.newSingleThreadScheduledExecutor();

        schedule.scheduleAtFixedRate(() -> {
            // Appeler la méthode checkContentIntegrity ici
            System.out.println("Periodic check: "+ LocalDateTime.now());
            //checkContentIntegrity(contentID);
        }, 0, intervalMinutes, TimeUnit.MINUTES);
    }

    public void stopCheckingContentIntegrity() {
        schedule.shutdown();
    }
}
