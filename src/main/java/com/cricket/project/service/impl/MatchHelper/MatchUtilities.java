package com.cricket.project.service.impl.MatchHelper;

import com.cricket.project.dto.MatchDto;
import com.cricket.project.model.Match;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;

import java.util.Date;
import java.util.Random;

public class MatchUtilities {
    public Match Toss(MatchDto document){
        Random random = new Random();
        int toss = random.nextInt(4);
        int tossWinningTeamId = 0;
        int battingFirstTeamId=0;
        switch (toss) {
            case 0:
                tossWinningTeamId=document.getTeam1Id();
                battingFirstTeamId=document.getTeam1Id();
                break;
            case 1:
                tossWinningTeamId=document.getTeam1Id();
                battingFirstTeamId=document.getTeam2Id();
                break;
            case 2:
                tossWinningTeamId=document.getTeam2Id();
                battingFirstTeamId=document.getTeam1Id();
                break;
            case 3:
                tossWinningTeamId=document.getTeam2Id();
                battingFirstTeamId=document.getTeam2Id();
                break;
        }
        Match match = Match.builder()
                .id(document.getId())
                .date(new Date())
                .team1Id(document.getTeam1Id())
                .team2Id(document.getTeam2Id())
                .matchVenue(document.getMatchVenue())
                .overs(document.getOvers())
                .tossWinningTeamId(tossWinningTeamId)
                .battingFirstTeamId(battingFirstTeamId)
                .build();
        return match;
    }
}
