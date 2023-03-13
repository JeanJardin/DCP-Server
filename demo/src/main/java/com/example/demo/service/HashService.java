package com.example.demo.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Class to hash content
 * @author Nathan Gaillard: nathan.gaillard@students.hevs.ch
 */
@Service
public class HashService implements IHashService{

    /**
     * The hash algorithm in use
     */
    public final static String HASH_ALGORITHM = "MD5";

    /**
     * Hash json content
     * @param jsonObject message to digest
     * @return digest in a string format
     */
    @Override
    public String hashContent(JSONObject jsonObject) {
        byte[] data = jsonObject.toString().getBytes();

        return byteArrayToHex(hashData(data));
    }

    /**
     * Hash binary content such as images, videos, audios
     * @param resourceUrl the url where the binary content is stored
     * @return digest in a string format
     */
    @Override
    public String hashBinaryContent(String resourceUrl) {
        IVideoDownloader videoDownloader = new VideoDownloader();

        Future<byte[]> dataTemp =  videoDownloader.downloadVideo(resourceUrl);
        while(dataTemp.isDone()==false){
            System.out.println("Downloading..");
        }
        // Wait for the result of the future
        byte[] data = new byte[0];
        try {

            data = dataTemp.get();
            System.out.println("Downloading finished");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return byteArrayToHex(hashData(data));
    }

    /**
     * Method to compare the hash from the mongoDB and the one computed on the tablet
     * @param hashMongoDB is the hash stored in the database
     * @param hashTablet is the hash computed directly on the tablet and send to the server
     * @return a boolean response to tell if it matches or not
     */
    public boolean compareHashSignature(String hashMongoDB, String hashTablet){
        return hashMongoDB.equals(hashTablet) ? true : false;
    }

    private byte[] hashData(byte[] data){
        byte[] res;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            res = digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
