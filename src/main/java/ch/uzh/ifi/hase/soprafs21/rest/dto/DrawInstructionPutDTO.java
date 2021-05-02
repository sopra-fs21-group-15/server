package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class DrawInstructionPutDTO {

    // private long user_id;
    private String sender;
    private int x;
    private int y;
    private int size;
    private String colour;

    // methods for user_id
    public String getSender() { return this.sender; }
    public void setSender(String sender) { this.sender = sender; }

    // methods for x
    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; }

    // methods for y
    public int getY() { return this.y; }
    public void setY(int y) { this.y = y; }

    // methods for size
    public int getSize() { return this.size; }
    public void setSize(int size) { this.y = y; }

    // methods for colour
    public String getColour() { return this.colour; }
    public void setColour(String colour) { this.colour = colour; }

    public String toString() {
      String ans = "";
      ans += "Sender:" + this.sender + "\n";
      ans += "x:" + this.x + "\n";
      ans += "y:" + this.y + "\n";
      ans += "size:" + this.size + "\n";
      ans += "colour:" + this.colour;
      return ans;
    }
}
