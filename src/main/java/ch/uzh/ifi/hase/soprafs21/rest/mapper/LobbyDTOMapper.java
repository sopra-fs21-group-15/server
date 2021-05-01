package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;



/**
 * LobbyDTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface LobbyDTOMapper {

    LobbyDTOMapper INSTANCE = Mappers.getMapper(LobbyDTOMapper.class);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "lobbyname", target = "lobbyname")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "lobbyname", target = "lobbyname")
    @Mapping(source = "creation_date", target = "creation_date") //map the creation date upwards!
    @Mapping(source = "status", target = "status")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);
}
