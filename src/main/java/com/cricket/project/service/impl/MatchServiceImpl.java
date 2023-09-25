package com.cricket.project.service.impl;

import com.cricket.project.dto.MatchDto;
import com.cricket.project.model.*;
import com.cricket.project.repository.BallRepository;
import com.cricket.project.repository.PlayerRepository;
import com.cricket.project.repository.TeamRepository;
import com.cricket.project.service.MatchService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Data
@NoArgsConstructor
public class MatchServiceImpl implements MatchService {
    @Autowired
    private TeamServiceImpl teamService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private BallRepository ballRepository;
    @Autowired
    private InningServiceImpl inningService;

    @Override
    public Match setUpMatch(MatchDto document) {
        Match match = Toss(document);
        playMatch(match);
        return match;
    }

    public Match Toss(MatchDto document) {
        Random random = new Random();
        int toss = random.nextInt(4);
        int tossWinningTeamId = 0;
        int battingFirstTeamId = 0;
        switch (toss) {
            case 0:
                tossWinningTeamId = document.getTeam1Id();
                battingFirstTeamId = document.getTeam1Id();
                break;
            case 1:
                tossWinningTeamId = document.getTeam1Id();
                battingFirstTeamId = document.getTeam2Id();
                break;
            case 2:
                tossWinningTeamId = document.getTeam2Id();
                battingFirstTeamId = document.getTeam1Id();
                break;
            case 3:
                tossWinningTeamId = document.getTeam2Id();
                battingFirstTeamId = document.getTeam2Id();
                break;
        }
        return Match.builder()
                .id(document.getId())
                .date(new Date())
                .team1Id(document.getTeam1Id())
                .team2Id(document.getTeam2Id())
                .matchVenue(document.getMatchVenue())
                .overs(document.getOvers())
                .tossWinningTeamId(tossWinningTeamId)
                .battingFirstTeamId(battingFirstTeamId)
                .build();
    }

    public void playMatch(Match match) {

        InningStats inningStats1;
        InningStats inningStats2;
        BatAndBallOrder batAndBallOrder = battingAndBowlingTeamOrder(match);
        int firstInningScore = inningService.playInning(match, batAndBallOrder.getFirstInningBattingOrder(), batAndBallOrder
                .getFirstInningBowlingOrder(), batAndBallOrder.getFirstInningBattingTeamId(), 9999);
        int wickets1 = ballRepository.wickets(match.getId(),batAndBallOrder.getFirstInningBattingTeamId());

        inningStats1=InningStats.builder()
                .matchId(match.getId())
                .battingTeamId(batAndBallOrder.getFirstInningBattingTeamId())
                .inningNumber(1)
                .runs(firstInningScore)
                .wickets(wickets1)
                .build();

        System.out.println(inningStats1);

        int secondInningScore = inningService.playInning(match, batAndBallOrder.getSecondInningBattingOrder(), batAndBallOrder
                .getSecondInningBowlingOrder(), batAndBallOrder.getSecondInningBattingTeamId(), firstInningScore);
        int wickets2 = ballRepository.wickets(match.getId(),batAndBallOrder.getSecondInningBattingTeamId());

        inningStats2 = InningStats.builder()
                .matchId(match.getId())
                .battingTeamId(batAndBallOrder.getSecondInningBattingTeamId())
                .inningNumber(2)
                .runs(secondInningScore)
                .wickets(wickets2)
                .build();
        System.out.println(inningStats2);
        if(inningStats1.getRuns()>inningStats2.getRuns())
        {
            System.out.println("winner "+ inningStats1.getBattingTeamId());
        }
        else {
            System.out.println("winner "+ inningStats2.getBattingTeamId());
        }
    }

    public BatAndBallOrder battingAndBowlingTeamOrder(Match match) {
        List<Player> firstInningBattingOrder;
        List<Player> firstInningBowlingOrder;
        List<Player> secondInningBattingOrder;
        List<Player> secondInningBowlingOrder;


        BattingAndBallingTeam battingAndBallingTeam = decideBatOrBallTeam(match);

        firstInningBattingOrder = orderBuilder(battingAndBallingTeam.getBattingTeamPlayers(), "batting");
        secondInningBowlingOrder = orderBuilder(battingAndBallingTeam.getBowlingTeamPlayers(), "bowler");
        firstInningBowlingOrder = orderBuilder(battingAndBallingTeam.getBowlingTeamPlayers(), "bowling");
        secondInningBattingOrder = orderBuilder(battingAndBallingTeam.getBattingTeamPlayers(), "batting");

        return BatAndBallOrder.builder()
                .firstInningBattingOrder(firstInningBattingOrder)
                .firstInningBowlingOrder(firstInningBowlingOrder)
                .secondInningBattingOrder(secondInningBattingOrder)
                .secondInningBowlingOrder(secondInningBowlingOrder)
                .firstInningBattingTeamId(battingAndBallingTeam.getFirstInningBattingTeamId())
                .secondInningBattingTeamId(battingAndBallingTeam.getSecondInningBattingTeamId())
                .build();
    }

    public BattingAndBallingTeam decideBatOrBallTeam(Match match) {
        int firstInningBattingTeamId;
        int secondInningBattingTeamId;
        Team battingTeam = teamRepository.findTeamById(match.getBattingFirstTeamId());
        List<Integer> battingTeamPlayers = battingTeam.getTeamPlayersId();
        firstInningBattingTeamId = battingTeam.getId();

        Team bowlingTeam;
        List<Integer> bowlingTeamPlayers;

        if (battingTeam.getId() == match.getTeam1Id()) {
            bowlingTeam = teamRepository.findTeamById(match.getTeam2Id());
            secondInningBattingTeamId = match.getTeam2Id();
        } else {
            bowlingTeam = teamRepository.findTeamById(match.getTeam1Id());
            secondInningBattingTeamId = match.getTeam1Id();
        }
        bowlingTeamPlayers = bowlingTeam.getTeamPlayersId();

        return BattingAndBallingTeam.builder()
                .firstInningBattingTeamId(firstInningBattingTeamId)
                .secondInningBattingTeamId(secondInningBattingTeamId)
                .battingTeamPlayers(battingTeamPlayers)
                .bowlingTeamPlayers(bowlingTeamPlayers).build();

    }

    public List<Player> orderBuilder(List<Integer> teamPlayers, String batOrBall) {
        List<Player> players;
        if (Objects.equals(batOrBall, "batting")) {
            players = playerRepository.findListOfPlayersById(teamPlayers);
        } else {
            players = playerRepository.findListOfBowlersById(teamPlayers);
        }
        return players;
    }

}