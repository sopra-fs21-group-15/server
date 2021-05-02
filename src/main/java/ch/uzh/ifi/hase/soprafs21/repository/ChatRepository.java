package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// I deleted some unnecessary find functions...
@Repository("chatRepository")
public interface ChatRepository extends JpaRepository<Chat, Long> {

}