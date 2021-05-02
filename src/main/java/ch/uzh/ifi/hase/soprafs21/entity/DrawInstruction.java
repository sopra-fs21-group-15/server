package ch.uzh.ifi.hase.soprafs21.entity;
import ch.uzh.ifi.hase.soprafs21.constant.Colours;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "DRAWINSTRUCTION")
public class DrawInstruction implements Serializable {

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime timeStamp;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private String colour;

    // generic methods to handle incoming requests
    public String getSender() { return this.sender; }
    public void setSender(String sender) { this.sender = sender; }

    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return this.y; }
    public void setY(int y) { this.y = y; }

    public LocalDateTime getTimeStamp() { return this.timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public int getSize() { return this.size; }
    public void setSize(int size) { this.y = y; }

    public String getColour() { return this.colour; }
    public void setColour(String colour) { this.colour = colour; }

    // // constructor for all the setup
    // public DrawInstruction(int x, int y, int size, String c) {
    //     this.x = x;
    //     this.y = y;
    //     this.timeStamp = LocalDateTime.now();
    //     this.size = size;
    //     this.colour = c;
    // }
}
