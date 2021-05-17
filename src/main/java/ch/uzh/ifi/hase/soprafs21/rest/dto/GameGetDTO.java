package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.entity.ScoreBoard;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.ArrayList;

public class GameGetDTO {

    private Long id;
    private ArrayList<String> players;
    // private GameModes gameModes;
    private int numberOfRounds;
    private int timePerRound;
    private int roundTracker;
    private Long roundId;
    //private ScoreBoard scoreBoard;

    // access id
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // access players
    public ArrayList<String> getPlayers() { return players; }
    public void setPlayers(ArrayList<String> players) { this.players = players; }

    // access game modes
    /*public GameModes getGameModes() { return gameModes; }
    public void setGameModes(GameModes gameModes) { this.gameModes = gameModes; }*/

    // access number of rounds
    public int getNumberOfRounds() { return numberOfRounds; }
    public void setNumberOfRounds(int numberOfRounds) { this.numberOfRounds = numberOfRounds; }

    // access time per round
    public int getTimePerRound() { return timePerRound; }
    public void setTimePerRound(int timePerRound) { this.timePerRound = timePerRound; }

    // access the tracker for the rounds
    public int getRoundTracker() { return roundTracker; }
    public void setRoundTracker(int roundTracker) { this.roundTracker = roundTracker; }

    // access the scoreboard
    // public ScoreBoard getScoreBoard() { return scoreBoard; }
    // public void setScoreBoard(ScoreBoard scoreBoard) { this.scoreBoard = scoreBoard; }

    public Long getRoundId() { return roundId; }
    public void setRoundId(Long roundId) { this.roundId = roundId; }

}
