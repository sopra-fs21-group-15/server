package ch.uzh.ifi.hase.soprafs21.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

public class Drawing {

    // fields described by Kilian
    private long drawerId;
    private ArrayList<BrushStroke> brushStrokes = new ArrayList<BrushStroke>();

    // generic methods to handle incoming requests
    public long getDrawerId() { return this.drawerId; }
    public void setDrawerId(long newDrawerId) { this.drawerId = newDrawerId; };

    public ArrayList<BrushStroke> getBrushStrokes() { return this.brushStrokes; }
    public void setBrushStrokes(ArrayList<BrushStroke> brushStrokes) { this.brushStrokes = brushStrokes; };

    // one that does it all
    public void addStroke(int x, int y, LocalDateTime timeStamp, int size, Colours c) {
        brushStrokes.add(new BrushStroke(x,y,timeStamp,size,c));
    }

}
