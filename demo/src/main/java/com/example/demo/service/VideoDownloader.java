package com.example.demo.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Future;

/**
 * Utility class to download binary content from an url
 * @author Nathan Gaillard: nathan.gaillard@students.hevs.ch
 */
@Service
public class VideoDownloader implements IVideoDownloader {

    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a video from a url
     *
     * @param url the url in string format
     * @return a byte array containing the video raw data
     */
    @Async
    @Override
    public Future<byte[]> downloadVideo(String url) {
        URL videoUrl;
        try {
            videoUrl = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = videoUrl.openStream()) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
        return new AsyncResult<>(outputStream.toByteArray());
    }
}
