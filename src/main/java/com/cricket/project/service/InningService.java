package com.cricket.project.service;

import com.cricket.project.model.Match;
import com.cricket.project.model.Player;

import java.util.List;

public interface InningService {
    public int playInning(Match match, List<Player> battingOrder, List<Player> bowlingOrder, int battingTeamId, int targetScore);

}
