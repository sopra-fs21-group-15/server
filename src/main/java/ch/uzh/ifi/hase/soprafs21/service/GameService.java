package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ch.uzh.ifi.hase.soprafs21.repository.*;

/**
 * Game Service
 * This class is responsible for all requests regarding the game itself
 * (e.g., it creates, modifies and generates rounds). The result will
 * be passed back to the caller.
 */
@Service
@Transactional
public class GameService implements Runnable {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;
    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;

    private final RoundService roundService;
    private final TimerService timerService;
    private final ScoreBoardService scoreBoardService;

    private List<Game> gamesToBeRun = new ArrayList<Game>();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, LobbyRepository lobbyRepository, UserRepository userRepository, RoundService roundService, TimerService timerService, ScoreBoardService scoreBoardService) {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.roundService = roundService;
        this.timerService = timerService;
        this.scoreBoardService = scoreBoardService;
    }

    /** Huge method to create a game from the lobby id given to us. All the information should be stored and
     * available over there. First we do a safety check to make sure there are enough players but afterwards
     * we can go ahead and create the game.
     *
     * @param lobby = the lobby from where the owner (user) started the game
     * @return the game the owner (user) asked for with the provided information
     */
    public Long createGame(Lobby lobby) {
        // check if the lobby has enough players to play a game
        String notEnoughPlayer = "The lobby you provided does not have enough players. Please add more players and try again.";
        if (lobby.getMembers().size() < new Standard().getMinNumOfPlayers()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(notEnoughPlayer));
        }

        // change status of said lobby
        lobby.setStatus(LobbyStatus.PLAYING);

        // save into the repository
        lobbyRepository.save(lobby);
        lobbyRepository.flush();

        //--------------------------- create the new game -------------------------//

        Game newGame = new Game();

        // import information from lobby
        newGame.setPlayers(lobby.getMembers());
        newGame.setNumberOfRounds(lobby.getRounds());
        newGame.setGameName(lobby.getLobbyname());
        newGame.setId(lobby.getId());
        newGame.setGameModes(lobby.getGameMode());

        // generate Objects from lobby information
        int timePerRound = lobby.getTimer().intValue();
        Timer timer = timerService.createTimer(timePerRound);
        newGame.setTimePerRound(timePerRound);
        newGame.setTimer(timer);

        // the scoreboard
        ScoreBoard scoreBoard = scoreBoardService.createScoreBoard(lobby);
        newGame.setScoreBoard(scoreBoard);

        // general information
        newGame.setRoundTracker(0);

         // saves the given entity but data is only persisted in the database once flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        // create a separate thread that runs the game in the background
        synchronized (gamesToBeRun) {
            gamesToBeRun.add(newGame);
            Thread t = new Thread(this);
            t.start();
            //gamesToBeRun.remove(newGame);
        }

        log.debug("Created and started new game with given information: {}", newGame);
        return newGame.getId();
    }

    // add points for a correct answer
    public void addPoints(Game game, Message message) {
        // get the timer
        Timer timer = game.getTimer();

        // get the local time from the message
        int offSet = new Standard().getConvertToLocalTimeOffSet();
        String part1 = message.getTimeStamp().substring(offSet,offSet+8); // hours, minute and seconds
        String part2 = message.getTimeStamp().substring(offSet+8,offSet+12).replace(":","."); // milliseconds
        String part3 = "000000"; // added zeros for nanoseconds
        //LocalTime time = LocalTime.parse(part1 + part2 + part3); // transforming into LocalTime object
        LocalTime time = LocalTime.now(); // used whenever we are testing

        // pass it to the service
        int points = timerService.remainingTime(timer,time);
        int relPoints = (points / (timer.getDrawingTimeSpan() * 10) ) ; // makes a relative scale from 0 - 100, like percent
        scoreBoardService.addPoints(game.getScoreBoard(), message.getWriterName(), relPoints);
        Round round = roundService.getRound(game.getRoundId());
        roundService.addPoints(round, message.getWriterName(), relPoints);
    }

    // quality of life method (logging in again after disconnect)
    public Game getGame(Long gameId) {
        Optional<Game> potGame = gameRepository.findById(gameId);
        Game value = null;

        if (potGame.isEmpty()) { // if not found
            String nonExistingGame = "This game does not exist or has expired. Please search for an existing game.";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingGame));
        } else { // if found
            value = potGame.get();
        }

        return value;
    }
/*
    // quality of life method (logging in again after disconnect)
    public Game getGameFromLobbyId(Long lobbyId) {
        Optional<Game> potGame = gameRepository.findById(lobbyId);
        Game value = null;

        if (potGame.isEmpty()) { // if not found
            String nonStartedGame = "This lobby has not created a game yet. Please initiate the game before trying to access it.";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonStartedGame));
        } else { // if found
            value = potGame.get();
        }

        return value;
    }
*/
    // start the timer for this phase
    public int startPhase(Game game) {
        Timer timer = game.getTimer();
        timerService.begin(timer);
        return timerService.remainingTime(timer);
    }

    // end this phase and
    public void endPhase(Game game) {
        Timer timer = game.getTimer();
        timerService.reset(timer);
        timerService.changePhase(timer);
    }

    // (Issue #52 Part I) function to handle when a user has made a guess (should also #56)
    /*public boolean makeGuess(Message message, Game game) {
        if (round.getStatus().equals(SELECTING)) { // check if the phase of the round is correct
            String notCorrectPhase = "This round is currently in selecting. Wait for the phase to end before getting information about the word.";
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(notCorrectPhase));
        } else { // get the current length of the word
            return round.getWord().length();
        }

        boolean

        if(isNotPainter && isRightWord) {
            return true;
        } else {
            return false;
        }
    }*/

    /** core method, this method runs the game in the background
     *
     */
    @Override
    public void run() {
        Game game = gamesToBeRun.get(0);
        int i = 0, n = game.getNumberOfRounds(); // index and total number of rounds
        int h = 0, m = game.getPlayers().size(); // index and total number of players
        int waitingTime;
        Round round;

        while(i < n) { // for each round
            h = 0;
            round = roundService.createRound(game);
            game.setRoundTracker(i);
            while(h < m) { // for each player
                // pick a new drawer and select
                roundService.setNewPainter(round);
                roundService.setRoundIndex(round,h);
                roundService.resetChoice(round);
                roundService.changePhase(round);
                waitingTime = startPhase(game);
                // wait for drawer to chose a word
                try {
                    TimeUnit.MILLISECONDS.sleep(waitingTime);
                } catch (InterruptedException e) {
                        // needs to be implemented -> player has chosen a word before the timer ran out
                }
                // select word drawer pick or pick one yourself
                endPhase(game);
                round = roundService.getRound(round.getId()); // get the latest changes for the round
                if (round.getWord() == null) {
                    roundService.makeChoiceForUser(round);
                }
                // let players draw and guess the word
                roundService.changePhase(round);
                waitingTime = startPhase(game);
                try {
                    TimeUnit.MILLISECONDS.sleep(waitingTime);
                } catch (InterruptedException e) {
                    // needs to be implemented -> all player have guessed the word correctly before the timer ran out
                }
                // finish this round, pass the results
                endPhase(game);
                round = roundService.getRound(game.getRoundId());
                game = getGame(game.getId());

                int painterPoints = roundService.computeRewardPainter(round);

                scoreBoardService.addPoints(game.getScoreBoard(),round.getDrawerName(),painterPoints);
                roundService.resetHasGuessed(round);
                roundService.resetGotPoints(round);
                h++;
            }
            i++;
        }

    }
}
