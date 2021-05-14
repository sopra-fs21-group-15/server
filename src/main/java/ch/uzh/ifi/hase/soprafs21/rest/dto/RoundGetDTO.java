package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;

import java.util.ArrayList;

public class RoundGetDTO {

    private Long id;
    private RoundStatus status = RoundStatus.DRAWING;
    private ArrayList<String> players = new ArrayList<String>();
    private ArrayList<String> words;
    private int index;
    private boolean[] hasDrawn;
    private int[] hasGuessed;
    private String drawerName;
    private String word;

    // generic getter and setter methods for the mapper
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public RoundStatus getStatus() { return this.status; }
    public void setStatus(RoundStatus status) { this.status = status; }

    public ArrayList<String> getPlayers() { return this.players; }
    public void setPlayers(ArrayList<String> players) { this.players = players; }

    public ArrayList<String> getWords() { return this.words; }
    public void setWords(ArrayList<String> newWords) {this.words = newWords; }

    public int getIndex() { return this.index; }
    public void setIndex(int index) { this.index = index; }

    public boolean[] getHasDrawn() { return this.hasDrawn; }
    public void setHasDrawn(boolean[] newHasDrawn) { this.hasDrawn = newHasDrawn; }

    public int[] getHasGuessed() { return this.hasGuessed; }
    public void setHasGuessed(int[] hasGuessed) { this.hasGuessed = hasGuessed; }

    public String getDrawerName() { return this.drawerName; }
    public void setDrawerName(String newDrawerName) { this.drawerName = newDrawerName; }

    public String getWord() { return this.word; }
    public void setWord(String word) { this.word = word; }

}
