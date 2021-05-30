package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.constant.Colours;
import ch.uzh.ifi.hase.soprafs21.constant.RoundStatus;
import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.repository.BrushStrokeRepository;
import ch.uzh.ifi.hase.soprafs21.repository.DrawingRepository;
import ch.uzh.ifi.hase.soprafs21.repository.RoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        testBrushstroke.setTimeStamp("2021-05-17 14:57:10:000");

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
        Mockito.when(drawingRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(testdrawing));
        Mockito.when(brushStrokeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.ofNullable(testBrushstroke));

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
    void getDrawing_byID_success(){
        Drawing foundDrawing= drawingService.getDrawing(testdrawing.getId());

        assertEquals(testdrawing.getBrushStrokes(), foundDrawing.getBrushStrokes());
        assertEquals(testdrawing.getDrawerName(), foundDrawing.getDrawerName());
        assertEquals(testdrawing.getId(), foundDrawing.getId());


    }

    @Test
    void getDrawing_byID_failed() {
        Mockito.when(drawingRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(ResponseStatusException.class, () -> drawingService.getDrawing(6L));
    }





    @Test
    void test_the_save_of_thebrushStrokes() {
        BrushStroke Stroke2 = new BrushStroke();
        Stroke2.setId(3L);
        Stroke2.setColour(Colours.BLACK.toString());
        Stroke2.setSize(4);
        Stroke2.setY(5);
        Stroke2.setX(22);
        Stroke2.setTimeStamp("2021-05-17 14:57:10:100");

        ArrayList<BrushStroke> newStrokes = new ArrayList<>();
        newStrokes.add(testBrushstroke);
        newStrokes.add(Stroke2);

        Mockito.when(drawingRepository.saveAndFlush(Mockito.any())).thenReturn(testdrawing);
        drawingService.addStrokes(testdrawing, newStrokes);
        assertTrue(Stroke2.toString().contains("x = 22"));
        assertTrue(testdrawing.getBrushStrokes().contains(testBrushstroke));
        assertTrue(testdrawing.getBrushStrokes().contains(Stroke2));
    }

    @Test
    void get_thenewDrawings() {
        List<BrushStroke> ThreeStroke = new ArrayList<>();
        testBrushstroke.setTimeStamp("2021-05-17 14:57:10:000");
        ThreeStroke.add(testBrushstroke);

        BrushStroke Stroke2 = new BrushStroke();
        Stroke2.setId(3L);
        Stroke2.setColour(Colours.BLACK.toString());
        Stroke2.setSize(4);
        Stroke2.setY(5);
        Stroke2.setX(22);
        Stroke2.setTimeStamp("2021-05-17 14:57:12:100");

        BrushStroke Stroke3 = new BrushStroke();
        Stroke3.setId(3L);
        Stroke3.setColour(Colours.BLACK.toString());
        Stroke3.setSize(4);
        Stroke3.setY(5);
        Stroke3.setX(22);
        Stroke3.setTimeStamp("2021-05-17 14:57:11:000");

         ThreeStroke.add(Stroke2);
         ThreeStroke.add(Stroke3);

        testdrawing.setBrushStrokes(ThreeStroke);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        LocalDateTime dateTime = LocalDateTime.parse("2021-05-17 14:57:11:000", formatter);

        List<BrushStroke> emptylist = drawingService.getDrawing(testdrawing, dateTime);


        assertFalse(emptylist.contains(testBrushstroke));
        assertTrue(emptylist.contains(Stroke3));
        assertTrue(emptylist.contains(Stroke2));

    }

    @Test
    void get_no_newDrawings_Emptydrawing() {
        List<BrushStroke> empty = new ArrayList<>();
        testdrawing.setBrushStrokes(empty);
        List<BrushStroke> emptylist = drawingService.getDrawing(testdrawing, LocalDateTime.now());
        assertTrue(emptylist.isEmpty());

    }
}

