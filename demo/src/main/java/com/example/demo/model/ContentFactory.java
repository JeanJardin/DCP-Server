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
    public int createContent(String tableName)  {
        int count = 0;

        List<JSONObject> jsonObjectList = null;
        try {
            jsonObjectList = airtableService.getResponseList(tableName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (JSONObject object : jsonObjectList) {
            Content content = null;
            content = createContentFromJson(object);
            if (!isContentAlreadyInDatabase(content)) {
                contentService.addContent(content);
                count++;
            } else {
                System.out.println("Duplicate content found, skipping this content..");
            }
        }

        return count;
    }
    @Override
     public Content createContentFromJson(JSONObject jsonObject) {
        Content content = new Content();

         JSONObject fieldsObject = null;
         try {
             fieldsObject = jsonObject.getJSONObject("fields");
         } catch (JSONException e) {
             throw new RuntimeException(e);
         }

         String fieldType = getFieldType(fieldsObject);
        switch (fieldType) {
            case "VideoURL":
                String videoURL = fieldsObject.optString("VideoURL");
                content.setBinaryHash(hashService.hashBinaryContent(videoURL));
                System.out.println("Video hashed!");
                break;
            case "File":
                String fileURL = fieldsObject.optString("File");
                content.setBinaryHash(hashService.hashBinaryContent(fileURL));
                System.out.println("File hashed!");
                break;
            default:
                System.out.println("No video fields found..");
                break;
        }
        content.setJsonHash(hashService.hashContent(jsonObject));
        content.setAirtableID(jsonObject.optString("id"));

        return content;
    }
    @Override
     public String getFieldType(JSONObject fieldsObject) {
        if (fieldsObject.has("VideoURL")) {
            return "VideoURL";
        } else if (fieldsObject.has("File")) {
            return "File";
        } else {
            return "NoField";
        }
    }
    @Override
    public boolean isContentAlreadyInDatabase(Content content) {
        if (contentService.getContentRepository().existsByBinaryHash(content.getBinaryHash())
                || contentService.getContentRepository().existsByJsonHash(content.getJsonHash())) {
            return true;
        } else {
            return false;
        }
    }



}
