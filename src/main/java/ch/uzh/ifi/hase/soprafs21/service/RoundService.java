package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.repository.DrawingRepository;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class RoundService {

    private final GameRepository gameRepository;

    private final RoundRepository roundRepository;

    private final DrawingRepository drawingRepository;

    private final UserRepository userRepository;

    @Autowired
    public RoundService(@Qualifier("roundRepository") RoundRepository roundRepository, GameRepository gameRepository, DrawingRepository drawingRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
        this.drawingRepository = drawingRepository;
        this.userRepository = userRepository;
    }

    // get a specific round quality of life
    public Round getRound(Long roundId) {
        Optional<Round> potRound = roundRepository.findById(roundId);
        Round value = null;

        if (potRound.isEmpty()) { // if not found
            String nonExistingRound = "This round does not exist, has expired or has not been initialized yet.";
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingRound));
        } else { // if found
            value = potRound.get();
        }

        return value;
    }


    /**
     * Here starts the important part. This are the methods that implement the functionalities needed in the back-end
     * to make the game work.
     */
    // a method to create a new round
    public Round createRound(Game game) {

        // find the game this round belongs to and create an empty object
        Round round = new Round();
        int n = game.getPlayers().size();

        // prepare multiple drawings for this round
        List<Drawing> drawings = new ArrayList<Drawing>();
        Drawing tempDrawing;
        for(int i = 0; i < n; i++) {
            tempDrawing = new Drawing();
            tempDrawing = drawingRepository.save(tempDrawing);
            drawings.add(tempDrawing);
        }
        drawingRepository.flush(); // safe the drawings in the repository
        round.setDrawings(drawings); // safe the list of drawings within the round

        // get all the players (might get updated)
        round.setPlayers(game.getPlayers());

        // generate words for this round
        ArrayList<String> words = new ArrayList<String>();
        Words wordGenerator = new Words();
        int choices = new Standard().getNumberOfChoices();
        for (int i = 0; i < n * choices; i++) {
            words.add(wordGenerator.getRandomWord());
        }
        round.setWords(words);

        // continue with setting the fields in order
        round.setIndex(0);
        round.setHasGuessed( new int[n] );
        round.setHasDrawn( new boolean[n] );

        // special randomised initialization to get a random drawer each time
        Random rand = new Random();
        int r = rand.nextInt(n);
        round.setDrawerName(round.getPlayers().get(r));

        // just now we selected a drawer, hence we need to remember who we pick so that he does not draw again in this round
        ArrayList<String> usernames = round.getPlayers();
        for(int i = 0; i < n; i++) {
            if(usernames.get(i).equals(round.getDrawerName())) {
                round.getHasDrawn()[i] = true;
            }
        }

        //List<User> bla = new LinkedList<User>();

        //bla.
        // last but not least safe it in the repository
        round = roundRepository.save(round);
        roundRepository.flush();

        // finally return the created round
        return round;

    }

    // helper method to find a new painter while respecting who has already drawn
    public void setNewPainter(Round round) {
        // get a random player
        Random rand = new Random();

        int n = round.getPlayers().size();
        int i = rand.nextInt(n), sign = -1, distance = 0;

        // if player has already drawn systematically pick an acceptable drawer
        int value = (i + sign * distance) % n;
        while(round.getHasDrawn()[value]) {
            if(sign == -1) {
                distance++;
            }
            sign *= -1;
            value = (i + sign * distance) % n;
        }

        // set the new drawer
        String newDrawerName = round.getPlayers().get(value);
        round.setDrawerName(newDrawerName);

        // save in the array that he/she can not be picked again
        boolean[] newHasDrawn = round.getHasDrawn();
        newHasDrawn[value] = true;
        round.setHasDrawn(newHasDrawn);
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


}
