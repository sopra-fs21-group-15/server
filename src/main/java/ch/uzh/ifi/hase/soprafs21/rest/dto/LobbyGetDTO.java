package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Chat;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;

public class LobbyGetDTO {

    private Long id;
    private String password;
    private String lobbyname;
    // added creation
    private Integer size;
    private Integer rounds;
    private Integer timer;
    private ArrayList<String> members;
    private LobbyStatus status;
    private ArrayList<Chat> lobbyChat;

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

    // get & set size
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) { this.size = size; }

    // get & set rounds
    public Integer getRounds() { return rounds; }

    public void setRounds(Integer rounds) { this.rounds = rounds; }

    // get & set timer
    public Integer getTimer() { return  timer; }

    public void setTimer(Integer timer) { this.timer = timer; }

    // get & set members
    public ArrayList<String> getMembers() { return  members; }

    public void setMembers(ArrayList<String> members) { this.members = members; }

    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }

    // get & set lobbyChat
    public ArrayList<Chat> getLobbyChat() { return lobbyChat; }

    public void setLobbyChat(ArrayList<Chat> lobbyChat) { this.lobbyChat = lobbyChat; }
}
