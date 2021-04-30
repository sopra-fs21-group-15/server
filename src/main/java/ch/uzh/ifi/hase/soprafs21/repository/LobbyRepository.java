package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// I deleted some unnecessary find functions...
@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Lobby findByLobbyname(String lobbyname);
}