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
    private long timePerRound;

    // generated values in the back-end
    private int roundTracker;

    private ArrayList<Round> roundOverview;

    private ScoreBoard scoreBoard;

    // TODO: Revert changes for custom made game mapper (not constructor)
    // class generator
    public Game(ArrayList<User> players, GameModes gameModes, int numberOfRounds, int timePerRound){
        this.players = players;
        this.gameModes = gameModes;
        this.numberOfRounds = numberOfRounds;
        this.timePerRound = timePerRound;
        this.roundTracker = 0;
    }

    // access Id
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // access Players (User)
    public ArrayList<User> getPlayers() {
        return players;
    }
    public void setPlayers(ArrayList<User> players) {
        this.players = players;
    }

    // access Number of Rounds
    public int getNumberOfRounds() {
        return numberOfRounds;
    }
    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    // access current Round
    public int getCurrentRound() { return roundTracker; }
    public void setCurrentRound(int currentRound) {
        this.roundTracker = currentRound;
    }



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
