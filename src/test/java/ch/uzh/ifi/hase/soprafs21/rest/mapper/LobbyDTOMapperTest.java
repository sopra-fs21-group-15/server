package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class LobbyDTOMapperTest {
    @Test
     void testCreateLobby_fromLobbyPostDTO_success() {
        //create LobbypostDTO
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyname("Test");
        lobbyPostDTO.setPassword("TestPassword");
        lobbyPostDTO.setRounds("5");
        lobbyPostDTO.setSize("4");
        lobbyPostDTO.setTimer("60");


        // MAP -> Create user
        Lobby lobby = LobbyDTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);


        // check content
        assertEquals(lobbyPostDTO.getPassword(), lobby.getPassword());
        assertEquals(lobbyPostDTO.getLobbyname(), lobby.getLobbyname());
        assertEquals(lobbyPostDTO.getRounds(), lobby.getRounds().toString());
        assertEquals(lobbyPostDTO.getSize(), lobby.getSize().toString());
        assertEquals(lobbyPostDTO.getTimer(), lobby.getTimer().toString());


    }

    @Test
     void testGetLobby_fromLobby_toLobbyGetDTO_success() {
        // create Lobby
       Lobby lobby = new Lobby();
       lobby.setRounds(3);
       lobby.setSize(4);
       lobby.setTimer(60);
       lobby.setLobbyname("Lobby1");
       lobby.setStatus(LobbyStatus.OPEN);
       lobby.setPassword("Password1");
       lobby.setMembers("Anton");
       lobby.setId(1L);

        // MAP -> Create UserGetDTO
        LobbyGetDTO lobbyGetDTO= LobbyDTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        // check content
        assertEquals(lobby.getId(), lobbyGetDTO.getId());
        assertEquals(lobby.getPassword(), lobbyGetDTO.getPassword());
        assertEquals(lobby.getLobbyname(), lobbyGetDTO.getLobbyname());
        assertEquals(lobby.getStatus(), lobbyGetDTO.getStatus());
        assertEquals(lobby.getRounds(), lobbyGetDTO.getRounds());
        assertEquals(lobby.getTimer(),  lobbyGetDTO.getTimer());
    }
}
