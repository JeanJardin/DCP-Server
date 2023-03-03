package com.example.demo.model;

import javax.swing.text.AbstractDocument;

public interface IContentDB {




    //Methods
    Content getContentById();
    int saveContent(Content content);


}
