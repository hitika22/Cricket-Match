package com.cricket.project.model;

public class MatchScoreCard {
    private String matchId;
    private String winner;
    private int team1Runs;
    private int team1Wickets;
    private int team2Runs;
    private int team2Wickets;
    private enum matchType{
        testCricket,oneDayCricket,twenty20
    }
    private int totalOvers;
    private int winningTeamWonBy;
}
