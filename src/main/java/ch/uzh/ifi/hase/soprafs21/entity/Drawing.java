package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Drawing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private long drawerId;

    private ArrayList<BrushStroke> brushStrokes = new ArrayList<>();

    // generic methods to handle incoming requests
    public long getDrawerId() { return this.drawerId; }
    public void setDrawerId(long newDrawerId) { this.drawerId = newDrawerId; };

    public ArrayList<BrushStroke> getBrushStrokes() { return this.brushStrokes; }
    public void setBrushStrokes(ArrayList<BrushStroke> brushStrokes) { this.brushStrokes = brushStrokes; };

    // one that does it all
    public void addStroke(BrushStroke brushStroke) {
        brushStrokes.add(brushStroke);
    }

    public Drawing getDrawing(LocalDateTime timeStamp) {
        int index = 0;
        while(brushStrokes.get(index).getTimeStamp().isBefore(timeStamp)) {
            index++;
        }
        ArrayList<BrushStroke> temp = new ArrayList<BrushStroke>(brushStrokes.subList(index,brushStrokes.size()-1));
        Drawing value = new Drawing();
        value.setBrushStrokes(temp);
        value.setDrawerId(0);
        return value;
    }

}
