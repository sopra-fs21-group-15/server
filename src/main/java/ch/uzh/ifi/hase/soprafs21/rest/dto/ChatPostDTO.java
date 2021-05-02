package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.time.LocalDateTime;

public class ChatPostDTO {

    private String chat_message;
    private String writer_name;
    private LocalDateTime timestamp;

    // methods to access message
    public String getMessage() { return chat_message; }

    public void setMessage(String message) {
        this.chat_message = message;
    }

    // methods to access writer_id
    public String getUser_id() { return writer_name; }
    public void setUser_id(String writer_name) { this.writer_name = writer_name; }

    // methods to access timestamp
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}
