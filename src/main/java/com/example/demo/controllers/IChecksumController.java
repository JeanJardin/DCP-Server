package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;

public interface IChecksumController {

    //methods
    ResponseEntity<Boolean> checkContentIntegrity(String contentID);
}
