package com.cricket.project.model;


import com.cricket.project.enums.PlayerType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Entity
@Document("player")
public class Player {
    @Id
    private int id;
    private String playerName;
    private PlayerType playerType;
    private int age;
    private int totalRunScored;
    private int totalWicketsTaken;
    private int totalMatchesPlayed;


}
