package com.cricket.project.model;

import lombok.Data;

@Data
public class InningStats {
    private int matchId;
    private int battingTeamId;
    private int inningNumber;
    private String battingTeam;
    private String bowlingTeam;
    private int runs;
    private int wickets;
}
