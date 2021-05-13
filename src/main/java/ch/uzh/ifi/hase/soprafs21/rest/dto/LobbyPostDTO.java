package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Chat;

import java.util.ArrayList;

public class LobbyPostDTO {

    // added size, rounds, timer, lobbyChat, status to pass down and convert
    private Long id;

    private String password;

    private String lobbyname;

    private String size;

    private String rounds;

    private String  timer;

 //   private Integer chatId;

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

    public String getSize() { return size; }

    public void setSize(String  size) { this.size = size; }

    public String  getRounds() { return rounds; }

    public void setRounds(String  rounds) { this.rounds = rounds; }

    public String  getTimer() { return  timer; }

    public void setTimer(String timer) { this.timer = timer; }

 //   public Integer getChatId() { return chatId; }

 //   public void setChatId(Integer chatId) { this.chatId = chatId; }

}
