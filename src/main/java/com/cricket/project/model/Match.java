package com.cricket.project.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;

public class Match {
    private String matchId;
    private Date date;
    private Team team1;
    private Team team2;
    private String matchVenue;
    private int overs;
    private String tossWinningTeam;

}
