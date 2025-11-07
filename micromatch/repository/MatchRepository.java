package com.example.micromatch.repository;

import com.example.micromatch.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MatchRepository extends MongoRepository<Match, String> {
    Page<Match> findByDate(LocalDateTime date, Pageable pageable);
}
