package com.example.micromatch.repository;

import com.example.micromatch.entity.Planification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanificationRepository extends MongoRepository<Planification, String> {
}
