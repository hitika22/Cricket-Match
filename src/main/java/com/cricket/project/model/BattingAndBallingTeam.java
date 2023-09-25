package com.cricket.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattingAndBallingTeam {
    int firstInningBattingTeamId;
    int secondInningBattingTeamId;
    List<Integer> battingTeamPlayers;
    List<Integer> bowlingTeamPlayers;
}
