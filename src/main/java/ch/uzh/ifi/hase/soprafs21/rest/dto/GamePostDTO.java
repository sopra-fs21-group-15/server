package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.ArrayList;

public class GamePostDTO {

    private ArrayList<User> players;
    private int numberOfRounds;
    private int timePerRound;

    // access players
    public ArrayList<User> getPlayers() { return players; }
    public void setPlayers(ArrayList<User> players) { this.players = players; }

    // access number of rounds
    public int getNumberOfRounds() { return numberOfRounds; }
    public void setNumberOfRounds(int numberOfRounds) { this.numberOfRounds = numberOfRounds; }

    // access time per round
    public int getTimePerRound() { return timePerRound; }
    public void setTimePerRound(int timePerRound) { this.timePerRound = timePerRound; }

}
