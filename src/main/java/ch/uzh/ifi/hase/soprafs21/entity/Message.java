package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    @Id
    @GeneratedValue
    private LocalDateTime timeStamp;

    @Column(nullable = false)
    private Long writer_id;

    @Column(nullable = false)
    private String message;

    // generic methods to handle incoming requests
    public LocalDateTime getTimeStamp() { return this.timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public Long getWriter_id() { return this.writer_id; }

    public void setWriter_id(Long writer_id) { this.writer_id = writer_id; }

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message;    }

    // constructor for all the setup
    public Message(String message, Long writer_id) {
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.writer_id = writer_id;
    }

}
