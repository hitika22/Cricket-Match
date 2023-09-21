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
public class BatAndBallOrder {
    List<Player> firstInningBattingOrder;
    List<Player> firstInningBowlingOrder;
    List<Player> secondInningBattingOrder;
    List<Player> secondInningBowlingOrder;
    int firstInningBattingTeamId;
    int secondInningBattingTeamId;
}
