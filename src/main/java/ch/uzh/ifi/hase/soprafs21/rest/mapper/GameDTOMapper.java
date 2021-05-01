package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * GameDTOMapper
 * This class is used to map the objects passed down by the API calls to instances of classes the back-end understands
 * and can operate on.  GameGetDTO is used by the participants while GamePostDTO should be primarly if not exclusively
 * by the owner of the lobby.
 */

//TODO: see what worlds were used to by the front-end and change accordingly
@Mapper
public interface GameDTOMapper {

    GameDTOMapper INSTANCE = Mappers.getMapper(GameDTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "gameModes", target = "gameModes")
    @Mapping(source = "numberOfRounds", target = "numberOfRounds")
    @Mapping(source = "timePerRound", target = "timePerRound")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "players", target = "players")
    @Mapping(source = "gameModes", target = "gameModes")
    @Mapping(source = "numberOfRounds", target = "numberOfRounds")
    @Mapping(source = "timePerRound", target = "timePerRound")
    @Mapping(source = "roundTracker", target = "roundTracker")
    @Mapping(source = "scoreBoard" , target = "scoreBoard")
    GameGetDTO convertEntityToGameGetDTO(Game game);
}
