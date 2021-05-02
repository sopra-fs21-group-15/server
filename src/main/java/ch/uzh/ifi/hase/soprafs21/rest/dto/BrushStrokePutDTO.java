package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

public class BrushStrokePutDTO {

    private String userName;
    private int x;
    private int y;
    private int size;
    private String colour;

    // methods for userName
    public String getUserName() { return this.userName; }
    public void setUserName(String userName) { this.userName = userName; }

    // methods for x
    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; }

    // methods for y
    public int getY() { return this.y; }
    public void setY(int y) { this.y = y; }

    // methods for size
    public int getSize() { return this.size; }
    public void setSize(int size) { this.size = size; }

    // methods for colour
    public String getColour() { return this.colour; }
    public void setColour(String colour) { this.colour = colour; }

}
