package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private RoundStatus status = RoundStatus.DRAWING;

    @Column(nullable = true)
    private String endsAt;

    @OneToMany
    private List<Drawing> drawings = new ArrayList<Drawing>();

    @Column(nullable = false)
    private ArrayList<String> players = new ArrayList<String>();

    @Column(nullable = false)
    private ArrayList<String> words;

    @Column(nullable = true)
    private ArrayList<String> selection;

    @Column(nullable = false)
    private int index;

    @Column(nullable = false)
    private boolean[] hasDrawn;

    @Column(nullable = false)
    private boolean[] hasGuessed;

    @Column(nullable = false)
    private int[] gotPoints;

    @Column(nullable = true)
    private String drawerName;

    @Column(nullable = true)
    private String word;

    // generic getter and setter methods for the mapper
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public RoundStatus getStatus() { return this.status; }
    public void setStatus(RoundStatus status) { this.status = status; }

    public String getEndsAt() { return this.endsAt; }
    public void setEndsAt(String endsAt) { this.endsAt = endsAt; }

    public List<Drawing> getDrawings() { return this.drawings; }
    public void setDrawings(List<Drawing> drawings) { this.drawings = drawings; }

    public ArrayList<String> getPlayers() { return this.players; }
    public void setPlayers(ArrayList<String> players) { this.players = players; }

    public ArrayList<String> getWords() { return this.words; }
    public void setWords(ArrayList<String> newWords) {this.words = newWords; }

    public ArrayList<String> getSelection() { return this.selection; }
    public void setSelection(ArrayList<String> selection){ this.selection = selection; }

    public int getIndex() { return this.index; }
    public void setIndex(int index) { this.index = index; }

    public boolean[] getHasDrawn() { return this.hasDrawn; }
    public void setHasDrawn(boolean[] newHasDrawn) { this.hasDrawn = newHasDrawn; }

    public int[] getGotPoints() { return this.gotPoints; }
    public void setGotPoints(int[] gotPoints) { this.gotPoints = gotPoints; }

    public boolean[] getHasGuessed() { return this.hasGuessed; }
    public void setHasGuessed(boolean[] hasGuessed) { this.hasGuessed = hasGuessed; }

    public String getDrawerName() { return this.drawerName; }
    public void setDrawerName(String newDrawerName) { this.drawerName = newDrawerName; }

    public String getWord() { return this.word; }
    public void setWord(String word) { this.word = word; }

    // quality of life methods
    public Drawing getCurrentDrawing() { return getDrawings().get(index); }

}

