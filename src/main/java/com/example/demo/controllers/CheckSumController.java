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

    /**
     * Instance the ScheduledExecutorService for scheduling tasks
     * To run periodically of after a specified delay. This field can be accessed from anywhere
     */
    public static ScheduledExecutorService schedule;


    /**
     * Starts a periodic check of content integrity with the specified interval in minutes.
     * @param intervalMinutes the interval in minutes at which the check should be performed
     * @throws NullPointerException if the method checkContentIntegrity is not implemented or returns null
     */
    public static void startPeriodicCheck(int intervalMinutes) {
        ScheduledExecutorService schedule;
        schedule = Executors.newSingleThreadScheduledExecutor();

        schedule.scheduleAtFixedRate(() -> {
            // Appeler la méthode checkContentIntegrity ici
            System.out.println("Periodic check: "+ LocalDateTime.now());
            //checkContentIntegrity(contentID);
        }, 0, intervalMinutes, TimeUnit.SECONDS);
    }


    /**
     * Stops the periodic check of content integrity that was started using the startPeriodicCheck method.
     * This method will shutdown the ScheduledExecutorService instance that was created for scheduling
     * the periodic check.
     */
    public static void stopPeriodicCheck() {
        schedule.shutdown();
    }
}
