package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Guess;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GuessPutDTO;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

public interface GuessDTOMapper {

    GuessDTOMapper INSTANCE = Mappers.getMapper(GuessDTOMapper.class);

    // @Mapping(source = "guesser_id", target = "guesser_id")
    @Mapping(source = "guesserName", target = "guesserName")
    @Mapping(source = "guess", target = "guess")
    Guess convertGuessPutDTOToEntity(GuessPutDTO guessPutDTO);

}
