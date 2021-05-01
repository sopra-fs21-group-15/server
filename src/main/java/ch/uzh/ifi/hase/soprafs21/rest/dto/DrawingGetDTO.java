package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;

import java.util.ArrayList;

public class DrawingGetDTO {

    private ArrayList<BrushStroke> brushStrokes;

    // methods to access brushStrokes
    public ArrayList<BrushStroke> getBrushStrokes() { return brushStrokes; }
    public void setBrushStrokes(ArrayList<BrushStroke> brushStrokes) { this.brushStrokes = brushStrokes; }

}
