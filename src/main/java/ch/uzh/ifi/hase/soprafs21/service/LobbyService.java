package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
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
 * Lobby Service
 * This class is the "worker" and responsible for all functionality related to the lobby
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(ch.uzh.ifi.hase.soprafs21.service.LobbyService.class);

    private final LobbyRepository lobbyRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository) {
    this.lobbyRepository = lobbyRepository;
    }

    public List<Lobby> getLobbies() {
        return this.lobbyRepository.findAll();
    }

    // create the lobby
    public Lobby createLobby(Lobby newLobby) {
        checkIfLobbyExists(newLobby);

        newLobby.setToken(UUID.randomUUID().toString());
        newLobby.setStatus(LobbyStatus.WAITING);
        newLobby.setCreation_date(getDate()); //get the creation date with the current date

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

    // Save the lobby date
    private String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}

