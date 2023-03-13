package com.example.demo.service;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;

public interface IVideoDownloader {
    @Async
    Future<byte[]> downloadVideo(String url);
}
