package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class GuessPutDTO {

    private Long guesser_id;
    private String guess;

    // methods to access the id of the guesser
    public Long getGuesser_id() { return guesser_id; }
    public void setGuesser_id(Long guesser_id) { this.guesser_id = guesser_id; }

    // methods to access guess
    public String getGuess() { return guess; }
    public void setGuess(String guess) { this.guess = guess; }

}
