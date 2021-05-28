package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Lobby Service
 * This class is the "worker" and responsible for all functionality related to the lobby
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(ch.uzh.ifi.hase.soprafs21.service.LobbyService.class);

    private final LobbyRepository lobbyRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserRepository userRepository, UserService userService) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public User getUser(Long userId){
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

    public void saveUser(User user){
        userRepository.save(user);
        userRepository.flush();
    }
    // create the lobby
    public Lobby createLobby(Lobby newLobby, Long userId) {
        checkIfLobbyExists(newLobby);
        User userFound = getUser(userId);

        String username = userFound.getUsername();
        newLobby.setToken(UUID.randomUUID().toString());
        newLobby.setStatus(LobbyStatus.OPEN);
        newLobby.setMembers(username);

        userFound.setStatus(UserStatus.CHILLING);

        // saves the given entity but data is only persisted in the database once flush() is called

        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

        saveUser(userFound);
        log.debug("Created Information for Lobby: {}", newLobby);
        return newLobby;
    }

    // Check if the lobby exists
    private void checkIfLobbyExists(Lobby lobbyToBeCreated) {
        Lobby lobbyByLobbyname = lobbyRepository.findByLobbyname(lobbyToBeCreated.getLobbyname());

        String taken_lobbyname_error = "The %s %s already taken. Please choose an other lobbyname!";

        if (lobbyByLobbyname != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(taken_lobbyname_error, "lobbyname", "is"));
        }
    }

    // get a requested lobby and compare password if its a private lobby.
    public Lobby getLobby(Long lobbyId) {
        // get all lobbies
        Optional<Lobby> potLobby = lobbyRepository.findById(lobbyId);
        Lobby lobby = null;

        if (potLobby.isEmpty()) { // if not found
            String nonExistingLobby = "This lobby does not exist. Please search for an existing lobby!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingLobby));
        } else { // if found
            lobby = potLobby.get();
        }

        return lobby;
    }

    // Change lobby settings
    public void update_lobby(Long lobbyId, Lobby lobbyChange) {
         Lobby lobbytoupdate = getLobby(lobbyId);

        // change lobby name
        if (lobbyChange.getLobbyname() != null) {
            checkIfLobbyExists(lobbyChange);
            lobbytoupdate.setLobbyname(lobbyChange.getLobbyname());
        }

        // change password (private & public)
        if (lobbyChange.getPassword() != null) {
            lobbytoupdate.setPassword(lobbyChange.getPassword());
        }


        // change lobby size
        if (lobbyChange.getSize() != null) {
            String more_members_than_size ="Too many members. Please remove some members, before decreasing the lobby size!";
            if(lobbytoupdate.getMembers().size() > lobbyChange.getSize()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(more_members_than_size));
            }
            lobbytoupdate.setSize(lobbyChange.getSize());
            if(lobbytoupdate.getMembers().size() == lobbytoupdate.getSize()) {
                lobbytoupdate.setStatus(LobbyStatus.FULL);
            }
        }

        // change number of rounds
        if (lobbyChange.getRounds() != null) {
            lobbytoupdate.setRounds(lobbyChange.getRounds());
        }

        // change timer
        if (lobbyChange.getTimer() != null) {
            lobbytoupdate.setTimer(lobbyChange.getTimer());
        }

        if (lobbyChange.getGameMode() != null) {
            lobbytoupdate.setGameMode(lobbyChange.getGameMode());
        }
        System.out.print(lobbytoupdate.getGameMode());
        // save into the repository
        lobbyRepository.save(lobbytoupdate);
        lobbyRepository.flush();
    }

    public void addLobbyMembers(Long lobbyId, Lobby userLobby) {

        Lobby lobbytoupdate = getLobby(lobbyId);

        String userName = userLobby.getLobbyname();
        String inputPassword = userLobby.getPassword();
        User user = userRepository.findByUsername(userName);

        String lobby_is_full = "You cannot enter. The lobby is already full!";

        // check if the lobby is not full
        if(lobbytoupdate.getStatus() == LobbyStatus.FULL) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(lobby_is_full));
        }

        // check if the password is correct
        String wrong_password = "You entered the wrong password!";
        if (lobbytoupdate.getPassword() != null && inputPassword == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(wrong_password));
        }
        else if (lobbytoupdate.getPassword() != null &&!inputPassword.equals(lobbytoupdate.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, String.format(wrong_password));
        }

        // check if the user is already a member
        String player_already_in_lobby = "This user is already a member of the lobby!";
        if (lobbytoupdate.getMembers().contains(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(player_already_in_lobby));
        }
        lobbytoupdate.setMembers(userName);
        user.setStatus(UserStatus.CHILLING);
        // check if the lobby is full after the new member is added
        if(lobbytoupdate.getMembers().size() == lobbytoupdate.getSize()) {
            lobbytoupdate.setStatus(LobbyStatus.FULL);
        }
        // save into the repository
        lobbyRepository.save(lobbytoupdate);
        lobbyRepository.flush();

        saveUser(user);

    }

    public void removeLobbyMembers(Long lobbyId, String userName) {

        Lobby lobbytoupdate = getLobby(lobbyId);
        User user = userRepository.findByUsername(userName);

        // check if the userId is part of the lobby members
        String player_not_in_lobby = "This user is not a member of the lobby!";
        if (!lobbytoupdate.getMembers().contains(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(player_not_in_lobby));
        }
        lobbytoupdate.deleteMembers(userName);
        user.setStatus(UserStatus.ONLINE);
        // set the lobby to OPEN
        lobbytoupdate.setStatus(LobbyStatus.OPEN);


        // delete the lobby if there are no more members in the lobby
        if (lobbytoupdate.getMembers().size() == 0) {
            lobbyRepository.delete(lobbytoupdate);
            lobbyRepository.flush();
        }
        else
            // save into the repository
            lobbyRepository.save(lobbytoupdate);
            lobbyRepository.flush();
        userRepository.save(user);
        userRepository.flush();
    }

    public void returnLobbyMembers (Long lobbyId, String userName) {

        Lobby lobbyToUpdate = getLobby(lobbyId);
        User user = userRepository.findByUsername(userName);

        // check if the user is already a member
        String playerAlreadyInLobby = "This user is not a member of the lobby! Please enter the lobby normally.";
        if (!lobbyToUpdate.getMembers().contains(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(playerAlreadyInLobby));
        }
        user.setStatus(UserStatus.CHILLING);

        if(lobbyToUpdate.getMembers().size() == lobbyToUpdate.getSize()) {
            lobbyToUpdate.setStatus(LobbyStatus.FULL);
        }
        else lobbyToUpdate.setStatus(LobbyStatus.OPEN);
        // save into the repository
        lobbyRepository.save(lobbyToUpdate);
        lobbyRepository.flush();

        saveUser(user);

    }

}

