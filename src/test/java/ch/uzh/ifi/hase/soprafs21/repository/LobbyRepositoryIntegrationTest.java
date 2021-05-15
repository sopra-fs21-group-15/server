package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;


    @Autowired
    private  LobbyRepository lobbyRepository;
/**
   @Test
   public void findByName_success() {
        // given
        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setMembers("User");
        lobby.setPassword("123");
        lobby.setLobbyname("test");
        lobby.setSize(5);
        lobby.setTimer(60);
        lobby.setRounds(4);
        lobby.setToken("1");


       System.out.println("----------------------------------");
        System.out.println(lobby);
       System.out.println("----------------------------------");
       entityManager.persistAndFlush(lobby);
        // when
        Lobby found = lobbyRepository.findByLobbyname(lobby.getLobbyname());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getPassword(), lobby.getPassword());
        assertEquals(found.getLobbyname(), lobby.getLobbyname());
        assertEquals(found.getTimer(), lobby.getTimer());
        assertEquals(found.getRounds(), lobby.getRounds());
        assertEquals(found.getSize(), lobby.getSize());
        assertEquals(found.getMembers(), lobby.getMembers());
        
    }
   **/
/**

@Test
void findByLobbyId_success() {

    Lobby lobby = new Lobby();
    lobby.setLobbyname("testlobby");
    lobby.setId(2L);
    lobby.setMembers("User");
    lobby.setRounds(13);
    lobby.setSize(1);
    lobby.setTimer(2);
    lobby.setPassword("7");
    lobby.setToken("2");
    lobby.setStatus(LobbyStatus.FULL);

    entityManager.persistAndFlush(lobby);


    Optional<Lobby> foundLobby = lobbyRepository.findById(lobby.getId());
    assertTrue(foundLobby.isPresent());
    Lobby actualLobby = foundLobby.get();

    assertEquals(lobby.getId(), actualLobby.getId());
    assertTrue(actualLobby.getMembers().contains("User"));
    assertEquals(actualLobby.getRounds(), lobby.getRounds());
    assertEquals(lobby.getTimer(), actualLobby.getTimer());
    assertEquals(lobby.getPassword(), actualLobby.getPassword());
}
**/
}
