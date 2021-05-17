package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.ScoreBoard;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional
public class ScoreBoardService {

    private final ScoreBoardRepository scoreBoardRepository;

    @Autowired
    public ScoreBoardService(@Qualifier("scoreBoardRepository") ScoreBoardRepository scoreBoardRepository) {
        this.scoreBoardRepository = scoreBoardRepository;
    }

    // get a specific scoreboard, quality of life
    public ScoreBoard getScoreBoard(Long scoreBoardId) {
        Optional<ScoreBoard> potScoreBoard = scoreBoardRepository.findById(scoreBoardId);
        ScoreBoard value = null;

        if (potScoreBoard.isEmpty()) {
            String nonExistingScoreBoard = "This scoreboard does not exist, has expired or has not been initialized yet.";
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingScoreBoard));
        } else { // if found
            value = potScoreBoard.get();
        }

        return value;
    }

    // the method to create a scoreboard
    public ScoreBoard createScoreBoard(Lobby lobby) {
        ScoreBoard scoreBoard = new ScoreBoard(lobby.getMembers());
        scoreBoardRepository.saveAndFlush(scoreBoard);
        return scoreBoard;
    }

    /**
     * Default methods that have to be made in order to be able
     * to transform this data type and send it to the front-end.
     */
    // functions for the player list (returns sorted by ranking)
    public ArrayList<String> getPlayers(ScoreBoard scoreBoard) {
        // since we do not just return our list but a sorted one we need to create a new one
        ArrayList<String> value = new ArrayList<String>();

        // inefficient but working loop to get the right order of the ranking
        for(int position = 0; position < scoreBoard.getPlayers().size(); position++) {
            for(int index = 0; index < scoreBoard.getPlayers().size(); index++) {
                if (scoreBoard.getRanking()[index] == position) {
                    value.add(scoreBoard.getPlayers().get(index));
                }
            }
        }
        return value;
    }

    // change the players in the scoreboard and make the fitting changes
    public void setPlayers(ScoreBoard scoreBoard, ArrayList<String> newPlayers) {
        scoreBoard.setPlayers(newPlayers);
        scoreBoard.setRanking(new int[newPlayers.size()]); // reset the ranking
        scoreBoard.setScore(new int[newPlayers.size()]); // and resetting the scores
        scoreBoardRepository.saveAndFlush(scoreBoard);
    }

    // functions for the ranking
    public int[] getRanking(ScoreBoard scoreBoard) {
        int[] sorted = Arrays.copyOf(scoreBoard.getRanking(), scoreBoard.getRanking().length);
        Arrays.sort(sorted);
        return sorted;
    }

    // set a new ranking for when the point distribution has changed
    public void setRanking(ScoreBoard scoreBoard, int[] newRanking) {
        // needs to have the same length as the previous ranking array
        if( newRanking.length == scoreBoard.getRanking().length ) {
            scoreBoard.setRanking(newRanking);
            scoreBoardRepository.saveAndFlush(scoreBoard);
        }
    }

    // functions for the scores
    public int[] getScore(ScoreBoard scoreBoard) {
        int[] sorted = Arrays.copyOf(scoreBoard.getScore(), scoreBoard.getScore().length);
        Arrays.sort(sorted);

        // reversing it
        int temp = 0;
        int n = sorted.length;
        for(int i = 0; i < n / 2; i++) {
            temp = sorted[i];
            sorted[i] = sorted[n - i - 1];
            sorted[n - i - 1] = temp;
        }
        return sorted;
    }

    // change the score itself
    public void setScore(ScoreBoard scoreBoard, int[] newScore) {
        if(newScore.length == scoreBoard.getScore().length) {
            scoreBoard.setScore(newScore);
            scoreBoardRepository.saveAndFlush(scoreBoard);
        }
    }

    /**
     * Methods and functionalities needed in the backend.
     */
    // update the scoreboard after a round has finished
    public void updateScore(ScoreBoard scoreBoard, int[] roundScore) {
        if(roundScore.length == scoreBoard.getScore().length) {
            scoreBoard.setScore(roundScore);
        }
        fixRanking(scoreBoard);
        scoreBoardRepository.saveAndFlush(scoreBoard);
    }

    // add points to a particular player
    public void addPoints(ScoreBoard scoreBoard, String username, int points) {
        int index = scoreBoard.getPlayers().indexOf(username);
        int[] temp = scoreBoard.getScore();
        temp[index] += points;
        scoreBoard.setScore(temp);
        fixRanking(scoreBoard);
        scoreBoardRepository.saveAndFlush(scoreBoard);
    }

    // fix ranking => changes the order within ranking after the changes to the score have been made
    private void fixRanking(ScoreBoard scoreBoard) {
        int[] sorted = Arrays.copyOf(scoreBoard.getScore(), scoreBoard.getScore().length);
        Arrays.sort(sorted);

        // double check if we have players with identical scores
        boolean[] used = new boolean[sorted.length];

        // iterate over every value to find the player it belongs to and then safe him/her in ranking
        int target = 0, index = sorted.length - 1, position = 0, counter = 0;

        while(position < sorted.length && index >= 0) {
            target = 0; // resetting target to the first player
            while(scoreBoard.getScore()[target] != sorted[index] || used[target]) {
                if(target < sorted.length - 1 ) {
                    target++;
                } else {
                    break; // should never happen but just in case so it does not result in an infinite loop
                }
            } // target is now set to the first player with the fitting score for this position

            counter = 0; // # of players at this position

            while(target < sorted.length) {
                if(scoreBoard.getScore()[target] == sorted[index]) {
                    scoreBoard.getRanking()[target] = position;
                    counter++;
                }
                target++; // check next player
            }

            position += counter;
            index -= counter;
        }

        scoreBoardRepository.saveAndFlush(scoreBoard);
    }


}
