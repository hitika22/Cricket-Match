package com.cricket.project.service.impl;

import com.cricket.project.model.Player;
import com.cricket.project.repository.PlayerRepository;
import com.cricket.project.service.PlayerService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@NoArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public String addPlayer(Player player) {
        playerRepository.savePlayer(player);
        return "Player Added Successfully!!";
    }

    @Override
    public Player getPlayerById(int playerId) {
        return playerRepository.findPlayerById(playerId);
    }

    @Override
    public String removePlayer(int playerId) {
        return playerRepository.removePlayer(playerId);
    }


    @Override
    public List<Player> getPlayerList() {
        return playerRepository.findAllPlayers();
    }

}
