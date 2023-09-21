package com.cricket.project.service.impl;

import com.cricket.project.dto.MatchDto;
import com.cricket.project.enums.PlayerType;
import com.cricket.project.model.*;
import com.cricket.project.repository.BallRepository;
import com.cricket.project.repository.PlayerRepository;
import com.cricket.project.repository.TeamRepository;
import com.cricket.project.service.MatchService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.annotation.Target;
import java.util.*;

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
    @Override
    public Match setUpMatch(MatchDto document) {
        Match match=Toss(document);

        playMatch(match);

        return match;
    }

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
    public void playMatch(Match match) {

        BatAndBallOrder batAndBallOrder=battingAndBowlingTeamOrder(match);
        int firstInningScore=playInning(match,batAndBallOrder.getFirstInningBattingOrder(),batAndBallOrder
                        .getFirstInningBowlingOrder(), batAndBallOrder.getFirstInningBattingTeamId(),9999);
        System.out.println("firstInningScore: "+ firstInningScore);
        int secondInningScore=playInning(match,batAndBallOrder.getSecondInningBattingOrder(),batAndBallOrder
                .getSecondInningBowlingOrder(), batAndBallOrder.getSecondInningBattingTeamId(),firstInningScore);
        System.out.println("secondInningScore: "+secondInningScore);
    }

    public BatAndBallOrder battingAndBowlingTeamOrder(Match match){
        List<Player> firstInningBattingOrder=new ArrayList<>();
        List<Player> firstInningBowlingOrder=new ArrayList<>();
        List<Player> secondInningBattingOrder=new ArrayList<>();
        List<Player> secondInningBowlingOrder=new ArrayList<>();
        int firstInningBattingTeamId=0;
        int secondInningBattingTeamId=0;

        Team battingTeam = teamRepository.findTeamById(match.getBattingFirstTeamId());
        List<Integer> battingTeamPlayers = battingTeam.getTeamPlayersId();
        firstInningBattingTeamId=battingTeam.getId();
        Team bowlingTeam;
        List<Integer> bowlingTeamPlayers;

        if(battingTeam.getId() == match.getTeam1Id()) {
            bowlingTeam=teamRepository.findTeamById(match.getTeam2Id());
            secondInningBattingTeamId=match.getTeam2Id();
        }
        else {
            bowlingTeam=teamRepository.findTeamById(match.getTeam1Id());
            secondInningBattingTeamId=match.getTeam1Id();
        }
        bowlingTeamPlayers = bowlingTeam.getTeamPlayersId();


        firstInningBattingOrder=orderBuilder(battingTeamPlayers,firstInningBattingOrder,PlayerType.batsman);
        firstInningBattingOrder=orderBuilder(battingTeamPlayers,firstInningBattingOrder,PlayerType.allRounder);
        firstInningBattingOrder=orderBuilder(battingTeamPlayers,firstInningBattingOrder,PlayerType.bowler);
        secondInningBowlingOrder=orderBuilder(battingTeamPlayers,secondInningBowlingOrder,PlayerType.bowler);

        firstInningBowlingOrder=orderBuilder(bowlingTeamPlayers,firstInningBowlingOrder,PlayerType.bowler);
        secondInningBattingOrder=orderBuilder(bowlingTeamPlayers,secondInningBattingOrder,PlayerType.batsman);
        secondInningBattingOrder=orderBuilder(bowlingTeamPlayers,secondInningBattingOrder,PlayerType.allRounder);
        secondInningBattingOrder=orderBuilder(bowlingTeamPlayers,secondInningBattingOrder,PlayerType.bowler);;

        return BatAndBallOrder.builder()
                .firstInningBattingOrder(firstInningBattingOrder)
                .firstInningBowlingOrder(firstInningBowlingOrder)
                .secondInningBattingOrder(secondInningBattingOrder)
                .secondInningBowlingOrder(secondInningBowlingOrder)
                .firstInningBattingTeamId(firstInningBattingTeamId)
                .secondInningBattingTeamId(secondInningBattingTeamId)
                .build();
    }

    public List<Player> orderBuilder(List<Integer> teamPlayers, List<Player> teamOrder, PlayerType playerType){
        for(Integer id:teamPlayers)
        {
            Player player = playerRepository.findPlayerById(id);
            if(player.getPlayerType()== playerType){
                teamOrder.add(player);
            }
        }
        return teamOrder;
    }
    public int playInning(Match match, List<Player> battingOrder, List<Player> bowlingOrder,int battingTeamId, int targetScore){
        int InningScore=0;
        Player striker=battingOrder.get(0);
        Player nonStriker=battingOrder.get(1);
        Player bowler;
        int wickets=0;

        for(int i=0;i<match.getOvers();i++){
            bowler=bowlingOrder.get(i%bowlingOrder.size());
            for(int j=1;j<=6;j++){
                Ball ball=Ball.builder()
                        .ballId(i*6+j)
                        .bowlerId(bowler.getId())
                        .matchId(match.getId())
                        .battingTeamId(battingTeamId)
                        .strikerId(striker.getId())
                        .nonStrikerId(nonStriker.getId())
                        .build();

                Random random=new Random();
                int ballStatus = random.nextInt(8);
                if(ballStatus==7){
                    ball.setWickets(1);
                    ball.setRuns(0);
                    ballRepository.save(ball);

                    bowler.setTotalWicketsTaken(bowler.getTotalWicketsTaken()+1);
                    playerRepository.savePlayer(bowler);

                    wickets++;
                    if(wickets==10){
                        System.out.println("wickets: "+wickets);
                        return InningScore;
                    }
                    striker=battingOrder.get(wickets+1);
                }
                else{
                    ball.setRuns(ballStatus);
                    striker.setTotalRunScored(striker.getTotalRunScored()+ballStatus);
                    InningScore=InningScore+ballStatus;
                    playerRepository.savePlayer(striker);
                    if(ballStatus%2==1){
                        Player temp=striker;
                        striker=nonStriker;
                        nonStriker=temp;
                    }
                    ballRepository.save(ball);
                    if(InningScore>targetScore){
                        System.out.println("wickets: "+wickets);
                        return InningScore;
                    }
                }
            }
            Player temp=striker;
            striker=nonStriker;
            nonStriker=temp;
        }
        System.out.println("wickets: "+wickets);
        return InningScore;
    }
}
