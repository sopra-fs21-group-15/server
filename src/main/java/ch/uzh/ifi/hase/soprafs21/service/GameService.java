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


    /** Huge method to create a game from the lobby id given to us. All the information should be stored and
     * available over there. First we do a safety check to make sure there are enough players but afterwards
     * we can go ahead and create the game.
     *
     * @param lobbyId = the lobby from where the owner (user) started the game
     * @return the game the owner (user) asked for with the provided information
     */
    public Game createGame(Long lobbyId) {

        // find the right lobby
        Lobby lobbyToUpdate = null;
        List<Lobby> allLobbies = lobbyRepository.findAll();

        for (Lobby i : allLobbies) {
            if(i.getId().equals(lobbyId)) {
                lobbyToUpdate = i;
            }
        }

        // check if the lobby has enough players to play a game
        String notEnoughPlayer = "The lobby you provided does not have enough players. Please add more players and try again.";
        if (lobbyToUpdate.getMembers().size() < new Standard().getMinNumOfPlayers()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(notEnoughPlayer));
        }

        // change status of said lobby
        lobbyToUpdate.setStatus(LobbyStatus.PLAYING);
        // save into the repository
        lobbyRepository.save(lobbyToUpdate);
        lobbyRepository.flush();

        //--------------------------- create the new game -------------------------//

        Game newGame = new Game();

        // import information from lobby
        ArrayList<String> players = new ArrayList<String>();
        for (String memberName : lobbyToUpdate.getMembers()) {
            // find the user and change his/her status to in game
            User tempUser = this.userRepository.findByUsername(memberName);
            tempUser.setStatus(UserStatus.INGAME);
            players.add(memberName);
            // save changes into the repository
            userRepository.save(tempUser);
            userRepository.flush();
        }
        // save players in the game
        newGame.setPlayers(players);

        // initialize the remaining fields and there corresponding fields ...
        // ... rounds to numberOfRounds
        newGame.setNumberOfRounds(lobbyToUpdate.getRounds());

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
    public Game getGame(Long gameId) {

        //get all games
        List<Game> allGames = this.gameRepository.findAll();

        Game game_found = null;

        for (Game i : allGames) {
            if ( gameId.equals(i.getId()) ) {
                game_found = i;
            }
        }

        //if not found
        String nonexisting_game = "This game does not exist or has expired. Please search for an existing game!";
        if (game_found == null) {
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_game));
        }

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
