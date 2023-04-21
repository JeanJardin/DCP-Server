package com.example.demo.service;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


class VideoDownloaderTest {


    /**
     * Test the download video method
     * @throws Exception
     */
    @Test
    public void testDowloadVideo() throws Exception{
        VideoDownloader videoDownloader = new VideoDownloader();
        String urlTest = "https://freetestdata.com/wp-content/uploads/2022/02/Free_Test_Data_1MB_MP4.mp4";
        byte[] expectedBytes = ConvertToByteArray("src/test/java/com/example/demo/testData/Free_Test_Data_1MB_MP4.mp4");
        Future<byte[]> result = videoDownloader.downloadVideo(urlTest);
        byte[] actualBytes = result.get();
        assertArrayEquals(expectedBytes, actualBytes);
    }






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