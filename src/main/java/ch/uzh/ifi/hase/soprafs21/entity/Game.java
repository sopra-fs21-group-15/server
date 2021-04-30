package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;

public class Game {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    // passed values from front-end
    @Column(nullable = false)
    private ArrayList<User> players;

    @Column(nullable = false)
    private GameModes gameModes;

    @Column(nullable = false)
    private int numberOfRounds;

    @Column(nullable = false)
    private int timePerRound;

    // generated values in the back-end
    private int roundTracker;

    private ScoreBoard scoreBoard;

    private ArrayList<Round> rounds;

    /**
     * Basic getter and setter methods for the mapper (needed for front-end)
     */
    // access Id
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // access Players (User)
    public ArrayList<User> getPlayers() {
        return this.players;
    }
    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    // access GameMode
    public GameModes getGameMode() { return this.gameModes; }
    public void setGameMode(GameModes newMode) { this.gameModes = newMode; }

    // access Number of Rounds
    public int getNumberOfRounds() {
        return this.numberOfRounds;
    }
    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    // access time per Round
    public int getTimePerRound() { return this.timePerRound; }
    public void setTimePerRound(int newTimePerRound) { this.timePerRound = newTimePerRound; }

    // access current Round
    public int getCurrentRound() { return this.roundTracker; }
    public void setCurrentRound(int currentRound) {
        this.roundTracker = currentRound;
    }

    // access Scoreboard
    public ScoreBoard getScoreBoard() { return this.scoreBoard; }
    public void setScoreBoard(ScoreBoard newScoreBoard) { this.scoreBoard = newScoreBoard; }

    // access Rounds
    public ArrayList<Round> getRounds() { return this.rounds; }
    public void setRounds (ArrayList<Round> rounds) { this.rounds = rounds; }
    /**
     * Back-end specific methods needed for functionality
     */
    public void updatePoints(long[] points) {
        scoreBoard.updateScore(points);
    }

    /**
     * Back-end specific methods for quality of life
     */
    // a simple standardized method to make debugging easier
    public String toString() {
        String value = "";
        // get game Id
        value += "GameId =  " + getId() +"\n";
        // get a list of all the player names
        value += "Players = [ ";
        for(int i = 0; i<players.size(); i++){
            value += "" + players.get(i).getUsername() + "";
            if(i<players.size()-1){ // not last player
                value += ",";
            }
        }
        value += " ]\n";
        // get the gameMode that is being played
        value += "GameMode = ";
        switch (gameModes) {
            case CLASSIC:
                value += "CLASSIC\n";
                break;
            default:
                value += "CLASSIC\n";
                break;
        }
        return value;
    }
}
