package ch.uzh.ifi.hase.soprafs21.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "DRAWING")
public class Drawing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String drawerName = "";

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<BrushStroke> brushStrokes = new ArrayList<>();

    // generic methods to handle incoming requests
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public String getDrawerName() { return this.drawerName; }
    public void setDrawerName(String newDrawerName) { this.drawerName = newDrawerName; };

    public List<BrushStroke> getBrushStrokes() { return this.brushStrokes; }
    public void setBrushStrokes(List<BrushStroke> brushStrokes) { this.brushStrokes = brushStrokes; };

}
