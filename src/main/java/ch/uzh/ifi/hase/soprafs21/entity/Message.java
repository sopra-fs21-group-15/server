package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {

    @Id
    @GeneratedValue
    private LocalDateTime timeStamp;

    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String message;

    // generic methods to handle incoming requests
    public LocalDateTime getTimeStamp() { return this.timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public Long getId() { return this.id; }

    public void setId(Long id) { this.id = id; }

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message;    }

    // constructor for all the setup
    public Message(String message, Long id) {
        this.timeStamp = LocalDateTime.now();
        this.message = message;
        this.id = id;
    }

}
