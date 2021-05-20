package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.helper.Standard;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "TIMER")
public class Timer implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Basic fields for the functionality and organisation of the timer
     * @param timerId = id to find the correct timer in the repository
     * @param drawingTimeSpan = time to draw the picture (gets set by the api caller)
     * @param selectTimeSpan = time to select the word you (gets set by the standards defined in the respective class)
     * @param start = time at which the timer was started
     */
    @Id
    @GeneratedValue
    private Long id;

    //@OneToOne
    //private Long gameId;

    @Column(nullable = false)
    private int drawingTimeSpan;

    @Column(nullable = false)
    private int selectTimeSpan;

    @Column(nullable = false)
    private boolean isDrawing;

    @Column(nullable = true)
    private LocalTime start;

    /** Constructor for the timer
     * - without parameters, because it is needed for the mapper to work properly
     * - with parameters, so the users can change the time with the settings in the lobby
     */
    public Timer() { // default constructor for mapper
        int defaultTime = 60;
        new Timer(defaultTime);
    }

    public Timer(int drawingTimeSpan){ // constructor with options for callers
        this.drawingTimeSpan = drawingTimeSpan; // drawingTimeSpan;
        this.selectTimeSpan = new Standard().getTimeToSelect();
        this.isDrawing = false;
        this.start = null;
    }

    /** Basic getter and setter methods for the Timer */
    // access and change id
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    // access and change the id of the game that ones this timer
    //public Long getGameId() { return this.gameId; }
    //public void setGameId(Long id) { this.gameId = gameId; }

    // access and change drawing time span
    public int getDrawingTimeSpan() { return this.drawingTimeSpan; }
    public void setDrawingTimeSpan(int newDrawingTimeSpan) { this.drawingTimeSpan = drawingTimeSpan; }

    // access and change selection time span
    public int getSelectTimeSpan() { return this.selectTimeSpan; }
    public void setSelectTimeSpan(int selectTimeSpan) { this.selectTimeSpan = selectTimeSpan; }

    // access and change if the timer is measuring the selection time or the drawing time
    public boolean getIsDrawing() { return this.isDrawing; }
    public void setIsDrawing(boolean isDrawing) { this.isDrawing = isDrawing; }

    // access when the timer was started
    public LocalTime getStart() { return this.start; }
    public void setStart(LocalTime start) { this.start = start; }

    /** helper methods for debugging */
    // classical toString method for debugging
    public String toString() {
        String mode, totalTime, state;
        Double time;
        if(isDrawing) {
            mode = "drawing";
            totalTime = "" + drawingTimeSpan;
            time = (double) getDrawingTimeSpan();
        } else {
            mode = "selecting";
            totalTime = "" + selectTimeSpan;
            time = (double) getSelectTimeSpan();
        }
        if(start == null) {
            state = "can be run";
        } else {
            state = "is running, remaining time: ";
            LocalTime rightNow = LocalTime.now();
            double tempMilli = Math.floor((rightNow.getNano() - getStart().getNano()) / Math.pow(10.0,6.0)) / Math.pow(10.0,3.0);
            double tempSec = rightNow.getSecond() - start.getSecond();
            double value = Math.floor((time - (tempSec + tempMilli)) * 1000) / 1000;
            if(value >= 0){
                state += "" + value + "[s]";
            } else {
                state += "time is up";
            }
        }
        return "Timer: { currently " + mode + ", total time: " + totalTime + "[s], " + state + " }";
    }
}
