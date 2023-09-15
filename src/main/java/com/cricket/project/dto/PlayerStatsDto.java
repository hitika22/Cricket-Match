package com.cricket.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerStatsDto {
    private String playerId;
    private String teamId;
    private String matchId;
}
