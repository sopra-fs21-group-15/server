package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
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

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserRepository userRepository) {
    this.lobbyRepository = lobbyRepository;
    this.userRepository = userRepository;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    public User getUser_id(Long userId){
        User user;
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            user = optional.get();
            return user;
        }
        else {
            String nonexisting_user = "This user does not exist. Please search for an existing user!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_user));
        }
    }



    // create the lobby
    public Lobby createLobby(Lobby newLobby, Long userId) {
        checkIfLobbyExists(newLobby);
          User user_found = getUser_id(userId);
        ////get all users
        //List<User> all_users = this.userRepository.findAll();
        //
        //User user_found = null;
        //for (User i : all_users) {
        //    if (userId == i.getId()) {
        //        user_found = i;
        //    }
        //}
        //
        ////if not found
        //String nonexisting_user = "This user does not exist!";
        //if (user_found == null) {
        //    new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_user));
        //}

        String username = user_found.getUsername();
        newLobby.setToken(UUID.randomUUID().toString());
        newLobby.setStatus(LobbyStatus.OPEN);
        newLobby.setMembers(username);

        // saves the given entity but data is only persisted in the database once flush() is called

        newLobby = lobbyRepository.save(newLobby);
        lobbyRepository.flush();

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
        Lobby value = null;

        if (potLobby.isEmpty()) { // if not found
            String nonExistingLobby = "This lobby does not exist. Please search for an existing lobby!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingLobby));
        } else { // if found
            value = potLobby.get();
        }

        return value;
    }

   // public Lobby getLobby(Long lobbyId) {
   //
   //     //get all lobbies
   //     List<Lobby> alllobbies = this.lobbyRepository.findAll();
   //     Lobby lobbytofind = null;
   //
   //     for (Lobby i : alllobbies) {
   //         if (lobbyId == i.getId()) {
   //             lobbytofind = i;
   //         }
   //     }
   //
   //     String nonexisting_lobby = "This lobby does not exist. Please search for an existing lobby!";
   //     if (lobbytofind == null) {
   //         throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonexisting_lobby));
   //     }
   //
   //     return lobbytofind;
   // }

    // Change lobby settings
    public void update_lobby(Long lobbyId, Lobby lobbyChange) {
         Lobby lobbytoupdate = getLobby(lobbyId);
        //List<Lobby> alllobbies = this.lobbyRepository.findAll();
        //
        //Lobby lobbytoupdate = null;
        //
        //for (Lobby i : alllobbies) {
        //    if (lobbyId == i.getId()) {
        //        lobbytoupdate = i;
        //    }
        //}

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
            lobbytoupdate.setSize(lobbyChange.getSize());
        }

        // change number of rounds
        if (lobbyChange.getRounds() != null) {
            lobbytoupdate.setRounds(lobbyChange.getRounds());
        }

        // change timer
        if (lobbyChange.getTimer() != null) {
            lobbytoupdate.setTimer(lobbyChange.getTimer());
        }

        // save into the repository
        lobbyRepository.save(lobbytoupdate);
        lobbyRepository.flush();
    }

    public void add_lobby_members(Long lobbyId, Lobby userLobby) {

        Lobby lobbytoupdate = getLobby(lobbyId);

        //List<Lobby> alllobbies = this.lobbyRepository.findAll();
        //
        //Lobby lobbytoupdate = null;
        //
        //for (Lobby i : alllobbies) {
        //    if (lobbyId == i.getId()) {
        //        lobbytoupdate = i;
        //    }
        //}

        String userName = userLobby.getLobbyname();
        String inputPassword = userLobby.getPassword();

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
        else lobbytoupdate.setMembers(userName);

        // check if the lobby is full after the new member is added
        if(lobbytoupdate.getMembers().size() == lobbytoupdate.getSize()) {
            lobbytoupdate.setStatus(LobbyStatus.FULL);
        }
        System.out.println();
        // save into the repository
        lobbyRepository.save(lobbytoupdate);
        lobbyRepository.flush();
    }

    public void remove_lobby_members(Long lobbyId, String userName) {

        Lobby lobbytoupdate = getLobby(lobbyId);

        //
        //List<Lobby> alllobbies = this.lobbyRepository.findAll();
        //
        //Lobby lobbytoupdate = null;
        //
        //for (Lobby i : alllobbies) {
        //    if (lobbyId == i.getId()) {
        //        lobbytoupdate = i;
        //    }
        //}

        // check if the userId is part of the lobby members
        String player_not_in_lobby = "This user is not a member of the lobby!";
        if (!lobbytoupdate.getMembers().contains(userName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(player_not_in_lobby));
        }
        else lobbytoupdate.deleteMembers(userName);

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
    }
/*
    public void update_lobby_chat(Long lobbyId, String lobbyChatUpdate){
        Lobby lobbytoupdate = getLobby(lobbyId);

        //List<Lobby> alllobbies = this.lobbyRepository.findAll();
        //
        ////Lobby lobbytoupdate = null;

        //for (Lobby i : alllobbies) {
        //    if (lobbyId == i.getId()) {
        //        lobbytoupdate = i;
        //    }
        //}
        //
    }
*/
}

