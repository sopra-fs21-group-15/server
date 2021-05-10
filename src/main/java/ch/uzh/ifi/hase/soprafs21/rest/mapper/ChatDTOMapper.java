package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.MessagePostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.MessageGetDTO;

import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;

public interface ChatDTOMapper {

    ChatDTOMapper INSTANCE = Mappers.getMapper(ChatDTOMapper.class);

    @Mapping(source = "message", target = "message")
    @Mapping(source = "writerName", target = "writerName")
    @Mapping(source = "timeStamp", target = "timeStamp")
    Message convertMessagePostDTOtoEntity(MessagePostDTO messagePostDTO);

    @Mapping(source = "lobbyId", target = "lobbyId")
    ChatGetDTO convertEntityToChatGetDTO(Chat chat);

    @Mapping(source = "writerName", target = "writerName")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "timeStamp", target = "timeStamp")
    MessageGetDTO convertEntityToMessageGetDTO(Message message);

}
