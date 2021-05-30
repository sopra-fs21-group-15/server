package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
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
 * @see GameService
 */

    @WebAppConfiguration
    @SpringBootTest
    public class GameServiceIntegrationTest {

        @Qualifier("gameRepository")
        @Autowired
        private GameRepository gameRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private LobbyRepository lobbyRepository;

        @Autowired
        private RoundRepository roundRepository;

        @Autowired
        private RoundService roundService;

        @Autowired
        private LobbyService lobbyService;

        @Autowired
        private TimerService timerService;

        @Autowired
        private ScoreBoardService scoreBoardService;

        @Autowired
        private GameService gameService;



        @BeforeEach
        public void setup() {
            roundRepository.deleteAll();
            gameRepository.deleteAll();
            userRepository.deleteAll();
            lobbyRepository.deleteAll();
        }


        @Test
        void createGame_validInputs_success() {
            // given
            assertNull(lobbyRepository.findByLobbyname("testUsername"));

            User testUser = new User();
            testUser.setId(1L);
            testUser.setToken("a");
            testUser.setFriendRequestList("request");
            testUser.setFriendsList("friend");
            testUser.setStatus(UserStatus.ONLINE);
            testUser.setCreationDate("02.02.1995");
            testUser.setPassword("testPassword");
            testUser.setUsername("testUsername");
            userRepository.save(testUser);
            userRepository.flush();

            User testUser2 = new User();
            testUser2.setId(4L);
            testUser2.setToken("e");
            testUser2.setFriendRequestList("request");
            testUser2.setFriendsList("friend");
            testUser2.setStatus(UserStatus.ONLINE);
            testUser2.setCreationDate("02.02.1995");
            testUser2.setPassword("testPassword");
            testUser2.setUsername("testUsername2");
            userRepository.save(testUser2);
            userRepository.flush();

            User testUser3 = new User();
            testUser3.setId(9L);
            testUser3.setToken("t");
            testUser3.setFriendRequestList("request");
            testUser3.setFriendsList("friend");
            testUser3.setStatus(UserStatus.ONLINE);
            testUser3.setCreationDate("02.02.1995");
            testUser3.setPassword("testPassword");
            testUser3.setUsername("testUsername3");
            userRepository.save(testUser3);
            userRepository.flush();


            // when

            Lobby testLobby = new Lobby();
            testLobby.setStatus(LobbyStatus.OPEN);
            testLobby.setGameMode(GameModes.CLASSIC);
            testLobby.setToken("jdsfklf");
            testLobby.setSize(5);
            testLobby.setRounds(4);
            testLobby.setLobbyname("testlobby");
            testLobby.setTimer(15);
            testLobby.setMembers(testUser.getUsername());
            testLobby.setMembers(testUser2.getUsername());
            testLobby.setMembers(testUser3.getUsername());
            lobbyRepository.save(testLobby);
            lobbyRepository.flush();


            // when
            Long newGameID= gameService.createGame(testLobby);
            Game createdGame = gameService.getGame(newGameID);

            // then
            assertEquals(testLobby.getId(), createdGame.getId());

            assertEquals(testLobby.getTimer(), createdGame.getTimePerRound());
            assertEquals(testLobby.getRounds(), createdGame.getNumberOfRounds());
            assertEquals(testLobby.getLobbyname(), createdGame.getGameName());
            assertNotNull(createdGame.getTimer());
            assertTrue(createdGame.getPlayers().contains(testUser.getUsername()));

            assertEquals(LobbyStatus.PLAYING, testLobby.getStatus());
        }
    @Test
     void createGame_notEnoughmembers_throwsException() {
        assertNull(lobbyRepository.findByLobbyname("testUsername"));

        User testUser = new User();
        testUser.setId(1L);
        testUser.setToken("a");
        testUser.setFriendRequestList("request");
        testUser.setFriendsList("friend");
        testUser.setStatus(UserStatus.ONLINE);
        testUser.setCreationDate("02.02.1995");
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        userRepository.save(testUser);
        userRepository.flush();



        // when

        Lobby testLobby = new Lobby();
        testLobby.setStatus(LobbyStatus.OPEN);
        testLobby.setGameMode(GameModes.CLASSIC);
        testLobby.setToken("jdsfklf");
        testLobby.setSize(5);
        testLobby.setRounds(4);
        testLobby.setLobbyname("testlobby");
        testLobby.setTimer(15);
        testLobby.setMembers(testUser.getUsername());
        lobbyRepository.save(testLobby);
        lobbyRepository.flush();


        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testLobby));
    }

   
}
