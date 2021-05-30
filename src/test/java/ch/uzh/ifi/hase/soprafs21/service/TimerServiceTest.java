package ch.uzh.ifi.hase.soprafs21.service;


import ch.uzh.ifi.hase.soprafs21.entity.Timer;
import ch.uzh.ifi.hase.soprafs21.entity.Message;;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.repository.TimerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//I commented this test out, since I could not fix it and it was not required for this milestone!
public class TimerServiceTest {

    @Mock
    private TimerRepository timerRepository;




    @InjectMocks
    private TimerService timerService;
    private Timer testTimer;
    private Message testMessage;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testTimer = new Timer(60);
        testTimer.setId(1L);
        testTimer.setSelectTimeSpan(25);
        testTimer.setIsDrawing(true);


        // when -> any object is being save in the userRepository -> return the dummy testUser and the dummy testlobby
        Mockito.when(timerRepository.save(Mockito.any())).thenReturn(testTimer);
        Mockito.when(timerRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testTimer));
        Mockito.when(timerRepository.saveAndFlush(Mockito.any())).thenReturn(testTimer);

    }

    @Test
     void getTimer_By_ID_Success(){
        Timer foundTimer = timerService.getTimer(testTimer.getId());

        assertEquals(foundTimer.getId(), testTimer.getId());
        assertEquals(foundTimer.getDrawingTimeSpan(), testTimer.getDrawingTimeSpan());
        assertEquals(foundTimer.getSelectTimeSpan(), testTimer.getSelectTimeSpan());
        assertEquals(foundTimer.getIsDrawing(), testTimer.getIsDrawing());

    }
    @Test
     void getTimer_By_ID_failed(){
        Mockito.when(timerRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> timerService.getTimer(6L));
    }
    @Test
     void Changetonotdrawing(){
        timerService.changePhase(testTimer);

        assertFalse(testTimer.getIsDrawing());

    }
    @Test
     void changeto_DrawingPhase(){
        testTimer.setIsDrawing(false);

        timerService.changePhase(testTimer);
        assertTrue(testTimer.getIsDrawing());

    }

    @Test
     void create_new_Timer(){
        testTimer.setDrawingTimeSpan(50);
        Mockito.when(timerRepository.save(Mockito.any())).thenReturn(testTimer);

        Timer newTimer = timerService.createTimer(50);

        assertEquals(newTimer.getDrawingTimeSpan(), testTimer.getDrawingTimeSpan());
        assertEquals(newTimer.getSelectTimeSpan(), testTimer.getSelectTimeSpan());
        assertEquals(newTimer.getIsDrawing(), testTimer.getIsDrawing());
        assertTrue(testTimer.toString().contains("drawing"));
    }

    @Test
    void create_new_Timer2(){
        testTimer.setIsDrawing(false);
        testTimer.setDrawingTimeSpan(50);
        Mockito.when(timerRepository.save(Mockito.any())).thenReturn(testTimer);

        Timer newTimer = timerService.createTimer(50);

        assertEquals(newTimer.getDrawingTimeSpan(), testTimer.getDrawingTimeSpan());
        assertEquals(newTimer.getSelectTimeSpan(), testTimer.getSelectTimeSpan());
        assertEquals(newTimer.getIsDrawing(), testTimer.getIsDrawing());
        assertTrue(testTimer.toString().contains("selecting"));
    }
    @Test
    void create_new_Timer3(){
        testTimer.setStart(null);
        testTimer.setDrawingTimeSpan(50);
        Mockito.when(timerRepository.save(Mockito.any())).thenReturn(testTimer);

        Timer newTimer = timerService.createTimer(50);

        assertEquals(newTimer.getDrawingTimeSpan(), testTimer.getDrawingTimeSpan());
        assertEquals(newTimer.getSelectTimeSpan(), testTimer.getSelectTimeSpan());
        assertEquals(newTimer.getIsDrawing(), testTimer.getIsDrawing());
        assertTrue(testTimer.toString().contains("can be run"));
    }

    @Test
     void testbegin(){
        testTimer.setStart(null);
        timerService.begin(testTimer);

        assertNotNull(testTimer.getStart());

    }

    @Test
     void testbegin_secondcondition(){
        timerService.begin(testTimer);
        timerService.begin(testTimer);
        assertNotNull(testTimer.getStart());
    }

    @Test
     void testreset(){
        timerService.begin(testTimer);
        assertNotNull(testTimer.getStart());
        timerService.reset(testTimer);
        assertNull(testTimer.getStart());


    }
    @Test
     void testreset_secondcondition()    {
        testTimer.setStart(null);
        timerService.reset(testTimer);
        assertNull(testTimer.getStart());
    }

    @Test
     void testremainigtime_ofafinished_game(){
        int time = timerService.remainingTime(testTimer);

        assertEquals(time, 0);
    }

    @Test
     void testremainig_DrawingTime(){
        timerService.begin(testTimer);
        int time=timerService.remainingTime(testTimer);
        assertTrue(time>0);
        assertTrue(time/1000<=testTimer.getDrawingTimeSpan());
    }
    @Test
     void testremainig_Selectingtime(){
        testTimer.setIsDrawing(false);
        timerService.begin(testTimer);
        int time=timerService.remainingTime(testTimer);
        assertTrue(time>0);
        assertTrue(time/1000<=testTimer.getSelectTimeSpan());


    }




    @Test
     void test_getEnd(){
        Timer testtimer2 = new Timer(30);
        testtimer2.setSelectTimeSpan(25);
        testtimer2.setIsDrawing(true);


        timerService.begin(testTimer);
        timerService.begin(testtimer2);

        String End2 = timerService.getEnd(testtimer2);


        String End = timerService.getEnd(testTimer);

        assertNotEquals(End2, End);
        assertNotNull(End2);
        assertNotNull(End);


    }

}
