package com.cricket.project.repository;

import com.cricket.project.enums.PlayerType;
import com.cricket.project.model.Player;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Player savePlayer(Player player) {
        return mongoTemplate.save(player);
    }

    public List<Player> findPlayersByPlayerType(PlayerType playerType) {
        Query query = new Query(Criteria.where("playerType").is(playerType));
        return mongoTemplate.find(query, Player.class);
    }

}
