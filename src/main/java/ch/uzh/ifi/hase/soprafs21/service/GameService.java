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

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, LobbyRepository lobbyRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
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

        // import information from lobby
        ArrayList<User> players = new ArrayList<User>();
        for (String memberName : lobbyToUpdate.getMembers()) {
            System.out.print(lobbyToUpdate.getMembers());
            System.out.print(this.userRepository.findByUsername(memberName));
            User tempUser = this.userRepository.findByUsername(memberName);
            tempUser.setStatus(UserStatus.INGAME);
            players.add(tempUser);
            // save into the repository
            userRepository.save(tempUser);
            userRepository.flush();
        }
        newGame.setPlayers(players);

        // initialize the remaining fields and there corresponding fields ...
        // ... rounds to numberOfRounds
        newGame.setRoundTracker(lobbyToUpdate.getRounds());

        // ... timer to timerPerRound
        newGame.setTimePerRound(lobbyToUpdate.getTimer());

        // ... the scoreboard
        ScoreBoard scoreBoard = new ScoreBoard(newGame.getPlayers());
        newGame.setScoreBoard(scoreBoard);

        // ... the roundTracker
        newGame.setRoundTracker(1);

        // ... the rounds themselves
        int n = newGame.getPlayers().size();
        ArrayList<Round> rounds = new ArrayList<Round>(n);

        for (int i = 0; i < n; i++) {
            Round temp = new Round();
            temp.setup(newGame);
            rounds.add(temp);
        }
        newGame.setRounds(rounds); //

         // saves the given entity but data is only persisted in the database once flush() is called
        newGame = gameRepository.save(newGame);
        gameRepository.flush();

        log.debug("Created and started new game with given information: {}", newGame);
        return newGame;
    }

    // quality of life method (logging in again after disconnect)
    public Game getGame(Long game_id) {

        //get all games
        List<Game> all_games = this.gameRepository.findAll();

        Game game_found = null;
        for (Game i : all_games) {
            if (game_id == i.getId()) {
                game_found = i;
            }
        }

        //if not found
        String nonexisting_game = "This game does not exist or has expired. Please search for an existing user!";
        if (game_found == null) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_game));
        }

        return game_found;
    }
}
