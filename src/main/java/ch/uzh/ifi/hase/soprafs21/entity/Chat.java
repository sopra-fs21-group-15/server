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

    @Column(nullable = true)
    private ArrayList<String> messageList = new ArrayList<String>();

    @Column(nullable = true)
    private ArrayList<String> writerList = new ArrayList<String>();

    @Column(nullable = true)
    private ArrayList<LocalDateTime> timeStamps = new ArrayList<LocalDateTime>();

    // generic methods to handle incoming requests
    public Long getId() { return this.id; }
    public void setId(Long id) { this.id = id; }

    public ArrayList<String> getMessageList() { return this.messageList; }
    public void setMessageList(ArrayList<String> messsageList) { this.messageList = messageList; }

    public ArrayList<String> getWriterList() { return this.writerList; }
    public void setWriterList(ArrayList<String> writerList) { this.writerList = writerList; }

    public ArrayList<LocalDateTime> getTimeStamps() { return this.timeStamps; }

    public void setTimeStamps(ArrayList<LocalDateTime> timeStamps) {
        this.timeStamps = timeStamps;
    }
}
