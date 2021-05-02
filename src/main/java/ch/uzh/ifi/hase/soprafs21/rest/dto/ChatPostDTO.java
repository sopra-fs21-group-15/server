package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.time.LocalDateTime;

public class ChatPostDTO {

    private long user_id;
    private long game_id;
    private LocalDateTime timestamp;

    // methods to access user_id
    public long getUser_id() { return user_id; }
    public void setUser_id(long user_id) { this.user_id = user_id; }

    // methods to access game_id
    public long getGame_id() { return game_id; }
    public void setGame_id(long game_id) { this.game_id = game_id; }

    // methods to access timestamp
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

}
