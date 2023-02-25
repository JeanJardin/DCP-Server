package com.example.demo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;


@RestController
public class VideoController {

    @PostMapping("/videos")
    public ResponseEntity<String> handleVideoUpload(@RequestParam("file") MultipartFile file) throws Exception {

        // Calculate hash value for uploaded video file
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(file.getBytes());
        byte[] hash = md.digest();
        String hashString = bytesToHex(hash);

        // Return hash value as response to client
        return ResponseEntity.ok(hashString);
    }

    // Helper method to convert byte array to hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}

