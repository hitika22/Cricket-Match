package com.cricket.project.dto;

import lombok.Data;

@Data
public class AddPlayerToTeamDto {
    private int teamId;
    private int playerId;
}
