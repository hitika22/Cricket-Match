package com.cricket.project.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
public class MatchDto {
    @Id
    private int id;
    private int team1Id;
    private int team2Id;
    private String matchVenue;
    private int overs;
}
