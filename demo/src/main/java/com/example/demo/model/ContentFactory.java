package com.example.demo.model;

import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.demo.service.HashService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

//TODO FromAirtableToContentObject
@Service
public class ContentFactory implements IContentFactory {
    @Autowired
    ContentService contentService;
    @Autowired
    AirtableService airtableService;
    //ContentService contentService = new ContentService();
    @Autowired
    HashService hashService;

    @Override
    public Content[] createContent(String tableName) throws JSONException, IOException {

        // get all the json related to the tablename
        List<JSONObject> jsonObjectList = airtableService.getResponseList(tableName);

        // for each element in the table => put in an object Content
        for (JSONObject object : jsonObjectList) {

            Content content = new Content();
            JSONObject fieldsObject = object.getJSONObject("fields");

            if (fieldsObject.has("VideoURL")) {
                String videoURL = fieldsObject.optString("VideoURL");
                content.setBinaryHash(hashService.hashBinaryContent(videoURL));
                System.out.println("video hashed !");
            } else if (fieldsObject.has("File")) {
                String videoURL = fieldsObject.optString("File");
                content.setBinaryHash(hashService.hashBinaryContent(videoURL));
                System.out.println("video hashed !");
            } else {
                System.out.println("No video fields found..");
            }
            content.setJsonHash(hashService.hashContent(object));
            content.setAirtableID(object.optString("id"));
            // Remove transient content from memory
            content.setContentJson(null);

            // add content to the db
            if (!checkIfHashIsAlreadyInDB(content)) {
                System.out.println("same content not found in database, adding this content..");
                contentService.addContent(content);
            } else {
                System.out.println("Duplicate content found, skipping this content..");
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
    //TODO
    private boolean checkIfHashIsAlreadyInDB(Content content) {

        if (contentService.getContentRepository().existsByBinaryHash(content.getBinaryHash())){
            return true;
        }else if (contentService.getContentRepository().existsByJsonHash(content.getJsonHash())){
            return true;
        }
        return false;
    }

}
