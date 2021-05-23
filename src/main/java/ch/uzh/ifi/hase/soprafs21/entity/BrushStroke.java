package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.helper.Standard;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "BRUSHSTROKE")
public class BrushStroke implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    private String timeStamp;

    @Column(nullable = false)
    private int size;

    @Column(nullable = false)
    private String colour;

    // generic methods to handle incoming requests
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return this.y; }
    public void setY(int y) { this.y = y; }

    public String getTimeStamp() { return this.timeStamp; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public int getSize() { return this.size; }
    public void setSize(int size) { this.size = size; }

    public String getColour() { return this.colour; }
    public void setColour(String colour) { this.colour = colour; }

    /**
     * Additional fundamental classes to for quality of life and basic functionality
     */
    // constructor for all the setup
    public BrushStroke(int x, int y, int size, String c) {
        // trivial fields
        this.x = x;
        this.y = y;
        this.size = size;
        this.colour = c;

        // the time it was created
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = new Standard().getDateTimeFormatter();
        this.timeStamp = currentTime.format(formatter);
    }

    // required constructor with no parameters so that it can get parsed by the mapper
    public BrushStroke() {
        new BrushStroke(0,0,0,"");
    }

    // needed class in order to sort a list of BrushStroke objects
    @Override
    public int compareTo(Object o) {
        // initialize a faulty value
        int value = 0;

        // check if a proper object has been passed down
        if(o instanceof BrushStroke) {
            // preparation
            BrushStroke other = (BrushStroke) o; // casting the object that was past down to brushStroke in order to access its methods
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"); // create a formatter to parse the timeStamp Strings through
            LocalDateTime thisTime = LocalDateTime.parse(this.getTimeStamp(), formatter); // parse this time to LocalDateTime
            LocalDateTime otherTime = LocalDateTime.parse(other.getTimeStamp(), formatter); // parse the other objects time to LocalDateTime

            // comparison
            if(thisTime.isBefore(otherTime)) {
                value = -1;
            } else if (thisTime.isEqual(otherTime)) {
                value = 0;
            } else {
                value = 1;
            }
        } else {
            throw new ClassCastException();
        }

        return value;
    }

    // method for debugging
    public String toString() {
        String value = "BrushStroke ";

        value += "" + getId().toString() + " : { ";
        value += "x = " + getX() + ", ";
        value += "y = " + getY() + ", ";
        value += "time = " + getTimeStamp() + ", ";
        value += "size = " + getSize() + ", ";
        value += "colour = " + getColour() + " }";

        return value;
    }

}
