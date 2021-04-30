package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Table(name = "TIMER")
public class Timer implements Serializable {

    private  static final long serialVersionUID = 1L;

    // basic fields
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int timeSpan;

    @Column(nullable = false)
    private LocalTime start;

    // constructor with default values
    public Timer() {
        int defaultTime = 60;
        new Timer(defaultTime);
    }

    // constructor with options
    public Timer(int timeSpan){
        this.timeSpan = timeSpan;
        this.start = null;
    }

    // access and change id
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    // access and change time span for this timer (only if not running)
    public int getTimeSpan() { return this.timeSpan; }
    public void setTimeSpan(int newTimeSpan ) { if(this.ready()) { this.timeSpan = newTimeSpan; } }

    // start the timer if it isn't already running
    public void start(){
        if(this.ready()) {
            this.start = LocalTime.now();
        }
    }

    // reset the timer in order to use it again
    public void reset(){
        if(!this.ready()) {
            this.start = null;
        }
    }

    // a method to check if the Timer is ready to be used
    public boolean ready() {
        return this.start == null;
    }

    // return the remaining time this
    public double remainingTime(){
        if(this.start != null) {
            LocalTime rightNow = LocalTime.now();
            double tempMilli = Math.floor((rightNow.getNano() - start.getNano()) / Math.pow(10.0,6.0)) / Math.pow(10.0,3.0);
            double tempSec = ((double) timeSpan) - (rightNow.getSecond() - start.getSecond());
            double value = Math.floor((timeSpan - (tempSec + tempMilli)) * 1000) / 1000;
            if(value >= 0){
                return value;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    // checking if there is still time left
    public boolean timeIsUp() {
        if(!this.ready()) {
            return this.remainingTime() == 0;
        } else {
            return false;
        }
    }

    // classical toString method for debugging
    public String toString() {
        String value = "Timer: { timeSpan = " + timeSpan + "[s], isRunning = ";
        if(!this.ready()) {
            value += "TRUE";
        } else {
            value += "FALSE";
        }
        value += ", timeIsUp = ";
        if(this.timeIsUp()) {
            value += "TRUE";
        } else {
            value += "FALSE";
        }
        return value += ", remainingTime = " + this.remainingTime() + "[s] }";
    }
}
