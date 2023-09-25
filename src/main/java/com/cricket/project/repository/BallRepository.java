package com.cricket.project.repository;

import com.cricket.project.model.Ball;
import lombok.Data;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
public class BallRepository {
    private final MongoTemplate mongoTemplate;
    BallRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Data
    public static class ResultClass {
        private int totalWickets;
    }
    public void save(Ball ball)
    {
        mongoTemplate.save(ball);
    }
    public int wickets(int matchId,int battingTeamId)
    {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("matchId").is(matchId).and("battingTeamId").is(battingTeamId)
                ),
                Aggregation.group().sum("wickets").as("totalWickets")
        );
        AggregationResults<ResultClass> result = mongoTemplate.aggregate(
                aggregation, "ball", ResultClass.class
        );

        ResultClass aggregatedResult = result.getUniqueMappedResult();

        return aggregatedResult.getTotalWickets();
    }


}

