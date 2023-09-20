package com.cricket.project.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.List;


@Data
public class Team {
    @Id
    private int id;
    @Getter
    private String teamName;
    @Getter
    private String teamCaptain;
    @Getter
    private List<Integer> teamPlayersId;
    @Getter
    private int totalWins;
    @Getter
    private int totalMatchesPlayed;

    public Team(){
        this.totalWins=0;
        this.totalMatchesPlayed=0;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamCaptain(String teamCaptain) {
        this.teamCaptain = teamCaptain;
    }

    public void setTeamPlayersId(List<Integer> teamPlayers) {
        this.teamPlayersId = teamPlayers;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setTotalMatchesPlayed(int totalMatchesPlayed) {
        this.totalMatchesPlayed = totalMatchesPlayed;
    }


}
