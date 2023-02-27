package com.example.demo.controllers;

import com.example.demo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;


@RestController
@RequestMapping(path = "api")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/videos")
    public ResponseEntity<String> handleVideoUpload(@RequestParam("file") MultipartFile file) throws Exception {

        // Calculate hash value for uploaded video file
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(file.getBytes());
        byte[] hash = md.digest();
        String hashString = videoService.bytesToHex(hash);

        // Return hash value as response to client
        return ResponseEntity.ok(hashString);
    }
    @GetMapping("/hello")
    public String hello() throws Exception {

        return "Hello !";
    }

    // Helper method to convert byte array to hex string


}

