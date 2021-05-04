package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ChatPostDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

public interface ChatDTOMapper {

    ChatDTOMapper INSTANCE = Mappers.getMapper(ChatDTOMapper.class);

    @Mapping(source = "timestamp", target = "timestamp")
    LocalDateTime convertChatPostDTOtoEntity(ChatPostDTO chatPostDTO);

    @Mapping(source = "messages", target = "chat")
    ChatGetDTO convertEntityToChatGetDTO(Chat chat);

}
