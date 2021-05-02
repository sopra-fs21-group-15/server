package ch.uzh.ifi.hase.soprafs21.rest.mapper;
import ch.uzh.ifi.hase.soprafs21.entity.BrushStroke;
import ch.uzh.ifi.hase.soprafs21.entity.Message;
import ch.uzh.ifi.hase.soprafs21.rest.dto.BrushStrokePutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.MessagePostDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface MessageDTOMapper {

    MessageDTOMapper INSTANCE = Mappers.getMapper(MessageDTOMapper.class);

    @Mapping(source = "message", target = "message")
    Message convertMessagePostDTOtoEntity(MessagePostDTO messagePostDTO);
}
