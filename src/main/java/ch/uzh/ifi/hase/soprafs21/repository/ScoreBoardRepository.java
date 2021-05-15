package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.ScoreBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("scoreBoardRepository")
public interface ScoreBoardRepository extends JpaRepository<ScoreBoard, Long> {
    Optional<ScoreBoard> findById(Long id);
}
