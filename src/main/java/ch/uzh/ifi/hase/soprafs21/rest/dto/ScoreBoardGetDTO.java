package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.ArrayList;

public class ScoreBoardGetDTO {

    private Long id;
    private ArrayList<User> players;
    private int[] ranking;
    private long[] score;

    // methods for id
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    // methods for players
    public ArrayList<User> getPlayers() { return players; }
    public void setPlayers(ArrayList<User> players) { this.players = players;}

    // methods for ranking
    public int[] getRanking() { return ranking; }
    public void setRanking(int[] ranking) { this.ranking = ranking; }

    // methods for score
    public long[] getScore() { return score; }
    public void setScore(long[] score) { this.score = score; }

}
