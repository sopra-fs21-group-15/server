package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LobbyPostDTO {

    // added size, rounds, timer, lobbyChat, status to pass down and convert
    private Long id;

    private String password;

    private String lobbyname;

    private Integer size;

    private Integer rounds;

    private Integer timer;

    private String lobbyChat;

    private String status;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() { return id; }

    public String getLobbyname() {
        return lobbyname;
    }

    public void setLobbyname(String lobbyname) {
        this.lobbyname = lobbyname;
    }

    public Integer getSize() { return size; }

    public void setSize(Integer size) { this.size = size; }

    public Integer getRounds() { return rounds; }

    public void setRounds(Integer rounds) { this.rounds = rounds; }

    public Integer getTimer() { return  timer; }

    public void setTimer(Integer timer) { this.timer = timer; }

    public String getLobbyChat() { return lobbyChat; }

    public void setLobbyChat(String lobbyChat) { this.lobbyChat = lobbyChat; }

}
