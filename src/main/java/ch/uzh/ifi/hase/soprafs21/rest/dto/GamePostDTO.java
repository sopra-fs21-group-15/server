package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.ArrayList;

public class GamePostDTO {

    private Long id;
    private ArrayList<User> players;
    private String gameName;
    private GameModes gameModes;
    private int numberOfRounds;
    private int timePerRound;

    // access id
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // access players
    public ArrayList<User> getPlayers() { return players; }
    public void setPlayers(ArrayList<User> players) { this.players = players; }

    // access game name
    public String getGameName() {
        return this.gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    // access game modes
    public GameModes getGameModes() { return gameModes; }
    public void setGameModes(GameModes gameModes) { this.gameModes = gameModes; }

    // access number of rounds
    public int getNumberOfRounds() { return numberOfRounds; }
    public void setNumberOfRounds(int numberOfRounds) { this.numberOfRounds = numberOfRounds; }

    // access time per round
    public int getTimePerRound() { return timePerRound; }
    public void setTimePerRound(int timePerRound) { this.timePerRound = timePerRound; }

}
