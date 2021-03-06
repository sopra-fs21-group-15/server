package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;

import java.util.ArrayList;

public class RoundGetDTO {

    private Long id;

    private RoundStatus status = RoundStatus.DRAWING;
    private String endsAt;
    private ArrayList<String> players = new ArrayList<>();
    // private ArrayList<String> words;
    private ArrayList<String> selection;
    private int index;
    private boolean[] hasDrawn;
    private int[] gotPoints;
    private boolean[] hasGuessed;
    private String drawerName;
    private String word;

    // generic getter and setter methods for the mapper
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public RoundStatus getStatus() { return this.status; }
    public void setStatus(RoundStatus status) { this.status = status; }

    public String getEndsAt() { return this.endsAt; }
    public void setEndsAt(String endsAt) { this.endsAt = endsAt; }

    public ArrayList<String> getPlayers() { return this.players; }
    public void setPlayers(ArrayList<String> players) { this.players = players; }

    // public ArrayList<String> getWords() { return this.words; }
    // public void setWords(ArrayList<String> newWords) {this.words = newWords; }

    public ArrayList<String> getSelection() { return this.selection; }
    public void setSelection(ArrayList<String> selection) { this.selection = selection; }

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

}
