package com.cricket.project.model;

public class Player {
    private String playerId;
    private String playerName;
    private String playerTeam;

    private enum playerType{
        batsman,baller,allRounder;
    }
    private int totalRunScored;
    private int totalWicketsTaken;
    private int totalMatchesPlayed;

    private  boolean isCaptain;

}
