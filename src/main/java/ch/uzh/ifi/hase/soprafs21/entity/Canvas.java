package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.helper.Standard;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CANVAS")
public class Canvas implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private byte[] image;

    // generic methods to handle incoming requests
    public byte[] getImage() { return this.image; }
    public void setImage(byte[] image) { this.image = image; }

    /**
     * Additional fundamental classes to for quality of life and basic functionality
     */
    // constructor for all the setup
    public Canvas(byte[] image) {
        // trivial fields
        this.image = image;
    }

    public Canvas() {
        this.image = null;
    }
}
