package com.cricket.project.service.impl;

import com.cricket.project.enums.PlayerType;
import com.cricket.project.model.Player;
import com.cricket.project.model.Team;
import com.cricket.project.repository.PlayerRepository;
import com.cricket.project.repository.TeamRepository;
import com.cricket.project.service.TeamService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class TeamServiceImpl implements TeamService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;


    private Map<Integer, Integer> exist = new HashMap<Integer, Integer>();
    int count=0;
    @Override
    public Team createTeam(Team doc) {
        count++;
        List<Player> bowlers = playerRepository.findPlayersByPlayerType(PlayerType.valueOf("bowler"));
        List<Player> batsmen = playerRepository.findPlayersByPlayerType(PlayerType.valueOf("batsman"));
        List<Player> allRounders = playerRepository.findPlayersByPlayerType(PlayerType.valueOf("allRounder"));
        List<Integer> players = new ArrayList<>();
        List<Player> playerNames = new ArrayList<>();

        int numberOfBowlers = 4;
        int numberOfBatsman = 4;
        int numberOfAllRounder = 3;

        for(Player allRounder:allRounders)
        {
            int allRounderId = allRounder.getId();
            if(!exist.containsKey(allRounderId) && numberOfAllRounder>0)
            {
                exist.put(allRounderId,1);
                players.add(allRounderId);
                playerNames.add(allRounder);
                numberOfAllRounder--;
            }
        }
        for(Player batsman:batsmen)
        {
            int batsmanId = batsman.getId();
            if(!exist.containsKey(batsmanId) && numberOfBatsman>0)
            {
                exist.put(batsmanId,1);
                players.add(batsmanId);
                numberOfBatsman--;
            }
        }
        for(Player bowler:bowlers)
        {
            int bowlerId = bowler.getId();
            if(!exist.containsKey(bowlerId) && numberOfBowlers>0)
            {
                exist.put(bowlerId,1);
                players.add(bowlerId);
                numberOfBowlers--;
            }

        }

        Team team = new Team();
        if(!playerNames.isEmpty())
        {
            team.setTeamCaptain(playerNames.get(0).getPlayerName());
        }

        team.setTeamName(doc.getTeamName());
        team.setId(count);
        team.setTeamPlayersId(players);
        teamRepository.saveTeam(team);
        return team;
    }

    @Override
    public Team addPlayerToTeam(int playerId, int teamId) {
        Team team = teamRepository.findTeamById(teamId);
        List<Integer> playerIds = team.getTeamPlayersId();
        playerIds.add(playerId);
        team.setTeamPlayersId(playerIds);
        teamRepository.saveTeam(team);
        return team;
     }

    @Override
    public Team removePlayerFromTeam(int playerId,int teamId) {
        Team team = teamRepository.findTeamById(teamId);
        List<Integer> playerIds = team.getTeamPlayersId();
        ArrayList<Integer> newPlayerIds = new ArrayList<>();
        for (Integer id : playerIds) {
            if (id != playerId) {
                newPlayerIds.add(id);
            }
        }
        team.setTeamPlayersId(newPlayerIds);
        teamRepository.saveTeam(team);
        return team;
    }
}
