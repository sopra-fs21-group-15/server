package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
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
import java.util.UUID;


import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;import java.time.format.DateTimeFormatter;
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

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setCreation_date(getDate()); //set the creation date

        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User getUser(User oldUser) {

        //get all users
        List<User> all_users = this.userRepository.findAll();

        User user_found = null;
        for (User i : all_users) {
            if (oldUser.getUsername() == i.getUsername()) {
                user_found = i;
            }
        }
        return user_found;

    }


    /**
     * This is a helper method that will check the uniqueness criteria of the username and the password
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        //get entered password

        String entered_password = userToBeCreated.getPassword();

        String taken_username_error = "The %s provided %s already taken. Please choose an other username!";
        String empty_password_error = "You have not entered a password yet. Please enter one to register.";

        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(taken_username_error, "username", "is"));
        }
        else if (entered_password == "") {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(empty_password_error));
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

        //If you don't find the user. Tell him to register first.
        String nonexisting_user = "This username is not registered yet. Please register first or enter an existing username.";
        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(nonexisting_user));
        }

        //Now since the user exists. Get the saved password and the typed password

        String userByPassword = userByUsername.getPassword();
        String user_typed_password = requesting_user.getPassword();

        //If the given password and the saved password are not the same, tell the user.
        String wrong_password = "You entered the wrong password. Maybe caps lock is activated.";
        if (!userByPassword.equals(user_typed_password)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(wrong_password));
        }
        // save the data (copied from above)... Maybe useful for later

        User updatedUser = userRepository.save(userByUsername);
        userRepository.flush();

        return userByUsername;
    }



}
