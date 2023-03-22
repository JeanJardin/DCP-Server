package com.example.demo.service;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.URL;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;


class VideoDownloaderTest {

    @Test
    public void testDownloadImage() throws Exception {
        VideoDownloader videoDownloader  =new VideoDownloader();
        // Arrange
        String testUrl = "https://freetestdata.com/wp-content/uploads/2022/02/Free_Test_Data_117KB_JPG.jpg";
        byte[] expectedBytes = ConvertToByteArray("src/test/java/com/example/demo/testData/testimage.jpg");

        // Act
        Future<byte[]> result = videoDownloader.downloadVideo(testUrl);
        byte[] actualBytes = result.get();

        // Assert
        assertArrayEquals(expectedBytes, actualBytes);
    }

    public static byte[] ConvertToByteArray(String filePath) throws IOException {


        FileInputStream is = null;

        is = new FileInputStream(filePath);
        byte[] byteArr = IOUtils.toByteArray(is);


        return byteArr;
    }
}