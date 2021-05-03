package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "BRUSHSTROKE")
public class BrushStroke implements Serializable {

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

    // constructor for all the setup
    public BrushStroke(int x, int y, int size, String c) {
        // trivial fields
        this.x = x;
        this.y = y;
        this.size = size;
        this.colour = c;

        // the time it was created
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.timeStamp = currentTime.format(formatter);
    }

    public BrushStroke() {
        new BrushStroke(0,0,0,"");
    }

}
