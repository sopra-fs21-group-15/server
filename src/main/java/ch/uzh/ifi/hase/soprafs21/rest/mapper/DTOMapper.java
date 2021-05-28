package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "birthDate", target = "birthDate") //map the birth date downwards!
    @Mapping(source = "userTag", target = "userTag")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "creationDate", target = "creationDate") //map the creation date upwards!
    @Mapping(source = "birthDate", target = "birthDate") //map the birth date upwards!
    @Mapping(source = "status", target = "status")
    @Mapping(source = "friendsList", target = "friendsList") //map the members upwards!
    @Mapping(source = "friendRequestList", target = "friendRequestList") //map the friend request list upwards!
    UserGetDTO convertEntityToUserGetDTO(User user);

}
