package com.cricket.project.service.impl;

import com.cricket.project.dto.MatchDto;
import com.cricket.project.enums.PlayerType;
import com.cricket.project.model.Ball;
import com.cricket.project.model.Match;
import com.cricket.project.model.Player;
import com.cricket.project.model.Team;
import com.cricket.project.repository.BallRepository;
import com.cricket.project.repository.PlayerRepository;
import com.cricket.project.repository.TeamRepository;
import com.cricket.project.service.MatchService;
import com.cricket.project.service.impl.MatchHelper.MatchUtilities;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
    int targetRuns;
    int chaseRuns;

    @Override
    public Match setUpMatch(MatchDto document) {
        targetRuns=0;
        chaseRuns=0;

        MatchUtilities matchUtilities=new MatchUtilities();
        Match match=matchUtilities.Toss(document);

        playMatch(match);

        return match;
    }

    public void playMatch(Match match) {
        playInning(match, 0);
        System.out.println("target: "+ targetRuns+" and chase: "+chaseRuns);
        playInning(match, 1);
        System.out.println("target: "+ targetRuns+" and chase: "+chaseRuns);
    }
    public void playInning(Match match,int inningNumber){

            if(inningNumber==0){ //first Inning

                List<Player> battingOrder= new ArrayList<>();
                List<Player> bowlingOrder= new ArrayList<>();
                int wickets=0;

                Team battingTeam = teamRepository.findTeamById(match.getBattingFirstTeamId());
                List<Integer> battingTeamPlayers = battingTeam.getTeamPlayersId();

            Team bowlingTeam;
            List<Integer> bowlingTeamPlayers;

            if(battingTeam.getId() == match.getTeam1Id()) {
                bowlingTeam=teamRepository.findTeamById(match.getTeam2Id());
            }
            else {
                bowlingTeam=teamRepository.findTeamById(match.getTeam1Id());
            }
            bowlingTeamPlayers = bowlingTeam.getTeamPlayersId();

            for(Integer id:battingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.batsman){
                    battingOrder.add(player);
                }
            }

            for(Integer id:battingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.allRounder){
                    battingOrder.add(player);
                }
            }
            for(Integer id:battingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.bowler){
                    battingOrder.add(player);
                }
            }
            for(Integer id: bowlingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.bowler)
                {
                    bowlingOrder.add(player);
                }
            }

            for(Player player:battingOrder){
                player.setTotalMatchesPlayed(player.getTotalMatchesPlayed()+1);
                playerRepository.savePlayer(player);
            }

            //
            Player striker=battingOrder.get(0);
            Player nonStriker=battingOrder.get(1);
            Player bowler;
//
            for(int i=0;i<match.getOvers();i++){
                bowler=bowlingOrder.get(i%bowlingOrder.size());
                for(int j=1;j<=6;j++){
                    Ball ball=new Ball();
                    ball.setBallId(i*6+j);
                    ball.setBowlerId(bowler.getId());
                    ball.setMatchId(match.getId());
                    ball.setBattingTeamId(battingTeam.getId());
                    ball.setStrikerId(striker.getId());
                    ball.setNonStrikerId(nonStriker.getId());

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
                            return;
                        }
                        striker=battingOrder.get(wickets+1);
                    }
                    else{
                        ball.setRuns(ballStatus);
                        striker.setTotalRunScored(striker.getTotalRunScored()+ballStatus);
                        targetRuns=targetRuns+ballStatus;
                        playerRepository.savePlayer(striker);
                        if(ballStatus%2==1){
                            Player temp=striker;
                            striker=nonStriker;
                            nonStriker=temp;
                        }
                        ballRepository.save(ball);
                    }
                }
                Player temp=striker;
                striker=nonStriker;
                nonStriker=temp;
            }
            System.out.println("wickets: "+wickets);
        }
        else { //second Inning

            List<Player> battingOrder= new ArrayList<>();
            List<Player> bowlingOrder= new ArrayList<>();
            int wickets=0;

            Team bowlingTeam = teamRepository.findTeamById(match.getBattingFirstTeamId());
            List<Integer> battingTeamPlayers = bowlingTeam.getTeamPlayersId();

            Team battingTeam;
            List<Integer> bowlingTeamPlayers;

            if(bowlingTeam.getId() == match.getTeam1Id()) {
                battingTeam=teamRepository.findTeamById(match.getTeam2Id());
            }
            else {
                battingTeam=teamRepository.findTeamById(match.getTeam1Id());
            }
            bowlingTeamPlayers = bowlingTeam.getTeamPlayersId();

            for(Integer id:battingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.batsman){
                    battingOrder.add(player);
                }
            }

            for(Integer id:battingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.allRounder){
                    battingOrder.add(player);
                }
            }
            for(Integer id:battingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.bowler){
                    battingOrder.add(player);
                }
            }
            for(Integer id: bowlingTeamPlayers)
            {
                Player player = playerRepository.findPlayerById(id);
                if(player.getPlayerType()== PlayerType.bowler)
                {
                    bowlingOrder.add(player);
                }
            }

            for(Player player:battingOrder){
                player.setTotalMatchesPlayed(player.getTotalMatchesPlayed()+1);
                playerRepository.savePlayer(player);
            }

            //
            Player striker=battingOrder.get(0);
            Player nonStriker=battingOrder.get(1);
            Player bowler;
//
            for(int i=0;i<match.getOvers();i++){
                bowler=bowlingOrder.get(i%bowlingOrder.size());
                for(int j=1;j<=6;j++){
                    Ball ball=new Ball();
                    ball.setBallId(i*6+j);
                    ball.setBowlerId(bowler.getId());
                    ball.setMatchId(match.getId());
                    ball.setBattingTeamId(battingTeam.getId());
                    ball.setStrikerId(striker.getId());
                    ball.setNonStrikerId(nonStriker.getId());

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
                            return;
                        }
                        striker=battingOrder.get(wickets+1);
                    }
                    else{
                        ball.setRuns(ballStatus);
                        striker.setTotalRunScored(striker.getTotalRunScored()+ballStatus);
                        chaseRuns=chaseRuns+ballStatus;
                        playerRepository.savePlayer(striker);
                        if(ballStatus%2==1){
                            Player temp=striker;
                            striker=nonStriker;
                            nonStriker=temp;
                        }
                        ballRepository.save(ball);
                        if(chaseRuns>targetRuns){
                            System.out.println("wickets: "+wickets);
                            return;
                        }
                    }
                }
                Player temp=striker;
                striker=nonStriker;
                nonStriker=temp;
            }
            System.out.println("wickets: "+wickets);
        }
    }
}
