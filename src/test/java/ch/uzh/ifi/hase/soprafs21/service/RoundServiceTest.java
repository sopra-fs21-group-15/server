package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//I commented this test out, since I could not fix it and it was not required for this milestone!
public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;
    @Mock
    private DrawingRepository drawingRepository;
    @Mock
    private GameRepository gameRepository;



    @InjectMocks
    private RoundService roundService;
    public Round testround;
    public Game testGame;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        ArrayList<String> players = new ArrayList();
        players.add("User1");
        players.add("User2");
        players.add("User3");
        players.add("User4");

        ArrayList <String> words = new ArrayList();
        words.add("House");
        words.add("get");
        words.add("Test");
        words.add("abc");
        words.add("kjdfhkg");
        words.add("Test5");
        words.add("Test6");
        words.add("Test7");
        words.add("Test8");
        words.add("Test9");
        int [] points = {10,20,30,40};
        boolean [] drawn = {false,false,false,false};
        boolean[] guessed= {false,false,false,false};

        testGame = new Game();
        testGame.setGameModes(GameModes.CLASSIC);
        testGame.setId(1L);
        testGame.setTimePerRound(60);
        testGame.setNumberOfRounds(4);
        testGame.setGameName("Test");
        testGame.setRoundTracker(0);
        testGame.setRoundId(2L);
        testGame.setPlayers(players);

        testround = new Round();
        testround.setId(2L);
        testround.setDrawerName("User1");
        testround.setIndex(2);
        testround.setStatus(RoundStatus.DRAWING);
        testround.setEndsAt("13:55:00");
        testround.setPlayers(players);
        testround.setSelection(words);
        testround.setWords(words);
        testround.setGotPoints(points);
        testround.setHasDrawn(drawn);
        testround.setHasGuessed(guessed);
        testround.setWord(words.get(0));





        // when -> any object is being save in the userRepository -> return the dummy testUser and the dummy testlobby
        Mockito.when(roundRepository.save(Mockito.any())).thenReturn(testround);
        Mockito.when(roundRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testround));
        Mockito.when(roundRepository.saveAndFlush(Mockito.any())).thenReturn(testround);
        Mockito.when(gameRepository.saveAndFlush(Mockito.any())).thenReturn(testGame);
    }

    @Test
    public void GetRound_byRoundID_success(){
        Round foundround = roundService.getRound(testround.getId());

        assertEquals(testround.getId(), foundround.getId());
        assertEquals(testround.getDrawerName(), foundround.getDrawerName());
        assertEquals(testround.getIndex(), foundround.getIndex());
        assertEquals(testround.getStatus(), foundround.getStatus());
        assertEquals(testround.getPlayers(), foundround.getPlayers());
        assertEquals(testround.getEndsAt(), foundround.getEndsAt());
        assertEquals(testround.getSelection(), foundround.getSelection());
        assertEquals(testround.getHasDrawn(), foundround.getHasDrawn());
        assertEquals(testround.getHasGuessed(), foundround.getHasGuessed());
        assertEquals(testround.getWord(), foundround.getWord());
        assertEquals(testround.getGotPoints(), foundround.getGotPoints());




    }
    @Test
    public void GetRound_byRoundID_failed(){
        Mockito.when(roundRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> roundService.getRound(6L));

    }
    @Test
    public void createRound_successfull(){

        Round createdround = roundService.createRound(testGame);
        Mockito.verify(drawingRepository).flush();

        assertEquals(testGame.getPlayers(), createdround.getPlayers());
        assertEquals(RoundStatus.DRAWING, createdround.getStatus());
        assertEquals(testround.getIndex(), createdround.getIndex());
        assertEquals(testGame.getRoundId(),createdround.getId());


    }

    @Test
    void setNewPainter_success(){
        boolean [] drawn = {true,false,true,true};
        testround.setHasDrawn(drawn);

        roundService.setNewPainter(testround);

        assertEquals(testround.getDrawerName(), testround.getPlayers().get(1));
        assertTrue(testround.getHasDrawn()[1]);

    }
    @Test
    void IndexTest_success(){
        Mockito.when(roundRepository.saveAndFlush(Mockito.any())).thenReturn(testround);
        roundService.setRoundIndex(testround, 1);

        assertEquals(1, testround.getIndex());


    }
    @Test
    void Indextestfails(){
        roundService.setRoundIndex(testround, 6);
        Mockito.verify(roundRepository, Mockito.never()).saveAndFlush(Mockito.any());
        assertEquals(2, testround.getIndex());
    }
    @Test
    void testChangePhase_from_Drawing_to_selecting(){
        testround.setStatus(RoundStatus.DRAWING);

        roundService.changePhase(testround);
        Mockito.verify(roundRepository).saveAndFlush(Mockito.any());


        assertEquals(RoundStatus.SELECTING, testround.getStatus());
    }
    @Test
    void testChangePhase_from_selecting_to_drawing(){
        testround.setStatus(RoundStatus.SELECTING);

        roundService.changePhase(testround);
        Mockito.verify(roundRepository).saveAndFlush(Mockito.any());


        assertEquals(RoundStatus.DRAWING, testround.getStatus());
    }
    @Test
    void testroundEnd(){
        roundService.endRound(testround);
        Mockito.verify(roundRepository).saveAndFlush(Mockito.any());


        assertEquals(RoundStatus.DONE, testround.getStatus());

    }
    @Test
    void getChoices_success(){
        testround.setStatus(RoundStatus.SELECTING);
        ArrayList<String> choises = roundService.getChoices(testround, testround.getDrawerName());


        assertFalse(choises.isEmpty());
        assertTrue(testround.getWords().contains(choises.get(0)));
        assertTrue(testround.getWords().contains(choises.get(1)));
        assertTrue(testround.getWords().contains(choises.get(2)));
    }
    @Test
    void getChoice_failed_isDrawing(){
        ArrayList<String> choises = roundService.getChoices(testround, testround.getDrawerName());


        assertTrue(choises.isEmpty());
    }
    @Test
    void getChoice_failed_wrongPlayer(){
        testround.setStatus(RoundStatus.SELECTING);
        ArrayList<String> choises = roundService.getChoices(testround, testround.getPlayers().get(2));


        assertTrue(choises.isEmpty());
    }
    @Test
    void makechoice_success(){
        testround.setIndex(0);
        testround.setStatus(RoundStatus.SELECTING);
        testround.setWord(null);
        roundService.makeChoice(testround, testround.getDrawerName(), 0);

        assertEquals(testround.getWords().get(0), testround.getWord());

    }


    }
