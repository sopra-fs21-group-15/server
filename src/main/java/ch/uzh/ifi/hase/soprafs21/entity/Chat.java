package ch.uzh.ifi.hase.soprafs21.entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@Entity
@Table(name = "CHAT")
public class Chat implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private Long lobbyId;

    @OneToMany
    ArrayList<Message> messages = new ArrayList<Message>();

    // generic methods to handle incoming requests
    public Long getLobbyId() { return this.lobbyId; }
    public void setLobbyId(Long lobbyId) { this.lobbyId = lobbyId; }

    public ArrayList<Message> getMessage() { return this.messages; }
    public void setMessage(Message message) { messages.add(message); }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true; }
        if (!(o instanceof Chat)) {return false;}
        Chat other = (Chat) o;
        return lobbyId != null && lobbyId.equals(other.getLobbyId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLobbyId());
    }
}
