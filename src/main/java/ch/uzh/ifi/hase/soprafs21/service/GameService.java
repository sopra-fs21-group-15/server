package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import ch.uzh.ifi.hase.soprafs21.repository.*;

import javax.persistence.criteria.CriteriaBuilder;

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

    private List<Game> gamesToBeRun = new ArrayList<Game>();

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, LobbyRepository lobbyRepository, UserRepository userRepository, RoundService roundService, TimerService timerService) {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.roundService = roundService;
        this.timerService = timerService;
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
        newGame.setLobbyId(lobby.getId());

        // generate Objects from lobby information
        int timePerRound = lobby.getTimer().intValue();
        Timer timer = timerService.createTimer(timePerRound);
        newGame.setTimePerRound(timePerRound);
        newGame.setTimer(timer);

        // ... the scoreboard
        //ScoreBoard scoreBoard = new ScoreBoard(newGame.getPlayers());
        //newGame.setScoreBoard(scoreBoard);
        //System.out.println("Scoreboard worked");

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

        System.out.println("other threads are finishing.");

        log.debug("Created and started new game with given information: {}", newGame);
        return newGame.getId();
    }

    // quality of life method (logging in again after disconnect)
    public Game getGame(Long gameId) {
        Optional<Game> potGame = gameRepository.findById(gameId);
        Game value = null;

        if (potGame.isEmpty()) { // if not found
            String nonExistingGame = "This game does not exist or has expired. Please search for an existing game.";
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingGame));
        } else { // if found
            value = potGame.get();
        }

        return value;
    }

    // quality of life method (logging in again after disconnect)
    public Game getGameFromLobbyId(Long lobbyId) {
        Optional<Game> potGame = gameRepository.findByLobbyId(lobbyId);
        Game value = null;

        if (potGame.isEmpty()) { // if not found
            String nonStartedGame = "This lobby has not created a game yet. Please initiate the game before trying to access it.";
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonStartedGame));
        } else { // if found
            value = potGame.get();
        }

        return value;
    }

    // start the timer for this phase
    public int startPhase(Game game) {
        Timer timer = game.getTimer();
        timerService.begin(timer);
        Round round = roundService.getRound(game.getRoundId());
        roundService.changePhase(round);
        return timerService.remainingTime(timer);
    }

    // end this phase and
    public void endPhase(Game game) {
        Timer timer = game.getTimer();
        timerService.reset(timer);
        timerService.changePhase(timer);
    }

    /** core method, this method runs the game in the background
     *
     */
    @Override
    public void run() {
        Game game = gamesToBeRun.get(0);
        int i = 0, n = game.getNumberOfRounds(); // index and total number of rounds
        int h = 0, m = game.getPlayers().size(); // index and total number of players
        int waitingTime;
        int selection = game.getTimer().getSelectTimeSpan() * 1000;
        int drawing = game.getTimer().getDrawingTimeSpan() * 1000;
        Round round;

        while(i < n) { // for each round
            h = 0;
            round = roundService.createRound(game);
            game.setRoundTracker(i);
            while(h < m) { // for each player
                // pick a new drawer and select
                roundService.setNewPainter(round);
                roundService.setRoundIndex(round,h);
                waitingTime = startPhase(game);
                // wait for drawer to chose a word
                try {
                    TimeUnit.MILLISECONDS.sleep(waitingTime);
                } catch (InterruptedException e) {
                        // needs to be implemented -> player has chosen a word before the timer ran out
                }
                // needs to be implemented -> select word drawer pick or pick one yourself
                endPhase(game);
                // let players draw and guess the word
                System.out.println("Should have changed phases");
                waitingTime = startPhase(game);
                try {
                    TimeUnit.MILLISECONDS.sleep(waitingTime);
                } catch (InterruptedException e) {
                    // needs to be implemented -> all player have guessed the word correctly before the timer ran out
                }
                // finish this round, pass the results
                endPhase(game);
                h++;
            }
            i++;
        }

    }
}
