package ch.uzh.ifi.hase.soprafs21.entity;

public class Guess {

    //private Long guesser_id;
    private String guesserName;
    private String guess;

    // methods to access the id of the guesser
    //public Long getGuesser_id() { return guesser_id; }
    public String getGuesserName() { return guesserName; }
    //public void setGuesser_id(Long guesser_id) { this.guesser_id = guesser_id; }
    public void setGuesser_id(String guesserName) { this.guesserName = guesserName; }

    // methods to access guess
    public String getGuess() { return guess; }
    public void setGuess(String guess) { this.guess = guess; }

}
