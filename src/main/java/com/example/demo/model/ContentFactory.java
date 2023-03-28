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
    public int createContent(String tableName) throws JSONException {
        int countAdded = 0;
        int countPassed = 0;
        int countUpdated = 0;
        List<JSONObject> jsonObjectList = null;
        try {
            jsonObjectList = airtableService.createJsonObject(tableName, DotenvConfig.get("BASE_ID"), DotenvConfig.get("ACCESS_TOKEN"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (JSONObject object : jsonObjectList) {
            Content content = new Content();
            content = createContentFromJson(object);
            //check for update

            //check if match database
            if (!isContentAlreadyInDatabaseWithAirtableId(content)) {
                contentService.addContent(content);
                countAdded++;
                countPassed++;
                System.out.println("No airtable id match found, adding this content into the database");
            } else {
                if (contentService.updateContent(content)) {
                    countUpdated++;
                    countPassed++;
                    System.out.println("Content is updated");
                }else{
                    countPassed++;
                    System.out.println("--");
                    System.out.println("Airtable id match, no update needed, skipping this content..");
                    System.out.println("--");
                }

            }
        }
        System.out.println("----------------------");
        System.out.println("Reload finished with " + countAdded + " new contents");
        System.out.println("Reload finished by passing trough " + countPassed + " elements");
        System.out.println("Reload finished by updating " + countUpdated + " elements");
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
    public Content createContentFromJson(JSONObject jsonObject) throws JSONException {
        Content content = new Content();

        List<String> listOfHttps = airtableService.findHttps(jsonObject);
        // List<String> listHashes = new ArrayList<>();
        // content.setBinaryHashes(listHashes);

        //do the binary hashes
        for (String httpsUrl : listOfHttps) {
            System.out.println("Https URL in cCFj : " + httpsUrl);
            content.addBinaryHashToList(hashService.hashBinaryContent(httpsUrl));
            // listHashes.add(hashService.hashBinaryContent(httpsUrl));
            System.out.println("added binary hash to list");
        }

        //do the jsonHash
        content.setJsonHash(hashService.hashContent(jsonObject));
        content.setAirtableID(jsonObject.optString("id"));
        return content;
    }

    private String reformattedUrl(String url) {
        // Replace
        url = url.replaceAll("\\[", "");
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
    public boolean isContentAlreadyInDatabaseWithAirtableId(Content content) {
        // if content already exist in database but has modified hash
        //TODO compare with airtable id and not jsonHash
        if (contentService.getContentRepository().existsByAirtableID(content.getAirtableID())) {
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
