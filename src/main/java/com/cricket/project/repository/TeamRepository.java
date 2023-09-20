package com.cricket.project.repository;

import com.cricket.project.model.Player;
import com.cricket.project.model.Team;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TeamRepository{
    private MongoTemplate mongoTemplate;

    TeamRepository(MongoTemplate mongoTemplate){
        this.mongoTemplate=mongoTemplate;
    }

    public Team findTeamById(int id)
    {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query,Team.class);
    }
    public Team saveTeam(Team team) {
        return mongoTemplate.save(team);
    }
}
