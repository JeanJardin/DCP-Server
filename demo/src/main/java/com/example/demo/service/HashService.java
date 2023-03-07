package com.example.demo.service;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class to hash content
 * @author Nathan Gaillard: nathan.gaillard@students.hevs.ch
 */
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
        byte[] data = videoDownloader.downloadVideo(resourceUrl);

        return byteArrayToHex(hashData(data));
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
