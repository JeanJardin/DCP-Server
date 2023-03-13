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
            try {
                content = createContentFromJson(object);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (!isContentAlreadyInDatabase(content)) {
                contentService.addContent(content);
                count++;
            } else {
                System.out.println("Duplicate content found, skipping this content..");
            }
        }

        return count;
    }

    private Content createContentFromJson(JSONObject jsonObject) throws JSONException {
        Content content = new Content();

        JSONObject fieldsObject = jsonObject.getJSONObject("fields");

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

    private String getFieldType(JSONObject fieldsObject) {
        if (fieldsObject.has("VideoURL")) {
            return "VideoURL";
        } else if (fieldsObject.has("File")) {
            return "File";
        } else {
            return "NoField";
        }
    }

    private boolean isContentAlreadyInDatabase(Content content) {
        if (contentService.getContentRepository().existsByBinaryHash(content.getBinaryHash())
                || contentService.getContentRepository().existsByJsonHash(content.getJsonHash())) {
            return true;
        } else {
            return false;
        }
    }



}
