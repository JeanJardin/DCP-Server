package com.example.demo.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**

 The IContentFactory interface defines methods for creating and manipulating content objects.
 */
public interface IContentFactory {
    /**
     Creates content objects from the JSON objects in the specified Airtable table.
     @param tableName The name of the Airtable table containing the JSON objects.
     @return The number of new content objects created.
     @throws JSONException If there is an error parsing the JSON.
     */
    int createContent(String tableName) throws JSONException;
    /**

     Creates a content object from the given JSON object.
     @param jsonObject The JSON object to create the content object from.
     @return The newly created content object.
     @throws JSONException If there is an error parsing the JSON.
     */
    Content createContentFromJson(JSONObject jsonObject) throws JSONException;
    /**

     Reformats the given URL by replacing certain characters.
     @param url The URL to be reformatted.
     @return The reformatted URL.
     */
    String reformattedUrl(String url);
    /**

     Determines the type of field in the given JSON object.
     @param fieldsObject The JSON object containing the fields.
     @return A string representing the field type ("VideoURL", "File", or "NoField").
     */
    String getFieldType(JSONObject fieldsObject);
    /**

     Checks if a content object with the given airtable ID already exists in the database.
     @param content The content object to check.
     @return True if a content object with the given airtable ID exists in the database, false otherwise.
     */
    boolean isContentAlreadyInDatabaseWithAirtableId(Content content);
}
