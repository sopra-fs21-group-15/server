package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "GAME")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    // passed values from front-end
    @Column(nullable = false)
    private ArrayList<String> players = new ArrayList<String>();

    @Column(nullable = false, unique = true)
    private String gameName;

    @Column(nullable = false)
    private GameModes gameModes = GameModes.CLASSIC;

    @Column(nullable = false)
    private int numberOfRounds;

    @Column(nullable = false)
    private int timePerRound;

    // generated values in the back-end

    @Column(nullable = false)
    private int roundTracker;

    @Column(nullable = false, unique = true)
    private Long lobbyId;

    @Column(nullable = true, unique = true)
    private Long roundId;

    @OneToOne
    private Timer timer;

    @OneToOne
    private ScoreBoard scoreBoard;

    //private ArrayList<Round> rounds = new ArrayList<>();
    // private Round rounds = new Round();

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
    public ArrayList<String> getPlayers() { return this.players; }
    public void setPlayers(ArrayList<String> players) { this.players = players; }

    // access game name
    public String getGameName() {
        return this.gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    // access GameMode
    public GameModes getGameModes() { return this.gameModes; }
    public void setGameModes(GameModes newMode) { this.gameModes = newMode; }

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
    public int getRoundTracker() { return this.roundTracker; }
    public void setRoundTracker(int roundTracker) {
        this.roundTracker = roundTracker;
    }

    // access Lobby
    public Long getLobbyId() { return this.lobbyId; }
    public void setLobbyId(Long lobbyId) { this.lobbyId = lobbyId; }

    // access Scoreboard
    public ScoreBoard getScoreBoard() { return this.scoreBoard; }
    public void setScoreBoard(ScoreBoard newScoreBoard) { this.scoreBoard = newScoreBoard; }

    // access Rounds
    // public ArrayList<Round> getRounds() { return this.rounds; }
    //public Round getRounds() { return this.rounds; }
    // public void setRounds (ArrayList<Round> rounds) { this.rounds = rounds; }
    //public void setRounds (Round rounds) { this.rounds = rounds; }

    // access the current round
    public Long getRoundId() { return this.roundId; }
    public void setRoundId(Long roundId) { this.roundId = roundId; }

    // access to the timer
    public Timer getTimer() { return this.timer; }
    public void setTimer(Timer timer) { this.timer = timer; }

    /**
     * Back-end specific methods needed for functionality
     */
    //public void updatePoints(long[] points) { scoreBoard.updateScore(points); }

    //public void addStroke(long user_id, BrushStroke brushStroke) { this.rounds.get(roundTracker - 1).addStroke(user_id, brushStroke); }
    // public void addStroke(String userName, BrushStroke brushStroke) { this.rounds.get(roundTracker - 1).addStroke(userName, brushStroke); }
    //public void addStroke(String userName, BrushStroke brushStroke) { this.rounds.addStroke(userName, brushStroke); }

    //public Drawing getDrawing(LocalDateTime timeStamp) { return rounds.get(roundTracker-1).getDrawing(timeStamp); }
    //public Drawing getDrawing(LocalDateTime timeStamp) { return rounds.getDrawing(timeStamp); }

    //public int getLength() { return rounds.get(roundTracker-1).getLength(); }
    // public int getLength() { return rounds.getLength(); }

    //public void makeGuess(Guess guess) { rounds.get(roundTracker-1).makeGuess(guess);}
    // public void makeGuess(Guess guess) { rounds.makeGuess(guess);}

    /**
     * Back-end specific methods for quality of life
     */
    // a simple standardized method to make debugging easier
    public String toString() {
        String value = "";
        // get game Id
        value += "GameId =  " + getId().intValue() +"\n";
        // get a list of all the player names
        value += "Players = [ ";
        for(int i = 0; i<players.size(); i++){
            //value += "" + players.get(i).getUsername() + "";
            value += "" + players.get(i) + "";
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
        value += "Rounds = " + roundTracker + "/" + numberOfRounds + "\n";
        if(this.roundId != null) {
            value += "RoundId = " + this.roundId.toString() + "\n";
        }
        return value;
    }
}
