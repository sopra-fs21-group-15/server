package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// I deleted some unnecessary find functions...
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
