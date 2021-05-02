package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Table(name = "DRAWING")
public class Drawing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String drawerName;

    @Column(nullable = false)
    private ArrayList<Long> brushStrokeIds = new ArrayList<>();

    // generic methods to handle incoming requests
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getDrawerName() { return this.drawerName; }
    public void setDrawerName(String newDrawerName) { this.drawerName = newDrawerName; };

    public ArrayList<Long> getBrushStrokeIds() { return this.brushStrokeIds; }
    public void setBrushStrokeIds(ArrayList<Long> brushStrokes) { this.brushStrokeIds = brushStrokeIds; };

    // add a new brushStroke
    public void add(Long brushStrokeId) { this.brushStrokeIds.add(brushStrokeId); }

    public Drawing() {
        this.drawerName = "";
    }

}
