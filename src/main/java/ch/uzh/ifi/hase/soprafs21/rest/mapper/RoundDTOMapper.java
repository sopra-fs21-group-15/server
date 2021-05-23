package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Round;
import ch.uzh.ifi.hase.soprafs21.rest.dto.RoundGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoundDTOMapper {

    RoundDTOMapper INSTANCE = Mappers.getMapper(RoundDTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "players", target = "players")
    // @Mapping(source = "words", target = "words")
    @Mapping(source = "selection", target = "selection")
    @Mapping(source = "index", target = "index")
    @Mapping(source = "hasDrawn", target = "hasDrawn")
    @Mapping(source = "gotPoints", target = "gotPoints")
    @Mapping(source = "hasGuessed", target = "hasGuessed")
    @Mapping(source = "drawerName", target = "drawerName")
    @Mapping(source = "word", target = "word")
    RoundGetDTO convertEntityToRoundGetDTO(Round round);
}
