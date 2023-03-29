package com.example.demo.repository;

import com.example.demo.model.Content;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface represents the repository for managing Content objects in MongoDB.
 * <p>
 * It extends the MongoRepository interface and provides methods for querying Content objects based on their Airtable ID, JSON hash, and binary hash.
 */
@Repository
public interface ContentRepository extends MongoRepository<Content, String> {
    /**
     * Finds a Content object by its Airtable ID.
     *
     * @param airtableID the Airtable ID of the Content object to find
     * @return the Content object with the specified Airtable ID, or null if not found
     */
    Optional<Content> findByAirtableID(String airtableID);

    /**
     * Finds a Content object by its JSON hash.
     *
     * @param jsonHash the JSON hash of the Content object to find
     * @return the Content object with the specified JSON hash, or null if not found
     */
    Content findByJsonHash(String jsonHash);



    /**
     * Checks if a Content object with the specified JSON hash exists in the repository.
     *
     * @param jsonHash the JSON hash to check for
     * @return true if a Content object with the specified JSON hash exists, false otherwise
     */
    boolean existsByJsonHash(String jsonHash);

    /**
     * This method checks if a record with the given Airtable ID exists in the database.
     *
     * @param airtableId the Airtable ID to check for existence
     * @return true if a record with the given Airtable ID exists in the database, false otherwise
     */
    boolean existsByAirtableID(String airtableId);



}
