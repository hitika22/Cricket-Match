package com.cricket.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class InningStats {
    private int matchId;
    private int battingTeamId;
    private int inningNumber;
    private int runs;
    private int wickets;
}
