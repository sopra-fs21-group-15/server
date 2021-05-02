package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.LobbyStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Internal Lobby Representation
 * This class composes the internal representation of the lobby and defines how the lobby is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */

@Entity
@Table(name = "LOBBY")
public class Lobby implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    //lobby type in password implicit included. If password = NULL --> lobby = public
    @Column(nullable = true)
    private String password;

    @Column(nullable = false, unique = true)
    private String lobbyname;

    @Column(nullable = false, unique = true)
    private String token;

    //lobby size per default 4
    @Column(nullable = false)
    private Integer size = 4;

    //number of rounds per default 4
    @Column(nullable = false)
    private Integer rounds = 4;

    //time per round per default 60
    @Column(nullable = false)
    private Integer timer = 60;

    //lobby members & their corresponding status; first member is automatically the owner
    @Column(nullable = false)
    private ArrayList<String> members = new ArrayList<String>();

    //lobby status
    @Column(nullable = false)
    private LobbyStatus status;

    //lobby chat
    @Column
    private ArrayList<Chat> lobbyChat;

    //index for current word
    private int currentWord; // works as an index for both words and pictures

    public Lobby() {
    }

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

    //get & set lobby size
    public  Integer getSize() { return size; }

    public void setSize(Integer size) { this.size = size; }

    //get & set number of rounds
    public Integer getRounds() { return rounds; }

    public void setRounds(Integer rounds) { this.rounds = rounds; }

    //get & set time per rounds
    public Integer getTimer() { return  timer; }

    public void setTimer(Integer timer) { this.timer = timer; }

    //get & set members & their status
    public ArrayList<String> getMembers() { return members; }

    public void setMembers(String member) { members.add(member); }

    public void deleteMembers(String member) { members.remove(member); }

    //get & set lobby status
    public LobbyStatus getStatus() {
        return status;
    }

    public void setStatus(LobbyStatus status) {
        this.status = status;
    }

    public int getCurrentWord() { return this.currentWord; }
    public void setCurrentWord(int nextCurrentWord) { this.currentWord = nextCurrentWord; }

    //get & set lobby chat
    public Chat getLobbyChat(LocalDateTime timeStamp) { return lobbyChat.get(currentWord-1).getChat(timeStamp); }

    public void setLobbyChat(Message message) {

        if(lobbyChat.size() < currentWord) {
            lobbyChat.add(new Chat());
        }
        if(lobbyChat.size() == currentWord) {
            lobbyChat.get(currentWord -1 ).addMessage(message);
        }

    }
}


