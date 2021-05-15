package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("roundRepository")
public interface RoundRepository extends JpaRepository<Round, Long> {
    Optional<Round> findById(Long id);
}
