package com.cricket.project.service;

import com.cricket.project.model.Team;

import java.util.Map;

public interface TeamService {
    public Team createTeam(Team doc);
    public Team addPlayerToTeam(int playerId,int teamId);
    public Team removePlayerFromTeam(int playerId,int teamId);


}
