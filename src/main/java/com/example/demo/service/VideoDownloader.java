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
 * Service class that implements the {@link IVideoDownloader} interface to download video from URL asynchronously.
 */
@Service
public class VideoDownloader implements IVideoDownloader {
    /**
     * The size of the buffer used for downloading the video.
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Downloads a video from the specified URL and returns it as a Future object.
     *
     * @param url the URL of the video to download
     * @return a Future object containing the downloaded video data
     * @throws RuntimeException if an error occurs while downloading the video
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new AsyncResult<>(outputStream.toByteArray());
    }
}
