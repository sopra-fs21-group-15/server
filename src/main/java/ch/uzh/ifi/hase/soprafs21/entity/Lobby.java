package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal Lobby Representation
 * This class composes the internal representation of the lobby and defines how the lobby is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "LOBBY")

public class Lobby implements  Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false, unique = true)
    private String lobbyname;

    @Column(nullable = false, unique = true)
    private String token;

    //creation date for the data base
    @Column(nullable = false)
    private String creation_date;

    @Column(nullable = false)
    private LobbyStatus status;

    @Column(nullable = true)
    private String lobby_tag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLobbyname() {
        return lobbyname;
    }

    public void setLobbyname(String lobbyname) {
        this.lobbyname = lobbyname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //get the creation date
    public String getCreation_date() {
        return creation_date;
    }

    // set the creation date
    public void setCreation_date(String creationDate) {
        this.creation_date = creationDate;
    }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }

    public String getLobby_tag() { return lobby_tag; }

    public void setLobby_tag(String lobbyTag) { this.lobby_tag = lobbyTag; }


}


