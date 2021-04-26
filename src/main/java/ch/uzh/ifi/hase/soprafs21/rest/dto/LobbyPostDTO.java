package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LobbyPostDTO {

    // added birth date to pass down and convert
    private String password;

    private String lobbyname;

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

}
