package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class DrawingGetDTO {

    private int x;
    private int y;
    private String timeStamp;
    private int size;
    private String colour;

    // methods for x
    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; }

    // methods for y
    public int getY() { return this.y; }
    public void setY(int y) { this.y = y; }

    // methods for timeStamp
    public String getTimeStamp() { return this.timeStamp; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    // methods for size
    public int getSize() { return this.size; }
    public void setSize(int size) { this.size = size; }

    // methods for colour
    public String getColour() { return this.colour; }
    public void setColour(String colour) { this.colour = colour; }
}
