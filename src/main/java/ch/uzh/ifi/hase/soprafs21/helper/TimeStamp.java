package ch.uzh.ifi.hase.soprafs21.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeStamp {

    private String timeString;

    private LocalDateTime timeObject;

    // methods to access time as a string
    public String getTimeString() { return timeString; }
    public void setTimeString(String timeString) {
        this.timeString = timeString;

        // set the object to the same value
        DateTimeFormatter formatter = new Standard().getDateTimeFormatter();
        this.timeObject = LocalDateTime.parse(timeString,formatter);
    }

    // methods to access time as a local date time object
    public LocalDateTime getTimeObject() { return timeObject; }
    public void setTimeObject(LocalDateTime timeObject) {
        this.timeObject = timeObject;

        // set the string to the same value
        DateTimeFormatter formatter = new Standard().getDateTimeFormatter();
        this.timeString = timeObject.format(formatter);
    }

    public String toString() {
        String value = "TimeStamp : { ";
        value += "" + timeString + ", ";
        value += "" + timeObject.toString() + " }";
        return value;
    }

}
