package com.example.demo.test;

import com.example.envUtils.DotenvConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@Service
public class AirtableServiceTest {


    public static void main(String[] args) throws Exception {
        String accessToken = "pat3H3xSwZH17T2Ih.ba11844b35297813197d6a2ea4a76ece36bc07884928765a84e704738e4cb8eb";
        String baseId = "appDP93O1gy4p4KjC";
        String tableName = "ContainerCodes";

        HttpClient httpClient = HttpClientBuilder.create().build();
        //used to get only a desired table
        HttpGet request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName);
//        HttpGet request = new HttpGet("https://api.airtable.com/v0/" + baseId +"/");
        //used to get everything

//        HttpGet requestOld = new HttpGet("https://api.airtable.com/v0/meta/bases/" + baseId + "/tables");
        request.setHeader("Authorization", "Bearer " + accessToken);
        String response = EntityUtils.toString(httpClient.execute(request).getEntity());
        System.out.println(response);
    }


    public static void test(String[] args) throws IOException, InterruptedException, NoSuchAlgorithmException, JSONException {
        String accessToken = "pat0TalhEwsr9igsU.11f3fd461c25f39875650c2e2d593c0c7b6baca09641ec4e34e949a30c055e96";
        String baseId = "applto3j1obQLAVnr";
        String tableName = "ContainerCodes";

     HttpClient httpClient = HttpClientBuilder.create().build();
     HttpGet request = new HttpGet("https://api.airtable.com/v0/" + baseId + "/" + tableName);

     request.setHeader("Authorization", "Bearer " + accessToken);
     String response = EntityUtils.toString(httpClient.execute(request).getEntity());
     System.out.println(response);

        JSONObject outerObject = new JSONObject(response);
        JSONArray jsonArray = outerObject.getJSONArray("records");

        for (int i = 0, size = jsonArray.length(); i < size; i++)
        {
            JSONObject objectInArray = jsonArray.getJSONObject(i);
            String objectInArrayString = objectInArray.toString();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hashBytes = digest.digest(objectInArrayString.getBytes(StandardCharsets.UTF_8));
            String hash = bytesToHex(hashBytes);
            System.out.println("Number: "+i+" The MD5 hash of the Airtable response is: " + hash);
        }
    }

 private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

private static void DownloadVideo(){

}

 private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
         }
    return new String(hexChars);
 }








}






