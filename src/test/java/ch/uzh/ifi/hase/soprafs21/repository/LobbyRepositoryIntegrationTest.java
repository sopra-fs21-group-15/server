package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;


    @Autowired
    private  LobbyRepository lobbyRepository;

 /**   @Test
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


        entityManager.persist(lobby);
        entityManager.flush();

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
}
