package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;


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
}