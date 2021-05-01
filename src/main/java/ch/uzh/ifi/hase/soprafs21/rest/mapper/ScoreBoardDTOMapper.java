package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.ScoreBoard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ScoreBoardGetDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper
public interface ScoreBoardDTOMapper {

    ScoreBoardDTOMapper INSTANCE = Mappers.getMapper(ScoreBoardDTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "ranking", target = "ranking")
    @Mapping(source = "score", target = "score")
    ScoreBoardGetDTO convertEntityToScoreBoardGetDTO(ScoreBoard scoreBoard);
}
