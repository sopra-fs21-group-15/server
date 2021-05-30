package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

//I commented this test out, since I could not fix it and it was not required for this milestone!

/**
 * Test class for the UserResource REST resource.
 *
 * @see LobbyService
 */

    @WebAppConfiguration
    @SpringBootTest
    public class LobbyServiceIntegrationTest {

        @Qualifier("lobbyRepository")
        @Autowired
        private LobbyRepository lobbyRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private LobbyService lobbyService;
        @Autowired
        private UserService userService;



        @BeforeEach
        public void setup() {
            userRepository.deleteAll();
            lobbyRepository.deleteAll();
        }




        @Test
        void createLobby_validInputs_success() {
            // given
            assertNull(lobbyRepository.findByLobbyname("testUsername"));

            User testUser = new User();
            testUser.setPassword("testPassword");
            testUser.setUsername("testUsername");
            User newUser = userService.createUser(testUser);


            // when

            Lobby testLobby = new Lobby();
            testLobby.setStatus(LobbyStatus.OPEN);
            testLobby.setGameMode(GameModes.CLASSIC);
            testLobby.setSize(5);
            testLobby.setRounds(4);
            testLobby.setLobbyname("testlobby");
            testLobby.setTimer(15);


            // when
            Lobby createdLobby = lobbyService.createLobby(testLobby, newUser.getId());

            // then
            assertEquals(testLobby.getId(), createdLobby.getId());
            assertEquals(testLobby.getSize(), createdLobby.getSize());
            assertEquals(testLobby.getTimer(), createdLobby.getTimer());
            assertEquals(testLobby.getRounds(), createdLobby.getRounds());
            assertEquals(testLobby.getLobbyname(), createdLobby.getLobbyname());
            assertNotNull(createdLobby.getToken());
            assertTrue(createdLobby.getMembers().contains(testUser.getUsername()));

            assertEquals(LobbyStatus.OPEN, createdLobby.getStatus());
        }


     @Test
     void createUser_duplicateUsername_throwsException() {
        assertNull(lobbyRepository.findByLobbyname("testUsername"));

         User testUser = new User();
         testUser.setPassword("testPassword");
         testUser.setUsername("testUsername");
         User newUser = userService.createUser(testUser);


        // when

        Lobby testLobby = new Lobby();
        testLobby.setStatus(LobbyStatus.OPEN);
        testLobby.setGameMode(GameModes.CLASSIC);
        testLobby.setSize(5);
        testLobby.setRounds(4);
        testLobby.setLobbyname("testlobby");
        testLobby.setTimer(15);


        // when
         Lobby createdLobby = lobbyService.createLobby(testLobby, newUser.getId());
        // attempt to create second user with same username

         User testUser3 = new User();
         testUser3.setPassword("testPassword");
         testUser3.setUsername("testUsername3");
         User newUser2 = userService.createUser(testUser3);


        Lobby testLobby2 = new Lobby();
        testLobby2.setStatus(LobbyStatus.OPEN);
        testLobby2.setGameMode(GameModes.CLASSIC);
        testLobby2.setSize(6);
        testLobby2.setRounds(7);
        testLobby2.setLobbyname("testlobby");
        testLobby2.setTimer(80);



        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby2,newUser2.getId()));
    }

   
}
