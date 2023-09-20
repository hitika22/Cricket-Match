package com.cricket.project.service;

import com.cricket.project.model.Player;

import java.util.List;

public interface PlayerService {
    String addPlayer(Player player);

    Player getPlayerById(int playerId);

    String removePlayer(int playerId);

    List<Player> getPlayerList();
}
