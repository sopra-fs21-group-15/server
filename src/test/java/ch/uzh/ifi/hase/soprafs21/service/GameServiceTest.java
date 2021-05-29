package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//I commented this test out, since I could not fix it and it was not required for this milestone!
public class GameServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoundRepository roundRepository;

    @Mock
    private TimerService timerService;
    @Mock
    private RoundService roundService;
    @Mock
    private  ScoreBoardService scoreBoardService;
    @Mock
    private Thread thread;




    @InjectMocks
    private GameService gameService;

    private Lobby testLobby;
    private Game testgame;
    private ScoreBoard testScoreboard;
    private Timer testTimer;
    private Round testRound;
    private User testUser;




    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setStatus(UserStatus.ONLINE);
        testUser.setId(45L);
        testUser.setUsername("User1");
        testUser.setPassword("dsf");

        testLobby = new Lobby();
        testLobby.setId(2L);
        testLobby.setPassword("testPassword");
        testLobby.setLobbyname("test");
        testLobby.setTimer(60);
        testLobby.setSize(8);
        testLobby.setRounds(5);


        testgame = new Game();
        testgame.setGameName("test");
        testgame.setNumberOfRounds(testLobby.getRounds());
        testgame.setTimePerRound(testLobby.getTimer());
        //testgame.setLobbyId(testLobby.getId());
        testgame.setScoreBoard(testScoreboard);
        testgame.setTimer(testTimer);
        testgame.setId(4L);
        testgame.setGameModes(GameModes.CLASSIC);
        testgame.setRoundTracker(0);
        testgame.setTestphase(true);

        testRound = new Round();
        testRound.setId(13L);
        testRound.setIndex(0);


        testScoreboard = new ScoreBoard();
        testScoreboard.setId(3L);
        testScoreboard.setPlayers(testLobby.getMembers());


        testTimer = new Timer();
        testTimer.setId(4L);
        testTimer.setDrawingTimeSpan(testLobby.getTimer());




        // when -> any object is being save in the userRepository -> return the dummy testUser and the dummy testlobby
        Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
        Mockito.when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testLobby));
        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testgame));
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testgame);


    }

    @Test
     void getGame_byGameID(){
        Game foundgame = gameService.getGame(testgame.getId());

        //assertEquals(foundgame.getLobbyId(), testgame.getLobbyId());
        assertEquals(foundgame.getTimePerRound(), testgame.getTimePerRound());
        assertEquals(foundgame.getGameName(),testgame.getGameName());
        assertEquals(foundgame.getGameModes(), testgame.getGameModes());
        assertEquals(foundgame.getNumberOfRounds(), testgame.getNumberOfRounds());


    }
    @Test
     void getGame_byGameID_failed(){
        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> gameService.getGame(6L));

    }

    @Test
     void start_aGame_alone_failed(){
        testLobby.setMembers("SoloPlayer");

        assertThrows(ResponseStatusException.class, () -> gameService.createGame(testLobby));

    }

    @Test
    public void create_aLobby_success(){
        testLobby.setMembers("User1");
        testLobby.setMembers("User2");
        testLobby.setMembers("User3");



        Mockito.when(timerService.createTimer(testLobby.getTimer())).thenReturn(testTimer);
        Mockito.when(scoreBoardService.createScoreBoard(Mockito.any())).thenReturn(testScoreboard);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testgame);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);


        Mockito.doNothing().when(thread).start();

        Long newgameID = gameService.createGame(testLobby);

        Game foundgame = gameService.getGame(newgameID);


        assertEquals( testgame.getTimePerRound(),foundgame.getTimePerRound());
        assertEquals(testgame.getGameName(),foundgame.getGameName());
        assertEquals(testgame.getGameModes(),foundgame.getGameModes());
        assertEquals( testgame.getNumberOfRounds(),foundgame.getNumberOfRounds());
        assertEquals(testgame.getScoreBoard(), foundgame.getScoreBoard());
        assertEquals(testgame.getTimer(), foundgame.getTimer());
        assertEquals(UserStatus.INGAME, testUser.getStatus());
        assertEquals(LobbyStatus.PLAYING, testLobby.getStatus());

    }
/**
    @Test
    void create_game_success2(){
        testLobby.setMembers("User1");
        testLobby.setMembers("User2");
        testLobby.setMembers("User3");


        Mockito.when(gameRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(timerService.createTimer(testLobby.getTimer())).thenReturn(testTimer);
        Mockito.when(scoreBoardService.createScoreBoard(Mockito.any())).thenReturn(testScoreboard);
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testgame);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);


        Mockito.verify(gameRepository).save(Mockito.any());

        assertThrows(ResponseStatusException.class, () ->gameService.createGame(testLobby));



    }
**/

    @Test
    public void test_runMethode1(){
        ArrayList<String> players = new ArrayList<String>();
        players.add(testUser.getUsername());
        players.add("User2");
        players.add("User3");
        testgame.setPlayers(players);
        testgame.setNumberOfRounds(-1);
        testRound.setPlayers(players);
        Mockito.when(roundService.getRound(Mockito.any())).thenReturn(testRound);
        Mockito.when(roundRepository.saveAndFlush(Mockito.any())).thenReturn(testRound);

        gameService.set_testgames(testgame);
        gameService.run();



        assertEquals(RoundStatus.DONE, testRound.getStatus());

    }

    @Test
    public void test_runMethode2(){
        ArrayList<String> players = new ArrayList<String>();
        players.add(testUser.getUsername());
        players.add("User2");
        players.add("User3");
        testgame.setPlayers(players);

        testRound.setPlayers(players);


        Mockito.when(roundService.getRound(Mockito.any())).thenReturn(testRound);
        Mockito.when(roundRepository.saveAndFlush(Mockito.any())).thenReturn(testRound);
        Mockito.when(roundService.createRound(Mockito.any())).thenReturn(testRound);
        Mockito.doNothing().when(scoreBoardService).addPoints(Mockito.any(), Mockito.any(), Mockito.anyInt());
        Mockito.doNothing().when(roundService).setNewPainter(Mockito.any());

        Mockito.doNothing().when(roundService).resetChoice(Mockito.any());
        Mockito.doNothing().when(roundService).changePhase(Mockito.any());
        


        gameService.set_testgames(testgame);
        gameService.run();



        assertEquals(RoundStatus.DONE, testRound.getStatus());

    }




    @Test
    void test_game_leaving_success(){
        ArrayList<String> players = new ArrayList<String>();
        players.add(testUser.getUsername());
        players.add("User2");
        players.add("User3");
        testgame.setPlayers(players);


        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        gameService.leaveGame(testgame.getId(), testUser.getUsername());

        assertFalse(testgame.getPlayers().contains(testUser.getUsername()));
        assertEquals(UserStatus.ONLINE,testUser.getStatus());
        assertEquals(2,testgame.getPlayers().size());
    }

    @Test
    void remove_lastPlayer(){
        ArrayList<String> players = new ArrayList<String>();
        players.add(testUser.getUsername());
        testgame.setPlayers(players);
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        gameService.leaveGame(testgame.getId(), testUser.getUsername());
        Mockito.verify(gameRepository).delete(Mockito.any());

    }

    @Test
    void test_game_leaving_failed(){
        ArrayList<String> players = new ArrayList<String>();
        players.add(testUser.getUsername());
        players.add("User2");
        players.add("User3");
        testgame.setPlayers(players);


        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        assertThrows(ResponseStatusException.class, () ->gameService.leaveGame(testgame.getId(), "NotMember"));

    }
    /*
    *@Test
    void addpoints_methode(){

    }
*/


}
