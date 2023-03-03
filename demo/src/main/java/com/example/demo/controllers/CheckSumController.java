package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;

public class CheckSumController implements IChecksumController{
    @Override
    public ResponseEntity<Boolean> checkContentIntegrity(String contentID) {
        return null;
    }
}
