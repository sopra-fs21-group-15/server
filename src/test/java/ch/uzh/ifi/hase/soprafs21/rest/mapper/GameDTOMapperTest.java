package ch.uzh.ifi.hase.soprafs21.rest.mapper;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class GameDTOMapperTest {


    @Test
    public void testGetGame_fromGame_toGameGetDTO_success() {
        // create Game
        Game game = new Game();
        game.setGameName("firstname@lastname");
        ArrayList<String> players = new ArrayList<>();
        players.add("Anton");
        players.add("Mike");
        game.setPlayers(players);
        game.setNumberOfRounds(8);
        game.setRoundTracker(8);
        game.setId(1L);
        game.setRoundId(5L);



        // MAP -> Create UserGetDTO
        GameGetDTO gameGetDTO = GameDTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
        // check content
        assertEquals(game.getPlayers(), gameGetDTO.getPlayers());
        assertEquals(game.getNumberOfRounds(),gameGetDTO.getNumberOfRounds());
        assertEquals(game.getId(), gameGetDTO.getId());
        assertEquals(game.getTimePerRound(),gameGetDTO.getTimePerRound());
        assertEquals(game.getRoundId(),gameGetDTO.getRoundId());
    }
}
