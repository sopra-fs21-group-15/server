package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Timer;
import ch.uzh.ifi.hase.soprafs21.helper.Standard;
import ch.uzh.ifi.hase.soprafs21.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, nonExistingTimer);
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
            timer.setStart(LocalTime.now(ZoneId.of("UTC")));
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

    // check if the time we were supposed to wait has passed
    public boolean done(Timer timer) { return remainingTime(timer) == 0 && !isReady(timer); }

    // get the time at which this round will end
    public String getEnd(Timer timer) {
        Long time = (long) remainingTime(timer);
        LocalDateTime end = LocalDateTime.now(ZoneId.of("UTC")).plus(time, ChronoUnit.MILLIS);
        DateTimeFormatter formatter = new Standard().getDateTimeFormatter();
        return end.format(formatter);
    }

    /** returns the remaining time by looking at the current mode (drawing or selecting), the starting time and the
     * current time to determine the remaining time in seconds and milliseconds in this mode.
     * @param timer = we have to give the method the timer of whom
     *              we would like to know the remaining time
     * @return remaining time in this mode
     */
    public int remainingTime(Timer timer){
        LocalTime rightNow = LocalTime.now(ZoneId.of("UTC"));
        return remainingTime(timer,rightNow);
    }

    /** returns the remaining time by looking at the current mode (usually drawing), the starting time and the
     * time past down to determine the remaining time in seconds and milliseconds in this mode.
     * @param timer = we have to give the method the timer of whom
     *              we would like to know the remaining time
     * @param timeStamp = the time we would like it to get compared to
     * @return remaining time in this mode
     */
    public int remainingTime(Timer timer, LocalTime timeStamp){
        double value = 0, tempMilli, tempSec, tempMin, tempHour, time;
        if(!isReady(timer)) {
            if(timer.getIsDrawing()) {
                time = timer.getDrawingTimeSpan();
            } else {
                time = timer.getSelectTimeSpan();
            }
            tempMilli = Math.floor((timeStamp.getNano() - timer.getStart().getNano()) / Math.pow(10.0,6.0)) / Math.pow(10.0,3.0);
            tempSec = timeStamp.getSecond() - timer.getStart().getSecond();
            tempMin = (timeStamp.getMinute() - timer.getStart().getMinute()) * 60;
            tempHour = (timeStamp.getHour() - timer.getStart().getHour()) * 3600;
            value = Math.floor((time - (tempHour + tempMin + tempSec + tempMilli)) * 1000);
        }
        return (int) Math.max(0,value);
    }


}
