package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.GameModes;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

    @Test
    void findGame_byID_success(){
        Game game = new Game();

        // initialize all the fields
        game.setId(1L);

        ArrayList<String> players = new ArrayList<>();
        players.add("Player1");
        players.add("Player2");
        game.setPlayers(players);

        game.setGameName("TestGame");
        game.setGameModes(GameModes.CLASSIC);
        game.setNumberOfRounds(1);
        game.setTimePerRound(1);
        game.setTestphase(true);
        game.setRoundTracker(0);
        game.setRoundId(1L);
        game.setTimer(null);
        game.setScoreBoard(null);

        entityManager.persist(game);
        entityManager.flush();

        // when
        Optional<Game> found = gameRepository.findById(game.getId());

        // then
        assertEquals(found.get().getId(), game.getId());
        assertEquals(found.get().getPlayers(), game.getPlayers());
        assertEquals(found.get().getGameName(), game.getGameName());
        assertEquals(found.get().getGameModes(), game.getGameModes());
        assertEquals(found.get().getNumberOfRounds(), game.getNumberOfRounds());
        assertEquals(found.get().getTimePerRound(), game.getTimePerRound());
        assertEquals(found.get().getRoundTracker(), game.getRoundTracker());
        assertEquals(found.get().getRoundId(), game.getRoundId());
        assertEquals(found.get().getTestphase(), game.getTestphase());
        assertEquals(found.get().getTimer(), game.getTimer());
        assertEquals(found.get().getScoreBoard(), game.getScoreBoard());

    }

}

