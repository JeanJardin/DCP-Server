package com.example.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public class CheckSumController implements IChecksumController{
    @Override
    public ResponseEntity<Boolean> checkContentIntegrity(String contentID) {
        return null;
    }
}
