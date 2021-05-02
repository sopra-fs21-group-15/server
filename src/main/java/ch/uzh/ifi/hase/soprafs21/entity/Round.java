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
    private Game gameId;

    @Column(nullable = false)
    private Timer stopWatch;

    // private ArrayList<User> players;
    @Column(nullable = false)
    private ArrayList<String> players;


    private ArrayList<Drawing> pictures;


    private ArrayList<String> words;

    @Column(nullable = false)
    private int currentWord; // works as an index for both words and pictures
    private boolean[] hasDrawn;
    private long[][] tempScore;
    //private long drawerId;
    private String drawerName;

    // generic getter and setter methods for the mapper
    //public Game getPartOf() { return this.partOf; }
    //public void setPartOf(Game newGame) { this.partOf = newGame; }

    public Timer getStopWatch() { return this.stopWatch; }
    public void setStopWatch(Timer newStopWatch) { this.stopWatch = newStopWatch; }

    //public ArrayList<User> getPlayers() { return this.players; }
    public ArrayList<String> getPlayers() { return this.players; }
    //public void setPlayers(ArrayList<User> newPlayers) { this.players = newPlayers; }
    public void setPlayers(ArrayList<String> newPlayers) { this.players = newPlayers; }

    public ArrayList<Drawing> getPictures() { return this.pictures; }
    public void setPictures(ArrayList<Drawing> newPictures) { this.pictures = newPictures; }

    public ArrayList<String> getWords() { return this.words; }
    public void setWords(ArrayList<String> newWords) {this.words = newWords; }

    public int getCurrentWord() { return this.currentWord; }
    public void setCurrentWord(int nextCurrentWord) { this.currentWord = nextCurrentWord; }

    public boolean[] getHasDrawn() { return this.hasDrawn; }
    public void setHasDrawn(boolean[] newHasDrawn) { this.hasDrawn = newHasDrawn; }

    public long[][] tempScore() { return this.tempScore; }
    public void setTempScore(long[][] newTempScore) { this.tempScore = newTempScore; }

    //public long getDrawerId() { return this.drawerId; }
    public String getDrawerName() { return this.drawerName; }
    //public void setDrawerId(long newDrawerId) { this.drawerId = newDrawerId; }
    public void setDrawerName(String newDrawerName) { this.drawerName = newDrawerName; }

    /**
     * Here starts the important part. This are the methods that implement the functionalities needed in the back-end
     * to make the game work.
     */

    // a setup method to initialize everything because I could not make a
    // constructor without breaking the auto-generated mapper functions
    public void setup(Game caller) {

        // Things given by the caller
        //this.partOf = caller;
        this.stopWatch = new Timer(caller.getTimePerRound());
        this.players = caller.getPlayers();

        // self generated objects and fields
        this.pictures = new ArrayList<Drawing>();
        this.currentWord = 1;
        this.hasDrawn = new boolean[players.size()];
        this.tempScore = new long[players.size()][players.size()];

        // special randomised initialization
        Random rand = new Random();
        int r = rand.nextInt(players.size());
        this.drawerName = players.get(r);
        this.hasDrawn[r] = true;
        // TODO: part 2 of the above mentioned task, use it to get good words
        this.words = new ArrayList<String>();
        Words listOfWords = new Words();
        for(int i = 0; i<players.size(); i++) {
            words.add(listOfWords.getRandomWord());
        }

    }

    // TODO *41 see if function handling is up to the standarts
    // drawer has drawn (automatically distinguishes between first time drawing and subsequent strokes)
    // public void addStroke(long idOfDrawer, BrushStroke brushStroke) {
    public void addStroke(String nameOfDrawer, BrushStroke brushStroke) {
        // safety-check if it is coming from the right person
        // if(idOfDrawer == drawerId) {
        if(nameOfDrawer == drawerName) {
            // now check if it is the first time this person is sending
            if(pictures.size() < currentWord) { // if the index is bigger the drawing record then the drawing has not been created yet
                pictures.add(new Drawing());
                stopWatch.reset(); // prepare the timer and then start it
                if(stopWatch.ready()) {
                    stopWatch.begin();
                }
            }
            // if the drawing has been set up we can pass the inputs down to the drawing
            if(pictures.size() == currentWord && !stopWatch.timeIsUp()) { // the current picture is being guest and changed
                pictures.get(currentWord - 1).addStroke(brushStroke);
            }
        }
    }

    // TODO: #43 add function to send drawing information to the guessers
    public Drawing getDrawing(LocalDateTime timeStamp) {
        return pictures.get(currentWord-1).getDrawing(timeStamp);
    }

    // TODO: #52 and #56 improve functionality and consistency
    // a guesser has made a guess
    public void makeGuess(Guess guess) {
        long potPoint = (long) stopWatch.remainingTime() * 1000;
        // check if: correct person, guess is correct, timer still running, has not made a correct guess before
        // if(guess.getGuesser_id() != drawerId && guess.getGuess().equals(words.get(currentWord-1)) && !stopWatch.timeIsUp()) {
        if(guess.getGuesserName() != drawerName && guess.getGuess().equals(words.get(currentWord-1)) && !stopWatch.timeIsUp()) {
            //for(User potGuesser : players) { //TODO: inefficient way to find wanted user within lobby (change/optimize if enough time left)
            for(String potGuesser : players) {
                //if(potGuesser.getId() == guess.getGuesser_id()) { // if it is the correct user ...
                if(potGuesser == guess.getGuesserName()) { // if it is the correct user ...
                    int i = players.indexOf(potGuesser);
                    int h = currentWord - 1;
                    tempScore[i][h] = Math.max(tempScore[i][h], potPoint); // ... check if he/she already made a better guess and then update accordingly
                }
            }
        }
    }

    // TODO #45 check if the function works properly
    public int getLength() {
        return words.get(currentWord - 1).length();
    }

    // round has a turning point with new roles or it finishes
    /*public void check() {
        // check if the timer indeed has run out and we have to ...
        if (stopWatch.timeIsUp()) {
            if(currentWord == hasDrawn.length + 1) { // ... finish the round and distribute the points
                long[] sumRound = new long[players.size()];
                int i = 0, h = 0;
                while(i < players.size()) {
                    h = 0;
                    while(h < players.size()) {
                        sumRound[h] += tempScore[h][i];
                        h++;
                    }
                }
                this.partOf.updatePoints(sumRound);
            } else { // ... a new painter
                setNewPainter();
                currentWord++;
            }
        }

    }*/

    // helper method to find a new painter
    private void setNewPainter() {
        // get a random player
        Random rand = new Random();

        int n = players.size();
        int i = rand.nextInt(n), sign = -1, distance = 0;

        // if player has already drawn systematically pick an acceptable drawer
        int value = (i + sign * distance) % n;
        while(hasDrawn[value]) {
            if(sign == -1) {
                distance++;
            }
            sign *= -1;
            value = (i + sign * distance) % n;
        }

        // set the new drawer
        //this.drawerId = players.get(value).getId();
        this.drawerName = players.get(value);
        hasDrawn[value] = true;
    }

}

