package com.example.demo.test;

import com.example.demo.test.VideoService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
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
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(file.getBytes());
        byte[] hash = md.digest();
        String hashString = VideoService.bytesToHex(hash);

        // Return hash value as response to client
        return ResponseEntity.ok(hashString);
    }
    @GetMapping("/hello")
    public String hello() throws Exception {

        return "Yes my man";
    }

    // Helper method to convert byte array to hex string


}

