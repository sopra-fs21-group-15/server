package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.uzh.ifi.hase.soprafs21.repository.*;

/**
 * Game Service
 * This class is responsible for all requests regarding the game itself
 * (e.g., it creates, modifies and generates rounds). The result will
 * be passed back to the caller.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    private final LobbyRepository lobbyRepository;

    private final UserRepository userRepository;

    private final RoundService roundService;

    private final TimerService timerService;

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
    public Game createGame(Lobby lobby) {
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

        // ... the roundTracker
        newGame.setRoundTracker(0);

         // saves the given entity but data is only persisted in the database once flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created and started new game with given information: {}", newGame);
        return newGame;
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

    /** core method, this method runs the game in the background
     *
     */
    public void runGame(Game game) {
        int i = 0, n = game.getNumberOfRounds(); // index and total number of rounds
        int h = 0, m = game.getPlayers().size(); // index and total number of players
        Round round;

        while(i < n) { // for each round
            game.setRoundTracker(i);
            round = roundService.createRound(game);

            while(h < m) { // for each player
                roundService.setNewPainter(round);
                round.setIndex(h);

                h++;
            }

            i++;
        }

    }
}
