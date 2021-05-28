package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.ScoreBoard;
import ch.uzh.ifi.hase.soprafs21.repository.ScoreBoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//I commented this test out, since I could not fix it and it was not required for this milestone!
public class ScoreBoardServiceTest {

    @Mock
    private ScoreBoardRepository scoreBoardRepository;



    @InjectMocks
    private ScoreBoardService scoreBoardService;
    private ScoreBoard testscoreBoard;
    private ScoreBoard testscoreBoard2;
    private Lobby testLobby;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testscoreBoard = new ScoreBoard();
        testscoreBoard.setId(1L);

        testscoreBoard2 = new ScoreBoard();
        testscoreBoard2.setId(6L);

        testLobby = new Lobby();
        testLobby.setId(2L);
        testLobby.setPassword("testPassword");
        testLobby.setLobbyname("test");
        testLobby.setTimer(60);
        testLobby.setSize(8);
        testLobby.setRounds(5);
        testLobby.setMembers("User1");
        testLobby.setMembers("User2");
        testLobby.setMembers("User3");
        testLobby.setMembers("User4");


        // when -> any object is being save in the userRepository -> return the dummy testUser and the dummy testlobby
        Mockito.when(scoreBoardRepository.save(Mockito.any())).thenReturn(testscoreBoard);
        Mockito.when(scoreBoardRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testscoreBoard));
    }

    @Test
     void create_Scoreboard() {
        // when -> any object is being save in the userRepository -> return the dummy testUser

        ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);

        // then
        Mockito.verify(scoreBoardRepository, Mockito.times(1)).saveAndFlush(Mockito.any());

        assertEquals(testLobby.getMembers(), createdScoreboard.getPlayers());

    }
    @Test
     void get_Scoreboard_byID_success(){
        ScoreBoard getScoreboard = scoreBoardService.getScoreBoard(testscoreBoard.getId());

        assertEquals(testscoreBoard.getId(), getScoreboard.getId());
    }
@Test
     void get_Scoreboard_byID_failed(){


        Mockito.when(scoreBoardRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> scoreBoardService.getScoreBoard(5L));
    }

    @Test
     void get_players(){
        ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);

        // then
        Mockito.verify(scoreBoardRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        ArrayList<String> players = scoreBoardService.getPlayers(createdScoreboard);

        assertEquals( testLobby.getMembers().get(0),players.get(0));
        assertEquals( testLobby.getMembers().get(1),players.get(1));
        assertEquals( testLobby.getMembers().get(2),players.get(2));
        assertEquals( testLobby.getMembers().get(3),players.get(3));
    }
    @Test
     void set_Players(){
        ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);

        // then
        Mockito.verify(scoreBoardRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        ArrayList<String> players = scoreBoardService.getPlayers(createdScoreboard);
        players.add("extraUser");
        scoreBoardService.setPlayers(createdScoreboard, players);

        assertEquals(5,createdScoreboard.getPlayers().size());
        assertEquals( "extraUser",createdScoreboard.getPlayers().get(4));
    }
@Test
     void update_scoreboard(){
    ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
    assertEquals( 0,createdScoreboard.getScore()[0]);
    assertEquals( 0,createdScoreboard.getScore()[1]);
    assertEquals( 0,createdScoreboard.getScore()[2]);
    assertEquals( 0,createdScoreboard.getScore()[3]);

    int [] newscores = {200,500,600,50};
    scoreBoardService.updateScore(createdScoreboard, newscores );
    assertEquals( newscores[0],createdScoreboard.getScore()[0]);
    assertEquals( newscores[1],createdScoreboard.getScore()[1]);
    assertEquals( newscores[2],createdScoreboard.getScore()[2]);
    assertEquals( newscores[3],createdScoreboard.getScore()[3]);

}
@Test
 void update_scoreboard_wrong() {
    ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
    assertEquals( 0,createdScoreboard.getScore()[0]);
    assertEquals( 0,createdScoreboard.getScore()[1]);
    assertEquals( 0,createdScoreboard.getScore()[2]);
    assertEquals( 0,createdScoreboard.getScore()[3]);

    int[] newscores = {200, 500, 600,};
    scoreBoardService.updateScore(createdScoreboard, newscores);
    assertEquals( 0,createdScoreboard.getScore()[0]);
    assertEquals( 0,createdScoreboard.getScore()[1]);
    assertEquals( 0,createdScoreboard.getScore()[2]);
    assertEquals( 0,createdScoreboard.getScore()[3]);

}

@Test
 void setScore_test(){
    ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
    assertEquals( 0,createdScoreboard.getScore()[0]);
    assertEquals( 0,createdScoreboard.getScore()[1]);
    assertEquals( 0,createdScoreboard.getScore()[2]);
    assertEquals( 0,createdScoreboard.getScore()[3]);

    int [] newscores = {200,500,600,50};
    scoreBoardService.setScore(createdScoreboard, newscores );
    assertEquals( newscores[0],createdScoreboard.getScore()[0]);
    assertEquals( newscores[1],createdScoreboard.getScore()[1]);
    assertEquals( newscores[2],createdScoreboard.getScore()[2]);
    assertEquals( newscores[3],createdScoreboard.getScore()[3]);


}

    @Test
     void setScore_test_wrong(){
        ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
        assertEquals( 0,createdScoreboard.getScore()[0]);
        assertEquals( 0,createdScoreboard.getScore()[1]);
        assertEquals( 0,createdScoreboard.getScore()[2]);
        assertEquals( 0,createdScoreboard.getScore()[3]);

        int [] newscores = {200,500,600};
        scoreBoardService.setScore(createdScoreboard, newscores );
        assertEquals( 0,createdScoreboard.getScore()[0]);
        assertEquals( 0,createdScoreboard.getScore()[1]);
        assertEquals( 0,createdScoreboard.getScore()[2]);
        assertEquals( 0,createdScoreboard.getScore()[3]);

    }



@Test
     void test_ranking(){

    ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
    int[] ranking = {2,4,3,1};
    createdScoreboard.setRanking(ranking);
    scoreBoardService.getRanking(createdScoreboard);

    assertEquals(2, createdScoreboard.getRanking()[0]);
    assertEquals(4, createdScoreboard.getRanking()[1]);
    assertEquals(3, createdScoreboard.getRanking()[2]);
    assertEquals(1, createdScoreboard.getRanking()[3]);

}
@Test
     void test_setRanking(){
    ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
    int[] ranking = {2,4,3,1};
    testscoreBoard.setRanking(ranking);
    scoreBoardService.setRanking(createdScoreboard, ranking);


    assertEquals(testscoreBoard.getRanking(),createdScoreboard.getRanking());


}
@Test
     void test_setRanking_wrong(){
    ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
    int[] ranking = {2,4,3};
    testscoreBoard.setRanking(ranking);
    scoreBoardService.setRanking(createdScoreboard, ranking);

    assertNotEquals(testscoreBoard.getRanking(),createdScoreboard.getRanking());
    }

    @Test
     void getScoretest(){
        ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
        int [] newscores = {200,500,600,50};


        createdScoreboard.setScore(newscores);

        Arrays.sort(newscores);

        assertEquals(newscores[3],scoreBoardService.getScore(createdScoreboard)[0]);
        assertEquals(newscores[2],scoreBoardService.getScore(createdScoreboard)[1]);
        assertEquals(newscores[1],scoreBoardService.getScore(createdScoreboard)[2]);
        assertEquals(newscores[0],scoreBoardService.getScore(createdScoreboard)[3]);



    }

    @Test
     void addingPoints_test(){
        ScoreBoard createdScoreboard = scoreBoardService.createScoreBoard(testLobby);
        int [] newscores = {200,500,600,50};
        createdScoreboard.setScore(newscores);
        scoreBoardService.addPoints(createdScoreboard, createdScoreboard.getPlayers().get(2), 50);

        Mockito.when(scoreBoardRepository.saveAndFlush(Mockito.any())).thenReturn(createdScoreboard);

        assertEquals(newscores[0],createdScoreboard.getScore()[0]);
         assertEquals(newscores[1],createdScoreboard.getScore()[1]);
         assertEquals(650,createdScoreboard.getScore()[2]);
         assertEquals(newscores[3],createdScoreboard.getScore()[3]);
    }

}