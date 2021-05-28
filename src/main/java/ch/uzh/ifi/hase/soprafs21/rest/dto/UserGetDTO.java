package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import java.util.ArrayList;

public class UserGetDTO {

    private Long id;
    private String password;
    private String username;
    // added creation & birth date
    private String creationDate;
    private String birthDate;
    private UserStatus status;
    private ArrayList<String> friendsList;
    private ArrayList<String> friendRequestList;
    private String userTag;


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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // get creation date
    public String getCreationDate() {
        return creationDate;
    }

    // set creation date
    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

    // get birth date
    public String getBirthDate() {
        return birthDate;
    }

    // set birth date
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    // get & set members
    public ArrayList<String> getFriendsList() { return  friendsList; }

    public void setFriendsList(ArrayList<String> friends) { this.friendsList = friends; }

    // get & set members
    public ArrayList<String> getFriendRequestList() { return friendRequestList; }

    public void setFriendRequestList(ArrayList<String> friendRequestList) { this.friendRequestList = friendRequestList; }

    public String getUserTag() { return userTag; }

    public void setUserTag(String userTag) { this.userTag = userTag; }
}
