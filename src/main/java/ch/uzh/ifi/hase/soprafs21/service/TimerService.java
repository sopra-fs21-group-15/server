package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Timer;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.Optional;

@Service
@Transactional
public class TimerService {

    private final TimerRepository timerRepository;
    private final RoundRepository roundRepository;
    private final GameRepository gameRepository;

    @Autowired
    public TimerService(@Qualifier("timerRepository") TimerRepository timerRepository, RoundRepository roundRepository, GameRepository gameRepository) {
        this.timerRepository = timerRepository;
        this.roundRepository = roundRepository;
        this.gameRepository = gameRepository;
    }

    // get a specific timer
    public Timer getTimer(Long timerId) {
        Optional<Timer> potTimer = timerRepository.findById(timerId);
        Timer value = null;

        if (potTimer.isEmpty()) { // if not found
            String nonExistingTimer = "The timer you have been looking for does not exist. Please search for an existing timer.";
            new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(nonExistingTimer));
        } else { // if found
            value = potTimer.get();
        }

        return value;
    }

    // change phase
    public void changePhase(Timer timer) {
        if(timer.getIsDrawing()) {
            timer.setIsDrawing(false);
        } else {
            timer.setIsDrawing(true);
        }
        timerRepository.saveAndFlush(timer);
    }

    // create a new timer
    public Timer createTimer(int drawingTimeSpan) {
        Timer timer = new Timer(drawingTimeSpan);
        timer = timerRepository.save(timer);
        timerRepository.flush();
        return timer;
    }

    // start the timer if it is not already running
    public void begin(Timer timer){
        if(isReady(timer)) {
            timer.setStart(LocalTime.now());
            timerRepository.saveAndFlush(timer);
        }
    }

    // reset the timer in order to use it again
    public void reset(Timer timer){
        if(!isReady(timer)) {
            timer.setStart(null);
            timerRepository.saveAndFlush(timer);
        }
    }

    // check if the timer is ready to be started
    public boolean isReady(Timer timer) {
        return timer.getStart() == null;
    }

    /** returns the remaining time
     * looks at the current mode (drawing or selecting), the starting time and the
     * current time to determine the remaining time in seconds and milliseconds in
     * this mode.
     * @param timer = we have to give the method the timer of whom
     *              we would like to know the remaining time
     * @return remaining time in this mode
     */
    public int remainingTime(Timer timer){
        double value = 0, tempMilli, tempSec, time;
        if(!isReady(timer)) {
            if(timer.getIsDrawing()) {
                time = timer.getDrawingTimeSpan();
            } else {
                time = timer.getSelectTimeSpan();
            }
            LocalTime rightNow = LocalTime.now();
            tempMilli = Math.floor((rightNow.getNano() - timer.getStart().getNano()) / Math.pow(10.0,6.0)) / Math.pow(10.0,3.0);
            tempSec = rightNow.getSecond() - timer.getStart().getSecond();
            value = Math.floor((time - (tempSec + tempMilli)) * 1000);
        }
        return (int) Math.max(0,value);
    }

}
