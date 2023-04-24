package com.example.demo.model;

import com.example.demo.service.AirtableService;
import com.example.demo.service.ContentService;
import com.example.demo.service.HashService;
import com.example.envUtils.DotenvConfig;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class ContentFactory implements IContentFactory {
	
    Dotenv dotenv = Dotenv.configure().load();
    String ACCESS_TOKEN = dotenv.get("ACCESS_TOKEN");
    String BASE_ID = dotenv.get("BASE_ID");
    String HTTP_AIRTABLE_TABLES = dotenv.get("HTTP_AIRTABLE_TABLES");

    @Autowired
    ContentService contentService;

    @Autowired
    AirtableService airtableService;
    //ContentService contentService = new ContentService();

    @Autowired
    HashService hashService;
    /**
     * Creates content from a given table name in Airtable.
     *
     * @param tableName The name of the table in Airtable.
     * @return The number of contents added.
     * @throws JSONException if there is an error while creating the JSON object.
     */
    @Override
    public int createContent(String tableName) throws JSONException {
        int countAdded = 0;
        int countPassed = 0;
        int countUpdated = 0;

        List<JSONObject> jsonObjectList = null;
        try {
            jsonObjectList = airtableService.createJsonObject(tableName, BASE_ID, ACCESS_TOKEN);
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
     * Creates content from a given JSON object retrieved from Airtable.
     *
     * @param jsonObject The JSON object retrieved from Airtable.
     * @return A Content object.
     * @throws JSONException if there is an error while creating the JSON object.
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
    /**

     Reformats the given URL by replacing certain characters.

     @param url The URL to be reformatted.

     @return The reformatted URL.
     */
    @Override
    public String reformattedUrl(String url) {
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

     Determines the type of field in the given JSON object.
     @param fieldsObject The JSON object containing the fields.
     @return A string representing the field type ("VideoURL", "File", or "NoField").
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

     Checks if a content object with the given airtable ID already exists in the database.
     @param content The content object to check.
     @return True if a content object with the given airtable ID exists in the database, false otherwise.
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
