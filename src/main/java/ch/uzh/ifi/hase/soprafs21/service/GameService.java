package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Round;
import ch.uzh.ifi.hase.soprafs21.entity.ScoreBoard;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
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
import java.util.UUID;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

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

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getGames() {
        return this.gameRepository.findAll();
    }


    // TODO #30 test and refine function handling the starting of a game
    // create a game with given parameters
    public Game createGame(Game newGame) {
        // initialize the remaining fields and there corresponding fields ...
        // ... the scoreboard
        ScoreBoard scoreBoard = new ScoreBoard(newGame.getPlayers(), newGame.getId());
        newGame.setScoreBoard(scoreBoard);

        // ... the roundTracker
        newGame.setCurrentRound(1);

        // ... the rounds themselves
        int n = newGame.getPlayers().size();
        ArrayList<Round> rounds = new ArrayList<Round>(n);

        for(int i = 0; i < n; i++) {
            Round temp = new Round();
            temp.setup(newGame);
            rounds.add(temp);
        }
        newGame.setRounds(rounds); //get the creation date with the current date

        // TODO: make the gameId unique even with in lobby calls
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


    /**
     * This is a helper method that will check the uniqueness criteria of the username and the password
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    /*
    // Check if the user Exists
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        //get entered password

        String entered_password = userToBeCreated.getPassword();

        String taken_username_error = "The %s %s already taken. Please choose an other username!";

        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(taken_username_error, "username", "is"));
        }
    }

    // Get the date for the creation date information.
    private String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    //Handle the log-in
    public User login_request(User requesting_user) {

        User userByUsername = userRepository.findByUsername(requesting_user.getUsername());

        List<User> allUsers = this.userRepository.findAll();

        //set all other users to Offline
        for (User user : allUsers) {
            user.setStatus(UserStatus.OFFLINE);
        }

        //If you don't find the user. Tell him to register first.
        String nonexisting_user = "This username is not registered yet. Please register first or enter an existing username.";
        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(nonexisting_user));
        }

        //Now since the user exists. Get the saved password and the typed password

        String userByPassword = userByUsername.getPassword();
        String user_typed_password = requesting_user.getPassword();

        //If the given password and the saved password are not the same, tell the user.
        String wrong_password = "You entered the wrong password. Maybe caps lock is activated.";
        if (!userByPassword.equals(user_typed_password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(wrong_password));
        }

        //Set the Status on Online and update the repository
        userByUsername.setStatus(UserStatus.ONLINE);
        User updatedUser = userRepository.save(userByUsername);
        userRepository.flush();


        return updatedUser;
    }

    // update the user after the edit
    public void update_user(Long userId, User userChange){

        List<User> allusers = this.userRepository.findAll();

        User usertoupdate = null;

        for(User i: allusers){
            if(userId == i.getId()){
                usertoupdate = i;
            }
        }

        if (userChange.getBirth_date() != null){
            usertoupdate.setBirth_date(userChange.getBirth_date());
        }

        if (userChange.getUsername() != null){
            //If user exists already you cannot change the name!
            checkIfUserExists(userChange);
            usertoupdate.setUsername(userChange.getUsername());
        }
        // save into the repository
        userRepository.save(usertoupdate);
        userRepository.flush();
    }

    // set the user offline
    public void logout(Long userId){
        List<User> allusers = this.userRepository.findAll();

        User leaving_user = null;

        for(User i: allusers){
            if(userId.equals(i.getId())){
                leaving_user = i;
            }
        }
        leaving_user.setStatus(UserStatus.OFFLINE);
        userRepository.save(leaving_user);
        userRepository.flush();
    }
    */
}
