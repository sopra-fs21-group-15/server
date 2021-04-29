package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoreBoard {

    private ArrayList<User> players;
    private int[] ranking;
    private long[] score;

    /**
     * Default methods that have to be made in order to be able
     * to transform this data type and send it to the front-end.
     */
    // functions for the player list (returns sorted by ranking)
    public ArrayList<User> getPlayers() {
        // since we do not just return our list but a sorted one we need to create a new one
        ArrayList<User> value = new ArrayList<User>();

        // inefficient but working loop to get the right order of the ranking
        for(int position = 0; position < players.size(); position++) {
            for(int index = 0; index < players.size(); index++) {
                if (ranking[index] == position) {
                    value.add(players.get(index));
                }
            }
        }
        return value;
    }

    public void setPlayers(ArrayList<User> newPlayers) {
        this.players = newPlayers;
        this.ranking = new int[newPlayers.size()]; // reset the ranking
        this.score = new long[newPlayers.size()]; // and resetting the scores
    }

    // functions for the ranking
    public int[] getRanking() {
        int[] sorted = Arrays.copyOf(ranking, ranking.length);
        Arrays.sort(sorted);
        return sorted;
    }

    public void setRanking(int[] newRanking) {

        // needs to have the same length as the previous ranking array
        if( newRanking.length == ranking.length ) {
            this.ranking = newRanking;
        }
    }

    // functions for the scores
    public long[] getScore() {
        long[] sorted = Arrays.copyOf(score, score.length);
        Arrays.sort(sorted);

        // reversing it
        long temp = 0;
        int n = sorted.length;
        for(int i = 0; i < n / 2; i++) {
            temp = sorted[i];
            sorted[i] = sorted[n - i - 1];
            sorted[n - i - 1] = temp;
        }
        return sorted;
    }

    public void setScore(long[] newScore) {
        if(newScore.length == this.score.length) {
            this.score = newScore;
        }
    }

    /**
     * Methods and functionalities needed in the backend.
     */
    // update the scoreboard after a round has finished
    public void updateScore(long[] roundScore) {
        if(roundScore.length == score.length) {
            for(int i = 0; i < roundScore.length; i++) {
                score[i] += roundScore[i];
            }
        }
        fixRanking();
    }

    // fix ranking => changes the order within ranking after the changes to the score have been made
    private void fixRanking() {
        long[] sorted = Arrays.copyOf(score, score.length);
        Arrays.sort(sorted);

        // double check if we have players with identical scores
        boolean[] used = new boolean[sorted.length];

        // iterate over every value to find the player it belongs to and then safe him/her in ranking
        int target = 0, index = sorted.length - 1, position = 0, counter = 0;

        while(position < sorted.length && index >= 0) {
            target = 0; // resetting target to the first player
            while(score[target] != sorted[index] || used[target]) {
                if(target < sorted.length - 1 ) {
                    target++;
                } else {
                    break; // should never happen but just in case so it does not result in an infinite loop
                }
            } // target is now set to the first player with the fitting score for this position

            counter = 0; // # of players at this position

            while(target < sorted.length) {
                if(score[target] == sorted[index]) {
                    ranking[target] = position;
                    counter++;
                }
                target++; // check next player
            }

            position += counter;
            index -= counter;
        }
    }

    // toString method to check if the implementation works
    public String toString() {
        String value = "This is the current Scoreboard:\n";
        value += "-------------------------------------------\n";
        value += "Position  | Player          | Points       \n";
        value += "-------------------------------------------\n";

        ArrayList<User> sortPlayers = this.getPlayers();
        long[] sortScore = this.getScore();
        int[] sortRanking = this.getRanking();

        for(int i = 0; i < players.size(); i++) {
            value += "" + (sortRanking[i] + 1) + ".        | " + sortPlayers.get(i).getUsername();
            value += "\t\t| " + sortScore[i];
            value += "             \n";
        }

        return value;
    }

}
