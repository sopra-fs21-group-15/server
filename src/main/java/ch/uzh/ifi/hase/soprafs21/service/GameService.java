package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
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

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, LobbyRepository lobbyRepository, UserRepository userRepository, RoundService roundService) {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.roundService = roundService;
    }

    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }


    // TODO #30 test and refine function handling the starting of a game
    // create a game with given parameters
    public Game createGame(Long lobbyId) {


        List<Lobby> allLobbies = this.lobbyRepository.findAll();

        Lobby lobbyToUpdate = null;
        for (Lobby i : allLobbies) {
            if (lobbyId == i.getId()) {
                lobbyToUpdate = i;
            }
        }

        String nonexisting_lobby = "This lobby does not exist. Please search for an existing lobby!";
        if (lobbyToUpdate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_lobby));
        }

        // change status of said lobby
        lobbyToUpdate.setStatus(LobbyStatus.PLAYING);
        // save into the repository
        lobbyRepository.save(lobbyToUpdate);
        lobbyRepository.flush();

        //--------------------------- create the new game -------------------------//
        Game newGame = new Game();

        //System.out.println(newGame.toString());

        // import information from lobby
        // ArrayList<User> players = new ArrayList<User>();
        ArrayList<String> players = new ArrayList<String>();
        for (String memberName : lobbyToUpdate.getMembers()) {
            //System.out.print(lobbyToUpdate.getMembers().toString());
            //System.out.print(this.userRepository.findByUsername(memberName).getUsername());
            User tempUser = this.userRepository.findByUsername(memberName);
            tempUser.setStatus(UserStatus.INGAME);
            //players.add(tempUser);
            players.add(memberName);
            // save into the repository
            userRepository.save(tempUser);
            userRepository.flush();
        }
        newGame.setPlayers(players);

        //System.out.println(newGame.toString());

        // initialize the remaining fields and there corresponding fields ...
        // ... rounds to numberOfRounds
        newGame.setNumberOfRounds(lobbyToUpdate.getRounds());
        //System.out.println("NumberOfRounds worked");

        // ... LobbyName to GameName
        newGame.setGameName(lobbyToUpdate.getLobbyname());

        // ... timer to timerPerRound
        newGame.setTimePerRound(lobbyToUpdate.getTimer());

        // ... the scoreboard
        //ScoreBoard scoreBoard = new ScoreBoard(newGame.getPlayers());
        //newGame.setScoreBoard(scoreBoard);
        //System.out.println("Scoreboard worked");

        // ... the roundTracker
        newGame.setRoundTracker(1);

        // ... the link to the lobby
        newGame.setLobbyId(lobbyId);

        // ... the link to a round
        newGame.setRoundId(404L);

         // saves the given entity but data is only persisted in the database once flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        // ... the link to a proper round
        Round round = roundService.createRound(newGame.getId());
        newGame.setRoundId(round.getId());

        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created and started new game with given information: {}", newGame);
        return newGame;
    }

    // quality of life method (logging in again after disconnect)
    public Game getGame(Long game_id) {

        //get all games
        List<Game> all_games = this.gameRepository.findAll();

        long gameIdLong = game_id.longValue();
        long potGameIdLong = 0;
        Game game_found = null;

        for (Game i : all_games) {
            potGameIdLong = i.getId().longValue();
            if ( gameIdLong == potGameIdLong ) {
                game_found = i;
            }
        }

        //if not found
        String nonexisting_game = "This game does not exist or has expired. Please search for an existing user!";
        if (game_found == null) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_game));
        }

        //System.out.println(game_id.toString());
        return game_found;
    }

    // quality of life method (logging in again after disconnect)
    public Game getGameFromLobby(Long lobbyId) {

        //get all games
        List<Game> all_games = this.gameRepository.findAll();

        Game game_found = null;

        for (Game i : all_games) {
            if ( lobbyId.equals(i.getLobbyId()) ) {
                game_found = i;
            }
        }

        //if not found
        String nonexisting_game = "This game does not exist or has expired. Please search for an existing user!";
        if (game_found == null) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_game));
        }

        //System.out.println(game_id.toString());
        return game_found;
    }
}
