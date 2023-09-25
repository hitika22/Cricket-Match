package com.cricket.project.repository;

import com.cricket.project.enums.PlayerType;
import com.cricket.project.model.Player;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class PlayerRepository{

    private MongoTemplate mongoTemplate;
    PlayerRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate=mongoTemplate;
    }
    public Player findPlayerById(int id)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,Player.class);
    }

    public List<Player> findAllPlayers()
    {
        Query query = new Query();
        return mongoTemplate.find(query, Player.class);
    }

    public void insertPlayer(Player player) {
        mongoTemplate.insert(player);
    }

    public String removePlayer(int id) {
        Query query = new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query, Player.class);
        return "Player Removed Successfully!!";
    }
    public void savePlayer(Player player) {
        mongoTemplate.save(player);
    }

    public List<Player> findPlayersByPlayerType(PlayerType playerType) {
        Query query = new Query(Criteria.where("playerType").is(playerType));
        return mongoTemplate.find(query, Player.class);
    }

    public List<Player> findListOfPlayersById(List<Integer> playerIds) {

        AggregationOperation customSort = context -> {
            // Use the $switch operator for custom sorting
            return context.getMappedObject(
                    Document.parse("{$addFields: { sortPriority: {$switch: { branches: ["
                            + "{ case: {$eq: ['$playerType', 'batsman']}, then: 3 },"
                            + "{ case: {$eq: ['$playerType', 'allRounder']}, then: 2 },"
                            + "{ case: {$eq: ['$playerType', 'bowler']}, then: 1 }], default: 0 } } } }")
            );
        };

        Aggregation aggregation = newAggregation(
                customSort,
                Aggregation.sort(Sort.Direction.DESC, "sortPriority"),
                Aggregation.match(Criteria.where("_id").in(playerIds))
        );
        return mongoTemplate.aggregate(aggregation, "player", Player.class).getMappedResults();

    }

    public List<Player> findListOfBowlersById(List<Integer> playerIds) {
        Criteria criteria = Criteria.where("playerType").is("bowler")
                .and("id").in(playerIds); // Add a condition to check if the ID is in the provided list.

        Query query = new Query(criteria);
        return mongoTemplate.find(query, Player.class);
    }




}
