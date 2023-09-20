package com.cricket.project.controller;

import com.cricket.project.dto.AddPlayerToTeamDto;
import com.cricket.project.model.Team;
import com.cricket.project.service.impl.TeamServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TeamController {

    @Autowired
    private TeamServiceImpl teamService;

    @PostMapping("/createTeam")
    public Team createTeam(@RequestBody Team document)
    {
        return teamService.createTeam(document);
    }


    @PostMapping("/addPlayerToTeam")
    public Team addPlayerToTeam(@RequestBody AddPlayerToTeamDto document)
    {
        return teamService.addPlayerToTeam(document.getPlayerId(),document.getTeamId());
    }

    @PostMapping("/removePlayerFromTeam")
    public Team removePlayerFromTeam(@RequestBody AddPlayerToTeamDto document)
    {
        return teamService.removePlayerFromTeam(document.getPlayerId(),document.getTeamId());
    }




}
