package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.rest.dto.WordCountGetDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface WordCountDTOMapper {

    WordCountDTOMapper INSTANCE = Mappers.getMapper(WordCountDTOMapper.class);

    @Mapping(source = "length", target = "length")
    WordCountGetDTO convertEntityToWordCountGetDTO(int length);

}
