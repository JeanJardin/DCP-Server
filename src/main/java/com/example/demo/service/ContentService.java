package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * This class implements the IContentService interface and is responsible for providing the necessary functionalities for
 * interacting with the ContentRepository. It contains methods to add content to the repository and retrieve content based
 * on the Airtable ID.
 */
@Service
public class ContentService implements IContentService {

    /**
     * The instance of the ContentRepository that this class interacts with.
     */
    @Autowired
    private ContentRepository contentRepository;

    /**
     * Adds the given content to the ContentRepository.
     *
     * @param content the content to add to the repository
     */
    @Override
    public void addContent(Content content) {
        contentRepository.save(content);
    }

    public ContentService() {
    }

    /**
     * Constructs a new instance of ContentService with the given ContentRepository instance.
     *
     * @param contentRepository the ContentRepository instance to use
     */
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    /**
     * Retrieves the content from the repository based on the given Airtable ID.
     *
     * @param id the Airtable ID of the content to retrieve
     * @return the content with the given Airtable ID, or null if it does not exist in the repository
     */
    @Override
    public Optional<Content> getContentByAirtableId(String id) {
        return contentRepository.findByAirtableID(id);
    }

    @Override
    public void deleteAllContent() {
        contentRepository.deleteAll();
        System.out.println("Database wiped out..");
    }

    public ContentRepository getContentRepository() {
        return contentRepository;
    }


    public boolean updateContent(Content contentFromAirtable){
        Optional<Content> contentFromDB = contentRepository.findByAirtableID(contentFromAirtable.getAirtableID());

        if (contentFromDB.isPresent()) {
            Content contentDB = contentFromDB.get();
            if(contentDB.getJsonHash().equals(contentFromAirtable.getJsonHash())){
                return false;
            }else {
                System.out.println("--UPDATE INFO--");
                System.out.println("Airtable id matched : " + contentDB.getAirtableID());
                System.out.println("JSON HASH of airtable matched : " + contentFromAirtable.getJsonHash());
                System.out.println("JSON HASH of contentDB  matched : " + contentDB.getJsonHash());
                System.out.println("----");
                contentDB.setJsonHash(contentFromAirtable.getJsonHash());
                contentDB.setBinaryHashes(contentFromAirtable.getBinaryHashes());
                contentRepository.save(contentDB);
                return true;
            }
        } else {
            return false;
        }

    }


}
