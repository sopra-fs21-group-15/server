package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "SCOREBOARD")
public class ScoreBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private ArrayList<String> players;

    @Column(nullable = false)
    private int[] ranking;

    @Column(nullable = false)
    private int[] score;

    // constructor methods
    public ScoreBoard() {
        ArrayList<String> players = new ArrayList<String>(4);
        new ScoreBoard(players);
    }

    public ScoreBoard(ArrayList<String> players) {
        int n = players.size();
        this.players = players;
        this.ranking = new int[n];
        this.score = new int[n];
    }

    // basic getter and setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ArrayList<String> getPlayers() { return players; }
    public void setPlayers(ArrayList<String> players) { this.players = players; }

    public int[] getRanking() { return ranking; }
    public void setRanking(int[] ranking) { this.ranking = ranking; }

    public int[] getScore() { return score; }
    public void setScore(int[] score) { this.score = score; }

    // toString method to check if the implementation works
    public String toString() {
        String value = "This is the current Scoreboard:\n";
        value += "-------------------------------------------\n";
        value += "Position  | Player          | Points       \n";
        value += "-------------------------------------------\n";

        //ArrayList<User> sortPlayers = this.getPlayers();
        ArrayList<String> sortPlayers = this.getPlayers();
        int[] sortScore = this.getScore();
        int[] sortRanking = this.getRanking();

        for(int i = 0; i < players.size(); i++) {
            //value += "" + (sortRanking[i] + 1) + ".        | " + sortPlayers.get(i).getUsername();
            value += "" + (sortRanking[i] + 1) + ".        | " + sortPlayers.get(i);
            value += "\t\t| " + sortScore[i];
            value += "             \n";
        }

        return value;
    }

}
