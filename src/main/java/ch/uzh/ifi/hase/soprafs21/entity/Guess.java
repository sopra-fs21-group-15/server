package ch.uzh.ifi.hase.soprafs21.entity;

public class Guess {

    private String guesserName;
    private String guess;

    // methods to access the id of the guesser
    public String getGuesserName() { return guesserName; }
    public void setGuesser_id(String guesserName) { this.guesserName = guesserName; }

    // methods to access guess
    public String getGuess() { return guess; }
    public void setGuess(String guess) { this.guess = guess; }

}
