package com.example.demo.model;
//TODO FromAirtableToContentObject
public class ContentFactory implements IContentFactory {
    @Override
    public Content[] createContent(String tableName) {


        // get all the json related to the tablename

        // for each element in the table => put in an object Content
            // all the json of the element into jsonObject field
            // airtable id of the element into airtableId field
            // hash the entire json element  into contentHash field
            // if there is a field "video" download the video and hash the video into the binaryContent field

        // for each Content object now created, add them to the database

        // database is ready ?


        return new Content[0];
    }
}
