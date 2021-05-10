package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.ChatDTOMapper;

import java.util.ArrayList;
import java.util.List;

public class ChatGetDTO {


    private Long lobbyId;

    List<MessageGetDTO> messages = new ArrayList<>();

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
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
