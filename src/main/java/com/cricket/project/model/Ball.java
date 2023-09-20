package com.cricket.project.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

@Data
@CompoundIndexes({
        @CompoundIndex(name = "match_inning_ball", def = "{'matchId': 1, 'battingTeamId': 1, 'ballId': 1}", unique = true)
})
public class Ball {
    @Id
    private String id; // Use a unique identifier field, typically of type ObjectId
    private int matchId;
    private int battingTeamId;
    private int ballId;
    private int strikerId;
    private int nonStrikerId;
    private int bowlerId;
    private int runs;
    private int wickets;
}
