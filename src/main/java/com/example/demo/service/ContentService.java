package com.example.demo.service;

import com.example.demo.model.Content;
import com.example.demo.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class implements the IContentService interface and is responsible for providing the necessary functionalities for
 * interacting with the ContentRepository. It contains methods to add content to the repository and retrieve content based
 * on the Airtable ID.
 */
@Service
public class ContentService implements IContentService {


    @Autowired
    private ContentRepository contentRepository;

    /**
     * Adds the given content to the ContentRepository.
     *
     * @param content the content to be added to the repository
     */
    @Override
    public void addContent(Content content) {
        contentRepository.save(content);
    }

    /**
     * Default constructor for the ContentService class.
     */
    public ContentService() {
    }

    /**
     * Constructor for the ContentService class that takes a ContentRepository object as a parameter.
     *
     * @param contentRepository the ContentRepository object to be used by the ContentService
     */
    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    /**
     * Retrieves the content from the repository based on the given Airtable ID.
     *
     * @param id the Airtable ID of the content to retrieve
     * @return an Optional object containing the content with the given Airtable ID, or empty if it does not exist in the repository
     */
    @Override
    public Optional<Content> getContentByAirtableId(String id) {
        return contentRepository.findByAirtableID(id);
    }

    /**
     * Deletes all content from the ContentRepository.
     */
    @Override
    public void deleteAllContent() {
        contentRepository.deleteAll();
        System.out.println("Database wiped out..");
    }

    /**
     * Gets the ContentRepository object used by the ContentService.
     *
     * @return the ContentRepository object used by the ContentService
     */
    public ContentRepository getContentRepository() {
        return contentRepository;
    }


    /**
     * Updates the content in the ContentRepository based on the given content from Airtable.
     *
     * @param contentFromAirtable the content from Airtable to use for the update
     * @return true if the content was updated, false otherwise
     */
    public boolean updateContent(Content contentFromAirtable) {
        Optional<Content> contentFromDB = contentRepository.findByAirtableID(contentFromAirtable.getAirtableID());

        if (contentFromDB.isPresent()) {
            Content contentDB = contentFromDB.get();
            if (contentDB.getJsonHash().equals(contentFromAirtable.getJsonHash())) {
                return false;
            } else {
                System.out.println("--UPDATE INFO--");
                System.out.println("Airtable id found in DB : " + contentDB.getAirtableID());
                System.out.println("New JSON HASH of airtable : " + contentFromAirtable.getJsonHash());
                System.out.println("Old JSON HASH of contentDB: " + contentDB.getJsonHash());
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

    /**
     * Starts a periodic check of content integrity
     * with the specified interval in minutes.
     * @param intervalMinutes the interval in minutes at which the check should be performed
     * @throws NullPointerException if the method checkContentIntegrity is not implemented or returns null
     */
    public static void startPeriodicCheck(int intervalMinutes) {
        ScheduledExecutorService schedule;
        schedule = Executors.newSingleThreadScheduledExecutor();

        schedule.scheduleAtFixedRate(() -> {

            System.out.println("Periodic check: " + LocalDateTime.now());
        }, 0, intervalMinutes, TimeUnit.SECONDS);
    }


}
