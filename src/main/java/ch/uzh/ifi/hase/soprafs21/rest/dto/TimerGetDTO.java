package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.time.LocalTime;

public class TimerGetDTO {

    private int timeSpan;
    private LocalTime start;

    // methods for timeSpan
    public int getTimeSpan() { return timeSpan; }
    public void setTimeSpan(int timeSpan) { this.timeSpan = timeSpan; }

    // method for start
    public LocalTime getStart() { return start; }
    public void setStart(LocalTime start) { this.start = start; }

}
