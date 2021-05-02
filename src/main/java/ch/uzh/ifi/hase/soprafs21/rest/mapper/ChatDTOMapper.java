package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.entity.Drawing;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawingGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.DrawingPostDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

public interface ChatDTOMapper {

    ChatDTOMapper INSTANCE = Mappers.getMapper(ChatDTOMapper.class);

    @Mapping(source = "writer_name", target = "writer_name")
    @Mapping(source = "chat_message", target = "chat_message")
    @Mapping(source = "timestamp", target = "timestamp")
    Chat convertChatPostDTOtoEntity(ChatPostDTO chatPostDTO);

    @Mapping(source = "writer_names", target = "writer_names")
    @Mapping(source = "chat_messages", target = "chat_messages")
    ChatGetDTO convertEntityToChatGetDTO(Chat chat);

}
