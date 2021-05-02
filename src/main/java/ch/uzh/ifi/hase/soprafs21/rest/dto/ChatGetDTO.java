package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.entity.Message;

import java.util.ArrayList;

public class ChatGetDTO {

    private ArrayList<Message> messages;

    // methods to access brushStrokes
    public ArrayList<Message> getMessages() { return messages; }
    public void setMessages(ArrayList<Message> messages) { this.messages = messages; }
}
