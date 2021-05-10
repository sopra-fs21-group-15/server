package ch.uzh.ifi.hase.soprafs21.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class MessageGetDTO {

    private String writerId;

    private String message;

    @JsonFormat(pattern="hh:mm:ss")
    private LocalTime timeStamp;

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}