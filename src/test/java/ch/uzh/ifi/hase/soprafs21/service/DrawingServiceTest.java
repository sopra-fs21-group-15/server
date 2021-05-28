package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.Colours;
import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.BrushStrokeRepository;
import ch.uzh.ifi.hase.soprafs21.repository.DrawingRepository;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class DrawingServiceTest {

    @Mock
    private RoundRepository roundRepository;
    @Mock
    private DrawingRepository drawingRepository;
    @Mock
    private BrushStrokeRepository brushStrokeRepository;


    @InjectMocks
    private DrawingService drawingService;
    private Round testround;
    private BrushStroke testBrushstroke;
    private Drawing testdrawing;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        ArrayList<String> players = new ArrayList();
        players.add("User1");
        players.add("User2");
        players.add("User3");
        players.add("User4");

        ArrayList<String> words = new ArrayList();
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
        int[] points = {10, 20, 30, 40};
        boolean[] drawn = {false, false, false, false};
        boolean[] guessed = {false, false, false, false};

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


        testBrushstroke = new BrushStroke();
        testBrushstroke.setId(2L);
        testBrushstroke.setColour(Colours.BLACK.toString());
        testBrushstroke.setSize(3);
        testBrushstroke.setY(2);
        testBrushstroke.setX(15);
        testBrushstroke.setTimeStamp("13:54:00");

        ArrayList<BrushStroke> brushstrokes = new ArrayList();
        brushstrokes.add(testBrushstroke);

        testdrawing = new Drawing();
        testdrawing.setId(1L);
        testdrawing.setDrawerName(testround.getDrawerName());
        testdrawing.setBrushStrokes(brushstrokes);





        // when -> any object is being save in the userRepository -> return the dummy testUser and the dummy testlobby
        Mockito.when(roundRepository.findAll()).thenReturn(Collections.singletonList(testround));
        Mockito.when(drawingRepository.findAll()).thenReturn(Collections.singletonList(testdrawing));
        Mockito.when(brushStrokeRepository.findAll()).thenReturn(Collections.singletonList(testBrushstroke));

    }

    @Test
    void GetRound_byRoundID_success() {
        Round foundround = drawingService.getRound(testround.getId());

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
    void GetRound_byRoundID_failed() {
        Mockito.when(roundRepository.findAll()).thenReturn(Collections.emptyList());

        assertNull(drawingService.getRound(6L));

    }
    @Test
    void getBrushstroke_byID_success(){
        BrushStroke foundBrushstroke = drawingService.getBrushStroke(testBrushstroke.getId());

        assertEquals(testBrushstroke.getTimeStamp(), foundBrushstroke.getTimeStamp());
        assertEquals(testBrushstroke.getColour(), foundBrushstroke.getColour());
        assertEquals(testBrushstroke.getY(), foundBrushstroke.getY());
        assertEquals(testBrushstroke.getSize(), foundBrushstroke.getSize());
        assertEquals(testBrushstroke.getX(),foundBrushstroke.getX());
        assertEquals(testBrushstroke.getId(),foundBrushstroke.getId());


    }
    @Test
    void getBrushstroke_byID_failed() {
        Mockito.when(brushStrokeRepository.findAll()).thenReturn(Collections.emptyList());

        assertNull(drawingService.getBrushStroke(6L));

    }

    @Test
    void getDrawing_byID_success(){
        Drawing foundDrawing = drawingService.getDrawing(testdrawing.getId());

        assertEquals(testdrawing.getId(), foundDrawing.getId());
        assertEquals(testdrawing.getDrawerName(), testdrawing.getDrawerName());
        assertEquals(testdrawing.getBrushStrokes(), foundDrawing.getBrushStrokes());


    }
    @Test
    void getDrawing_byID_failed() {
        Mockito.when(drawingRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> drawingService.getDrawing(6L));
    }


    }

