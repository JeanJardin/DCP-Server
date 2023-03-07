package com.example.demo;

import com.example.demo.service.HashService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTests {
    @Test
    public void testHashJson() {
        HashService hashService = new HashService();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "John");
            jsonObject.put("age", 30);
            jsonObject.put("city", "New York");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String digest = hashService.hashContent(jsonObject);
        assertEquals("f16ad52c441d68260fa08eac10f01803", digest);
    }

    @Test
    public void testHashBinary() {
        HashService hashService = new HashService();
        String digest = hashService.hashBinaryContent("https://scolcast.ch/system/files/episodes/videos/d2239ff867d5d59ef4be6cab28848f7d6daf6da9/original/3.phrases_additionnelles_enneigement.mp4");
        assertEquals("d4ba482b58b71960a32e5c1de85c58d4", digest);
    }

    @Test
    public void testCompareHashSignature(){
        HashService hashService = new HashService();
        String hashMongoDB = "f16ad52c441d68260fa08eac10f01803";
        hashService.compareHashSignature(hashMongoDB,"f16ad52c441d68260fa08eac10f01803");
    }
}
