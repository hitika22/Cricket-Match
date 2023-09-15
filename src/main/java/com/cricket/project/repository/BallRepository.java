package com.cricket.project.repository;

import com.cricket.project.model.Ball;
import com.cricket.project.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BallRepository extends MongoRepository<Ball,String> {
}
