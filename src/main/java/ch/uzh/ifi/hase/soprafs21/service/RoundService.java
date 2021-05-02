package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
@Transactional
public class RoundService {

    private final GameRepository gameRepository;

    private final RoundRepository roundRepository;

    @Autowired
    public RoundService(@Qualifier("roundRepository") RoundRepository roundRepository, GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
    }

    // get all the games
    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }

    // get a specific game
    public Game getGame(Long gameId) {
        List<Game> games = getGames();

        Game gameFound = null;
        for (Game i : games) {
            if (gameId.equals(i.getId())) {
                gameFound = i;
            }
        }

        return gameFound;
    }

    // get all rounds
    public List<Round> getRounds() {
        return this.roundRepository.findAll();
    }

    // get a specific round
    public Round getRound(Long roundId) {
        List<Round> rounds = getRounds();

        Round roundFound = null;
        for (Round i : rounds) {
            if (roundId.equals(i.getId())) {
                roundFound = i;
            }
        }

        return roundFound;
    }


    /**
     * Here starts the important part. This are the methods that implement the functionalities needed in the back-end
     * to make the game work.
     */
    // a method to create a new round
    public Round createRound(Long gameId) {

        // find the game this round belongs to and create an empty object
        Game game = getGame(gameId);
        Round round = new Round();
        int n = game.getPlayers().size();

        round.setPictureId(404L);
        round.setStopWatch(new Timer(game.getTimePerRound()));
        round.setPlayers(game.getPlayers());

        // generate words for this round
        ArrayList<String> words = new ArrayList<String>();
        Words wordGenerator = new Words();
        for (int i = 0; i < n; i++) {
            words.add(wordGenerator.getRandomWord());
        }
        round.setWords(words);

        // continue with setting the fields in order
        round.setIndex(1);
        round.setScore( new long[n] );

        // special randomised initialization to get a random drawer each time
        Random rand = new Random();
        int r = rand.nextInt(n);
        round.setDrawerName(round.getPlayers().get(r));

        // set the drawing array straight
        boolean[] hasDrawn = new boolean[n];
        ArrayList<String> players = round.getPlayers();
        for(int i = 0; i < n; i++) {
            if(players.get(i).equals(round.getDrawerName())) {
                hasDrawn[i] = true;
            }
        }
        round.setHasDrawn(hasDrawn);

        // last but not least safe it in the repository
        round = roundRepository.save(round);
        roundRepository.flush();

        // finally return the created round
        return round;

    }

    // TODO *41 see if function handling is up to the standarts
    // drawer has drawn (automatically distinguishes between first time drawing and subsequent strokes)
    // public void addStroke(long idOfDrawer, BrushStroke brushStroke) {
    /*public void addStroke(String nameOfDrawer, BrushStroke brushStroke) {
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
    /*private void setNewPainter() {
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
    }*/
}
