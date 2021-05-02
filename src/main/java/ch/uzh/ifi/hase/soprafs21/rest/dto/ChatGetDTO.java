package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.entity.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ChatGetDTO {

    private ArrayList<String> chat_messages;
    private ArrayList<String> writer_names;
    private ArrayList<LocalDateTime> timeStamps;

    // methods to access brushStrokes
    public ArrayList<String> getChat_messages() { return chat_messages; }
    public void setMessages(ArrayList<String> messages) { this.chat_messages = messages; }

    public ArrayList<String> getWriter_names() { return writer_names; }

    public void setWriter_names(ArrayList<String> writer_names) {
        this.writer_names = writer_names;
    }

    public ArrayList<LocalDateTime> getTimeStamps() {
        return timeStamps;
    }

    public void setTimeStamps(ArrayList<LocalDateTime> timeStamps) {
        this.timeStamps = timeStamps;
    }
}
