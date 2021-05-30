package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    void findByName_success() {
        Lobby lobby = new Lobby();

        // initialize all the fields
        lobby.setPassword("1234");
        lobby.setLobbyname("TestLobby");
        lobby.setToken("1");
        lobby.setSize(2);
        lobby.setRounds(1);
        lobby.setTimer(60);
        lobby.setMembers("Player1");
        lobby.setStatus(LobbyStatus.OPEN);
        lobby.setGameMode(GameModes.CLASSIC);

        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Lobby found = lobbyRepository.findByLobbyname(lobby.getLobbyname());

        // then
        assertEquals(found.getId(), lobby.getId());
        assertEquals(found.getPassword(), lobby.getPassword());
        assertEquals(found.getLobbyname(), lobby.getLobbyname());
        assertEquals(found.getSize(), lobby.getSize());
        assertEquals(found.getTimer(), lobby.getTimer());
        assertEquals(found.getRounds(), lobby.getRounds());
        assertEquals(found.getToken(), lobby.getToken());
        assertEquals(found.getMembers(), lobby.getMembers());
        assertEquals(found.getStatus(), lobby.getStatus());
        assertEquals(found.getGameMode(), lobby.getGameMode());
    }

    @Test
    void findByLobbyId_success() {
        Lobby lobby = new Lobby();

        // initialize all the fields
        lobby.setPassword("1234");
        lobby.setLobbyname("TestLobby");
        lobby.setToken("1");
        lobby.setSize(2);
        lobby.setRounds(1);
        lobby.setTimer(60);
        lobby.setMembers("Player1");
        lobby.setStatus(LobbyStatus.OPEN);
        lobby.setGameMode(GameModes.CLASSIC);

        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Optional<Lobby> found = lobbyRepository.findById(lobby.getId());

        // then
        assertEquals(found.get().getId(), lobby.getId());
        assertEquals(found.get().getPassword(), lobby.getPassword());
        assertEquals(found.get().getLobbyname(), lobby.getLobbyname());
        assertEquals(found.get().getSize(), lobby.getSize());
        assertEquals(found.get().getTimer(), lobby.getTimer());
        assertEquals(found.get().getRounds(), lobby.getRounds());
        assertEquals(found.get().getToken(), lobby.getToken());
        assertEquals(found.get().getMembers(), lobby.getMembers());
        assertEquals(found.get().getStatus(), lobby.getStatus());
        assertEquals(found.get().getGameMode(), lobby.getGameMode());
    }

}
