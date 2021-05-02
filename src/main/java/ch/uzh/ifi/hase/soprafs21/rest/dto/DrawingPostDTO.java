package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.time.LocalDateTime;

public class DrawingPostDTO {

    private String timeStamp;

    // methods to access timestamp
    public String getTimeStamp() { return timeStamp; }
    public void setTimeStamp(String timestamp) { this.timeStamp = timestamp; }

}
