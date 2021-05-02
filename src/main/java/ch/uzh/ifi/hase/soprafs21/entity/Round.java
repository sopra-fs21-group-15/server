package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

// external API call to generate a random word. not yet working
// TODO: part 1, make external API functional
// references: https://github.com/Dhiraj072/random-word-generator
// import com.github.dhiraj072.randomwordgenerator.RandomWordGenerator;

/**
 * Clarification how a round is supposed to roll out: At random a player is picked to be the drawer. Then that player
 * has given amount of time to draw starting the moment he/she made the first point. During this time the other player
 * get to guess. Only the first correct guess counts for each player. The sooner the guess the higher the reward.
 * At the end the drawer receives an average point of all the guesses. Then a new player that has not drawn yet is
 * chosen at random and it starts a new. Once everybody had his turn drawing the round ends, the points are summed up
 * and send to the game scoreboard. If it was the last round the game ends then and there, if it was not an other round
 * gets queued up.
 */

@Entity
@Table(name = "ROUND")
public class Round implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private Long pictureId;

    @Column(nullable = false)
    private Timer stopWatch;

    @Column(nullable = false)
    private ArrayList<String> players;

    @Column(nullable = false)
    private ArrayList<String> words;

    @Column(nullable = false)
    private int index;

    @Column(nullable = false)
    private boolean[] hasDrawn;

    @Column(nullable = false)
    private long[] score;

    @Column(nullable = false)
    private String drawerName;

    // generic getter and setter methods for the mapper
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public Long getPictureId() { return this.pictureId; }
    public void setPictureId(Long pictureId) { this.pictureId = pictureId; }

    public Timer getStopWatch() { return this.stopWatch; }
    public void setStopWatch(Timer newStopWatch) { this.stopWatch = newStopWatch; }

    public ArrayList<String> getPlayers() { return this.players; }
    public void setPlayers(ArrayList<String> newPlayers) { this.players = newPlayers; }

    public ArrayList<String> getWords() { return this.words; }
    public void setWords(ArrayList<String> newWords) {this.words = newWords; }

    public int getIndex() { return this.index; }
    public void setIndex(int index) { this.index = index; }

    public boolean[] getHasDrawn() { return this.hasDrawn; }
    public void setHasDrawn(boolean[] newHasDrawn) { this.hasDrawn = newHasDrawn; }

    public long[] score() { return this.score; }
    public void setScore(long[] score) { this.score = score; }

    public String getDrawerName() { return this.drawerName; }
    public void setDrawerName(String newDrawerName) { this.drawerName = newDrawerName; }

}

