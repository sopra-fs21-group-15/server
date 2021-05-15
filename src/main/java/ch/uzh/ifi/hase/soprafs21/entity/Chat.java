package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "CHAT")
public class Chat implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column (nullable = false)
    private Long chatId;

    @OneToMany
    List<Message> messages = new ArrayList<Message>();

    // generic methods to handle incoming requests
    public Long getChatId() { return this.chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public List<Message> getMessage() { return this.messages; }
    public void setMessage(Message message) { messages.add(message); }

    public void setMessages(List<Message> messages) { this.messages = messages; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true; }
        if (!(o instanceof Chat)) {return false;}
        Chat other = (Chat) o;
        return chatId != null && chatId.equals(other.getChatId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChatId());
    }
}
