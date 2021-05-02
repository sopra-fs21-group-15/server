package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

public class BrushStrokePutDTO {

    // private long user_id;
    private String userName;
    private int x;
    private int y;
    private int size;
    private Colours colour;

    // methods for user_id
    //public long getUser_id() { return this.user_id; }
    public String getUserName() { return this.userName; }
    //public void setUser_id(long user_id) { this.user_id = user_id; }
    public void setUserName(String userName) { this.userName = userName; }

    // methods for x
    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; }

    // methods for y
    public int getY() { return this.y; }
    public void setYV(int y) { this.y = y; }

    // methods for size
    public int getSize() { return this.size; }
    public void setSize(int size) { this.y = y; }

    // methods for colour
    public Colours getColour() { return this.colour; }
    public void setColour(Colours colour) { this.colour = colour; }

}
