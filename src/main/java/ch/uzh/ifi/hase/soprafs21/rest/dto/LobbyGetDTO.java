package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;

public class LobbyGetDTO {

    private Long id;
    private String password;
    private String lobbyname;
    // added creation & birth date
    private String creation_date;
    private LobbyStatus status;


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

    // get creation date
    public String getCreation_date() {
        return creation_date;
    }

    // set creation date
    public void setCreation_date(String creation_date) { this.creation_date = creation_date; }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }
}
