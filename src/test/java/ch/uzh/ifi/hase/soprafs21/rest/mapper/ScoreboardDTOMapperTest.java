package ch.uzh.ifi.hase.soprafs21.rest.mapper;


import ch.uzh.ifi.hase.soprafs21.entity.ScoreBoard;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ScoreBoardGetDTO;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class ScoreboardDTOMapperTest {
    @Test
     void testScoreboard_fromScoreboard_toScoreboardGetDTO() {
        // create Scoreboard

        ScoreBoard scoreBoard = new ScoreBoard();

        ArrayList<String> players = new ArrayList<>();
        players.add("Anton");
        players.add("Mike");
        scoreBoard.setPlayers(players);
        int [] newScore = {545654, 456465};
        scoreBoard.setScore(newScore);
        int[] ranking = {1,2};
        scoreBoard.setRanking(ranking);
        // MAP -> Create ScoreboardGetDTO
        ScoreBoardGetDTO scoreBoardGetDTO = ScoreBoardDTOMapper.INSTANCE.convertEntityToScoreBoardGetDTO(scoreBoard);
        // check content
        assertEquals(scoreBoard.getPlayers(), scoreBoardGetDTO.getPlayers());

    }
}
