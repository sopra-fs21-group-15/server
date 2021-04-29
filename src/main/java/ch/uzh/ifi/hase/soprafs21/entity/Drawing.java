package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

public class Drawing {

    // fields described by Kilian
    private long drawerId;
    private ArrayList<Integer> xValues = new ArrayList<Integer>();
    private ArrayList<Integer> yValues = new ArrayList<Integer>();
    private ArrayList<Integer> sizeValues = new ArrayList<Integer>();
    private ArrayList<Colours> colourValues = new ArrayList<Colours>();

    // generic methods to handle incoming requests
    public long getDrawerId() { return this.drawerId; }
    public void setDrawerId(long newDrawerId) { this.drawerId = newDrawerId; };

    public ArrayList<Integer> getXValues() { return this.xValues; }
    public void setXValues(ArrayList<Integer> newXValues) { this.xValues = newXValues; };

    public ArrayList<Integer> getYValues() { return this.yValues; }
    public void setYValues(ArrayList<Integer> newYValues) { this.yValues = newYValues; };

    public ArrayList<Integer> getSizeValues() { return this.sizeValues; }
    public void setSizeValues(ArrayList<Integer> newSizeValues) { this.yValues = newSizeValues; };

    public ArrayList<Colours> getColourValues() { return this.colourValues; }
    public void setColourValues(ArrayList<Colours> newColourValues) { this.colourValues = newColourValues; };

    // specific methods to handle mentioned calls
    public void addXValues(Integer x) { this.xValues.add(x); }
    public void addYValues(Integer y) { this.yValues.add(y); }
    public void addSizeValues(Integer size) { this.sizeValues.add(size); }
    public void addColourValues(Colours c) { this.colourValues.add(c); }

    // one that does it all
    public void addStroke(Integer x, Integer y, Integer size, Colours c) {
        this.xValues.add(x);
        this.yValues.add(y);
        this.sizeValues.add(size);
        this.colourValues.add(c);
    }

}
