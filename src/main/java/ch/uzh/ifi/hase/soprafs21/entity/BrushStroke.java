package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

import java.time.LocalDateTime;

public class BrushStroke {

    private int x;
    private int y;
    private LocalDateTime timeStamp;
    private int size;
    private Colours colour;

    // generic methods to handle incoming requests
    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; };

    public int getY() { return this.y; }
    public void setYV(int y) { this.y = y; };

    public LocalDateTime getTimeStamp() { return this.timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public int getSize() { return this.size; }
    public void setSize(int size) { this.y = y; };

    public Colours getColour() { return this.colour; }
    public void setColour(Colours colour) { this.colour = colour; };

    // constructor for all the setup
    public BrushStroke(int x, int y, LocalDateTime timeStamp, int size, Colours c) {
        this.x = x;
        this.y = y;
        this.timeStamp = timeStamp;
        this.size = size;
        this.colour = c;
    }

}
