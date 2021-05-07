package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("timerRepository")
public interface TimerRepository extends JpaRepository<Timer, Long> {
    Optional<Timer> findById(Long id);
}
