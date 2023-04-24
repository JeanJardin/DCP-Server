package com.example.demo.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
/**
 * This class implements the IHashService interface and is responsible for providing the necessary functionalities for
 * hashing the content data and binary data. It contains methods to hash the content data and binary data, and to compare
 * the hash signatures.
 */
@Service
public class HashService implements IHashService {

    /**
     * The hash algorithm in use
     */
    public final static String HASH_ALGORITHM = "MD5";
    private VideoDownloader videoDownloader;

    /**
     * Returns the hash value for the provided JSON object.
     * @param jsonObject the JSON object to hash
     * @return the hash value of the JSON object
     */
    @Override
    public String hashContent(JSONObject jsonObject) {
        byte[] data = jsonObject.toString().getBytes();

        return byteArrayToHex(hashData(data));
    }

    /**
     * Returns the hash value for the binary content located at the specified URL.
     * @param resourceUrl the URL of the binary content to hash
     * @return the hash value of the binary content
     */
    @Override
    public String hashBinaryContent(String resourceUrl) {
        if (videoDownloader == null) {
            videoDownloader = new VideoDownloader();
        }
        Future<byte[]> dataTemp = videoDownloader.downloadVideo(resourceUrl);
        while (dataTemp.isDone() == false) {
            System.out.println("Downloading..");
        }
        // Wait for the result of the future
        byte[] data;
        try {
            data = dataTemp.get();
            System.out.println("Downloading finished");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return byteArrayToHex(hashData(data));
    }
    /**
     * Compares the hash signatures of two content pieces.
     * @param hashMongoDB the hash signature stored in the MongoDB database
     * @param hashTablet the hash signature of the content on the tablet device
     * @return true if the hash signatures match, false otherwise
     */
    public boolean compareHashSignature(String hashMongoDB, String hashTablet) {
        return hashMongoDB.equals(hashTablet);
    }
    /**
     * Calculates the hash value of the provided data.
     * @param data the data to hash
     * @return the hash value of the data
     */
    static byte[] hashData(byte[] data) {
        byte[] res;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            res = digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return res;
    }
    /**
     * Converts a byte array to a hexadecimal string.
     * @param a the byte array to convert
     * @return the hexadecimal string representation of the byte array
     */
    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
    /**
     * Constructor for creating a new instance of HashService with a provided VideoDownloader object.
     * @param videoDownloader the VideoDownloader object to use for downloading videos
     */
    public HashService(VideoDownloader videoDownloader) {
        this.videoDownloader = videoDownloader;
    }
    /**
     * Default constructor for creating a new instance of HashService.
     */
    public HashService() {
    }
}
