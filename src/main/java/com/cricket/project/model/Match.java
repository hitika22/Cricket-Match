package com.cricket.project.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    private int id;
    private Date date;
    private int team1Id;
    private int team2Id;
    private String matchVenue;
    private int overs;
    private int tossWinningTeamId;
    private int battingFirstTeamId;
}
