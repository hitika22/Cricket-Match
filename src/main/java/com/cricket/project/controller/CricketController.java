package com.cricket.project.controller;

import com.cricket.project.dto.PlayerStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CricketController {
    @PostMapping("/playerStats")
    public PlayerStatsDto getPlayerDetailsForAParticularMatch(@RequestBody PlayerStatsDto playerStatsDTO) {
        /*
            Three request variable that's why converted it to the post request.
        */
        log.info("Request for Player Stats for playerId {}, teamId {},matchId {}",playerStatsDTO.getPlayerId(),playerStatsDTO.getTeamId(),playerStatsDTO.getMatchId());
        return   playerStatsDTO;
    }


}
