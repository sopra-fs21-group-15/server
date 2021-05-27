package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.Colours;

public class CanvasDTO {

    private byte[] image;

    // methods for userName
    public byte[] getImage() { return this.image; }
    public void setImage(byte[] image) { this.image = image; }
}
