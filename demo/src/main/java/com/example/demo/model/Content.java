package com.example.demo.model;

import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Content")
public class Content implements IContent{

    //Variables
    @Id
    private String contentID;
    private String contentHash;
    private JSONObject contentJson ;
    private Byte[] binaryContent;

    public Content() {
    }

    public Content(String contentHash, JSONObject contentJson, Byte[] binaryContent) {
        this.contentHash = contentHash;
        this.contentJson = contentJson;
        this.binaryContent = binaryContent;
    }
}
