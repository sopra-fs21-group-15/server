package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.ChatDTOMapper;

import java.util.ArrayList;
import java.util.List;

public class ChatGetDTO {


    private Long chatId;

    List<MessageGetDTO> messages = new ArrayList<>();

    public Long getLobbyId() {
        return chatId;
    }

    public void setLobbyId(Long chatId) {
        this.chatId = chatId;
    }

    public List<MessageGetDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        for(Message m : messages) {
            MessageGetDTO messageGetDTO = ChatDTOMapper.INSTANCE.convertEntityToMessageGetDTO(m);
            this.messages.add(messageGetDTO);
        }
    }
}
