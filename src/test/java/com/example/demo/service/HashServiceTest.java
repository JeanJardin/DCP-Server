package com.example.demo.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;

import static com.example.demo.service.HashService.HASH_ALGORITHM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HashServiceTest {
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);

    }
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }


    /**
     * Test of the hashing method for the content
     */
    @Test
    void hashContentTest() {
        HashService hashService = new HashService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "John");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("age", 30);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            jsonObject.put("married", true);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String hashedJson = hashService.hashContent(jsonObject);
        assertEquals("aadf601809e5cbfad5ae4fc67b270137", hashedJson);
    }

    /**
     * Test the hash method for the binary of the content
     */
    @Test
    void hashBinaryContentTest() {
        VideoDownloader videoDownloader = mock(VideoDownloader.class);
        HashService hashService = new HashService(videoDownloader);
        byte[] data = {0x0A, 0x1B, 0x2C, 0x3D, 0x4E};
        // create the completed future with the mock data
        CompletableFuture<byte[]> future = CompletableFuture.completedFuture(data);

        // when downloadVideo is called, return the completed future
        when(videoDownloader.downloadVideo("VideoUrl")).thenReturn(future);
        String hash = hashService.hashBinaryContent("VideoUrl");
        assertEquals("a2e8fd2370c614b8ae0316cac2447ad2", hash);
    }

    /**
     * Compare the signature of the hashes between mongodDB and the Tablet
     */
    @Test
    void compareHashSignatureTest(){
        String hashMongoDB = "hashMongoDB";
        String hashTablet = "hashTablet";
        HashService hashService = new HashService();
        assertFalse((hashService.compareHashSignature(hashMongoDB, hashTablet)));
    }


    @Test
    void hashDataTest() throws NoSuchAlgorithmException {
        byte[] testData = "Hello world".getBytes();
        byte[] expectedHash = MessageDigest.getInstance(HASH_ALGORITHM).digest(testData);
        byte[] actualHash = HashService.hashData(testData);
        assertArrayEquals(expectedHash, actualHash);
    }


    @Test 
    void byteArrayToHexTest(){
        byte[] testData = {0x01, 0x02, 0x03};
        String actualHex = HashService.byteArrayToHex(testData);
        assertEquals("010203", actualHex);
    }
}