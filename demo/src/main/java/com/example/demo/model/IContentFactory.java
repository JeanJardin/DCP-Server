package com.example.demo.model;

import javax.swing.text.AbstractDocument;

public interface IContentFactory {

    //methods
    Content[] createContent(String tableName);
}
