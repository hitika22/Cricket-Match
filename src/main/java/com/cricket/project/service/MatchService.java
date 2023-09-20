package com.cricket.project.service;

import com.cricket.project.dto.MatchDto;
import com.cricket.project.model.Match;

import java.util.Map;

public interface MatchService {
    public Match setUpMatch(MatchDto doc);
}
