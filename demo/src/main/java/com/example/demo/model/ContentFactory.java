package com.example.demo.model;

import com.example.demo.service.AirtableService;
import com.example.demo.service.HashService;
import com.example.demo.service.VideoDownloader;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

//TODO FromAirtableToContentObject
public class ContentFactory implements IContentFactory {

    AirtableService airtableService = new AirtableService();
    VideoDownloader videoDownloader = new VideoDownloader();

    HashService hashService = new HashService();


    @Override
    public Content[] createContent(String tableName) throws JSONException, IOException {

        List<Content> contentList;

        // get all the json related to the tablename
        List<JSONObject> jsonObjectList = airtableService.getResponseList(tableName);

        // for each element in the table => put in an object Content
        for (JSONObject object : jsonObjectList) {

            Content content = new Content();
            JSONObject fieldsObject = object.getJSONObject("fields");

            if(object.has("VideoURL")){
                JSONArray videoURLArray = fieldsObject.getJSONArray("VideoURL");
                String videoURL = videoURLArray.getString(0);
                content.setBinaryContent(videoDownloader.downloadVideo(videoURL));
                content.setContentHash(hashService.hashBinaryContent(videoURL));
            }
        }




            // all the json of the element into jsonObject field
            // airtable id of the element into airtableId field
            // hash the entire json element  into contentHash field
            // if there is a field "video" download the video and hash the video into the binaryContent field

        // for each Content object now created, add them to the database

        // database is ready ?


        return new Content[0];
    }
}
