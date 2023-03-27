package com.example.demo.model;

import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.demo.service.HashService;
import com.example.envUtils.DotenvConfig;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * A class that implements the {@link IContentFactory} interface, responsible for creating {@link Content} objects
 * from JSON objects obtained from an Airtable API response. It uses an {@link AirtableService} to obtain the response list,
 * a {@link HashService} to hash the content, and a {@link ContentService} to check whether the content already exists in the database.
 */
@Service
public class ContentFactory implements IContentFactory {
    /**
     * The ContentService used to add content to the database and check whether content already exists in the database.
     */
    @Autowired
    ContentService contentService;
    /**
     * The AirtableService used to obtain the response list from an Airtable API call.
     */
    @Autowired
    AirtableService airtableService;
    //ContentService contentService = new ContentService();
    /**
     * The HashService used to hash the content and check whether it already exists in the database.
     */
    @Autowired
    HashService hashService;
    /**
     * Creates Content objects from JSON objects obtained from an Airtable API response and adds them to the database.
     *
     * @param tableName the name of the table in Airtable to obtain the response list from
     * @return the number of new content items added to the database
     * @throws RuntimeException if there is an error parsing the JSON response or an IOException occurs while calling the Airtable API
     */
    @Override
    public int createContent(String tableName)  {
        int count = 0;
        int countAdded=0;
        int countPassed=0;
        List<JSONObject> jsonObjectList = null;
        try {
            jsonObjectList = airtableService.getResponseList(tableName, DotenvConfig.get("BASE_ID"),DotenvConfig.get("ACCESS_TOKEN"));
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
                countAdded++;
                countPassed++;
                System.out.println("No match found, adding this content into the database");
            } else {
                countPassed++;
                System.out.println("--");
                System.out.println("Duplicate content found skipping this content..");
                System.out.println("Found this in the database :");
                System.out.println(contentService.getContentRepository().findByJsonHash(content.getJsonHash().toString()));

                System.out.println("--");
            }
        }
        System.out.println("----------------------");
        System.out.println("Reload finished with " + countAdded + " new contents");
        System.out.println("Reload finished by passing trough " + countPassed + " elements");
        System.out.println("----------------------");
        return countAdded;
    }

    /**
     * Creates a Content object from a JSON object obtained from an Airtable API response.
     *
     * @param jsonObject the JSON object obtained from the Airtable API response
     * @return a Content object created from the JSON object
     * @throws RuntimeException if there is an error parsing the JSON object
     */
    @Override
    public Content createContentFromJson(JSONObject jsonObject) {
        Content content = new Content();

        JSONObject fieldsObject;
        try {
            fieldsObject = jsonObject.getJSONObject("fields");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String fieldType = getFieldType(fieldsObject);
        switch (fieldType) {
            case "VideoURL":
                String videoURL = fieldsObject.optString("VideoURL");
                if(videoURL.contains("[") && videoURL.contains("\"")){
                    videoURL = reformattedUrl(videoURL);
                }
                System.out.println("Video URL replaced : " + videoURL);
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

    private String reformattedUrl(String url) {
        // Replace
        url = url.replaceAll("\\[","");
        url = url.replaceAll("]", "");

        // Replace all occurrences of backslashes with forward slashes
        url = url.replaceAll("\\\\", "/");

        // Replace any sequence of more than one forward slash with just one
        url = url.replaceAll("/{2,}", "/");

        // Replace any sequence of one or more forward slashes followed by a colon with just two forward slashes
        url = url.replaceAll("(?<=https:)/+", "//");

        return url;
    }

    /**
     * Returns the type of field in the given JSON object.
     *
     * @param fieldsObject the JSON object containing the field to check
     * @return a string indicating the type of field ("VideoURL", "File", or "NoField")
     */
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

    /**
     * This method checks if the given Content object already exists in the database.
     *
     * @param content the Content object to check for existence in the database
     * @return true if the Content object already exists in the database, false otherwise
     */
    @Override
    public boolean isContentAlreadyInDatabase(Content content) {

        if (contentService.getContentRepository().existsByBinaryHash(content.getBinaryHash())
                || contentService.getContentRepository().existsByJsonHash(content.getJsonHash())) {

            if (content.getBinaryHash() == null || content.getBinaryHash().equals("")) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public ContentFactory() {
    }

    public ContentFactory(ContentService contentService, AirtableService airtableService, HashService hashService) {
        this.contentService = contentService;
        this.airtableService = airtableService;
        this.hashService = hashService;
    }
}
