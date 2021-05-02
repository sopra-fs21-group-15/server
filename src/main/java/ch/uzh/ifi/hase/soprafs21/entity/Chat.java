package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "CHAT")
public class Chat implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private ArrayList<Message> messages = new ArrayList<Message>();

    // generic methods to handle incoming requests
    public long getUserId() { return this.userId; }
    public void setUserId(long userId) { this.userId = userId; };

    public ArrayList<Message> getMessages() { return this.messages; }
    public void setMessages(ArrayList<Message> messages) { this.messages = messages; };

    // one that does it all
    public void addMessage(Message message) {
        messages.add(message);
    }

    public Chat getChat(LocalDateTime timeStamp) {
        int index = 0;
        while(messages.get(index).getTimeStamp().isBefore(timeStamp)) {
            index++;
        }
        ArrayList<Message> temp = new ArrayList<Message>(messages.subList(index,messages.size()-1));
        Chat value = new Chat();
        value.setMessages(temp);
        value.setUserId(0);
        return value;
    }

}
