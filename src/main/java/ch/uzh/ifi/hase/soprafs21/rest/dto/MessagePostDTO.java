package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Message;

import java.time.LocalDateTime;

public class MessagePostDTO {

    private String message;
    private String writerName;
    private String timeStamp;

    // methods to access message
    public String getMessage() { return message; }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() { return timeStamp; }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getWriterName() { return writerName; }
}



