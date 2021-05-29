package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    //save user
    public void saveUser(User user) {
        userRepository.save(user);
        userRepository.flush();
    }

    //save user & return it
    public User returnSavedUser(User user) {
        user = userRepository.save(user);
        userRepository.flush();
        return user;
    }

    // create the user
    public User createUser(User newUser) {
        checkIfUsernameTaken(newUser);

        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.ONLINE);
        newUser.setCreationDate(getDate()); //get the creation date with the current date

        // saves the given entity but data is only persisted in the database once flush() is called

        newUser = returnSavedUser(newUser);

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    // getUser from Id
    public User getUserById(Long userId) {
        User user;
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            user = optional.get();
            return user;
        }
        else {
            String nonExistingUser = "This user does not exist. Please search for an existing user!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingUser));
        }
    }

    public User getUserByUserName(User userInput) {
        User user = userRepository.findByUsername(userInput.getUsername());
        return user;
    }


    /**
     * This is a helper method that will check the uniqueness criteria of the username and the password
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */

    // Check if the user Exists
    private void checkIfUsernameTaken(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        //get entered password

        String enteredPassword = userToBeCreated.getPassword();

        String takenUsernameError = "The %s %s already taken. Please choose an other username!";

        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(takenUsernameError, "username", "is"));
        }
    }

    // Get the date for the creation date information.
    private String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    //Handle the log-in
    public User loginRequest(User requestingUser) {

        User userByUsername = userRepository.findByUsername(requestingUser.getUsername());


        //If you don't find the user. Tell him to register first.
        String nonExistingUser = "This username is not registered yet. Please register first or enter an existing username.";
        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(nonExistingUser));
        }

        //Now since the user exists. Get the saved password and the typed password

        String userByPassword = userByUsername.getPassword();
        String user_typed_password = requestingUser.getPassword();

        //If the given password and the saved password are not the same, tell the user.
        String wrong_password = "You entered the wrong password. Maybe caps lock is activated.";
        if (!userByPassword.equals(user_typed_password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(wrong_password));
        }

        //Set the Status on Online and update the repository
        userByUsername.setStatus(UserStatus.ONLINE);
        User updatedUser = returnSavedUser(userByUsername);

        return updatedUser;
    }

    // update the user after the edit
    public void updateUser(Long userId, User userChange){

        User userToUpdate = getUserById(userId);

        if (userChange.getBirthDate() != null){
            userToUpdate.setBirthDate(userChange.getBirthDate());
        }

        if (userChange.getUserTag() != null){
            userToUpdate.setUserTag(userChange.getUserTag());
        }

        if (userChange.getUsername() != null){
            //If user exists already you cannot change the name!
            checkIfUsernameTaken(userChange);
            userToUpdate.setUsername(userChange.getUsername());
        }
        // save into the repository
        saveUser(userToUpdate);
    }

    // set the user offline
    public void logout(Long userId){

        User leavingUser = getUserById(userId);

        leavingUser.setStatus(UserStatus.OFFLINE);
        saveUser(leavingUser);
    }

    public void addUserToFriendsList(Long userId, User friend) {

        User user1 = getUserById(userId);
        User user2 = userRepository.findByUsername(friend.getUsername());

        // check if the user is already a friend
        String playerAlreadyInFriendsList = "This user is already your friend!";
        if (user1.getFriendsList().contains(user2.getUsername()) || user2.getFriendsList().contains(user1.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(playerAlreadyInFriendsList));
        }
        else
            user1.setFriendsList(user2.getUsername());
            user2.setFriendsList(user1.getUsername());

        // save into the repository
        saveUser(user1);
        saveUser(user2);
    }

    public void removeUserFromFriendsList(Long userId, User friend) {
        User user1 = getUserById(userId);
        User user2 = userRepository.findByUsername(friend.getUsername());

        // check if the user is already a friend
        String playerAlreadyNotInFriendsList = "This user is already not your friend!";
        if (!user1.getFriendsList().contains(user2.getUsername()) || !user2.getFriendsList().contains(user1.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(playerAlreadyNotInFriendsList));
        }
        else
            user1.deleteFriendsList(user2.getUsername());
            user2.deleteFriendsList(user1.getUsername());

        // save into the repository
        saveUser(user1);
        saveUser(user2);
    }

    // send and accept friend requests?
    public void addUserToFriendRequestList(Long userId, User friend) {

        User requestingUser = getUserById(userId);
        User userToUpdate = userRepository.findByUsername(friend.getUsername());

        // check if the user is already a friend
        String playerAlreadyInFriendRequestList = "You already sent a friend request to this user. Please wait for his respond!";
        String playerAlreadyInFriendsList = "This user is already your friend!";
        if (userToUpdate.getFriendRequestList().contains(requestingUser.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(playerAlreadyInFriendRequestList));
        }
        else if (userToUpdate.getFriendsList().contains(requestingUser.getUsername()) || requestingUser.getFriendsList().contains(userToUpdate.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(playerAlreadyInFriendsList));
        }
        else userToUpdate.setFriendRequestList(requestingUser.getUsername());

        // save into the repository
        saveUser(userToUpdate);
    }


    public void removeUserFromFriendRequestList(Long userId, User friend) {
        User userToUpdate = getUserById(userId);

        String userName = friend.getUsername();

        // check if the user is already a friend
        String playerAlreadyNotInFriendRequestList = "This user has not send a friend request to you. Thus cannot be declined/accepted.";
        if (!userToUpdate.getFriendRequestList().contains(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(playerAlreadyNotInFriendRequestList));
        }
        else userToUpdate.deleteFriendRequestList(userName);

        // save into the repository
        saveUser(userToUpdate);
    }

}
