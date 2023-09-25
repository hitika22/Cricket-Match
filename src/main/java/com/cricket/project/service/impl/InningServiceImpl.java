package com.cricket.project.service.impl;
import com.cricket.project.model.Ball;
import com.cricket.project.model.InningStats;
import com.cricket.project.model.Match;
import com.cricket.project.model.Player;
import com.cricket.project.repository.BallRepository;
import com.cricket.project.repository.PlayerRepository;
import com.cricket.project.service.InningService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
@Data
@NoArgsConstructor
public class InningServiceImpl implements InningService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private BallRepository ballRepository;

    Player striker;
    Player nonStriker;
    public int playInning(Match match, List<Player> battingOrder, List<Player> bowlingOrder, int battingTeamId, int targetScore) {
        int inningScore = 0;
        int wickets = 0;
        striker = battingOrder.get(0);
        nonStriker = battingOrder.get(1);

        for (int i = 0; i < match.getOvers(); i++) {
            Player bowler = getBowlerForOver(bowlingOrder, i);
            for (int j = 1; j <= 6; j++) {
                Ball ball = playBall((i*6+j),match.getId(), bowler.getId(), battingTeamId, striker, nonStriker);

                int ballStatus = generateRandomBallStatus();
                ball.setRuns(ballStatus);

                if (ballStatus == 7) {
                    handleWicket(ball, bowler);
                    wickets++;
                    if (wickets == 10) {
                        return inningScore;
                    }
                    striker = getNextBatsman(battingOrder, wickets);
                } else {
                    handleRuns(ball, striker);
                    if (ballStatus % 2 == 1) {
                        swapStrikers(striker, nonStriker);
                    }
                }
                inningScore += ballStatus;
                if (inningScore > targetScore) {
                    return inningScore;
                }
            }
            swapStrikers(striker, nonStriker);
        }
        return inningScore;
    }

    private Player getBowlerForOver(List<Player> bowlingOrder, int over) {
        return bowlingOrder.get(over % bowlingOrder.size());
    }

    private Ball playBall(int ballId,int matchId, int bowlerId, int battingTeamId, Player striker, Player nonStriker) {
        return Ball.builder()
                .ballId(ballId)
                .bowlerId(bowlerId)
                .matchId(matchId)
                .battingTeamId(battingTeamId)
                .strikerId(striker.getId())
                .nonStrikerId(nonStriker.getId())
                .build();
    }

    private int generateRandomBallStatus() {
        Random random = new Random();
        return random.nextInt(8);
    }

    private void handleWicket(Ball ball, Player bowler) {
        ball.setWickets(1);
        ball.setRuns(0);
        ballRepository.save(ball);
        bowler.setTotalWicketsTaken(bowler.getTotalWicketsTaken() + 1);
        playerRepository.savePlayer(bowler);
    }

    private void handleRuns(Ball ball, Player striker) {
        striker.setTotalRunScored(striker.getTotalRunScored() + ball.getRuns());
        playerRepository.savePlayer(striker);
        ballRepository.save(ball);
    }

    private Player getNextBatsman(List<Player> battingOrder, int wickets) {
        return battingOrder.get(wickets + 1);
    }

    private void swapStrikers(Player new_non_striker, Player new_striker) {
        striker = new_striker;
        nonStriker = new_non_striker;
    }
}

