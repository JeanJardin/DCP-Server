package com.example.demo.repository;

import com.example.demo.model.Content;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends MongoRepository<Content,String> {
    Content findByJsonHash(String jsonHash);
    Content findByBinaryHash(String binaryHash);
    boolean existsByJsonHash(String jsonHash);
    boolean existsByBinaryHash(String binaryHash);
}
