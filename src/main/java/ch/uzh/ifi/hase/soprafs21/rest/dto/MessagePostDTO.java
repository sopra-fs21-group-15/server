package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class MessagePostDTO {

    private Long id;
    private String message;

    // methods for id
    public long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    // methods for message
    public String getMessage() { return this.message; }
    public void setMessage(String message) { this.message = message; }
}
