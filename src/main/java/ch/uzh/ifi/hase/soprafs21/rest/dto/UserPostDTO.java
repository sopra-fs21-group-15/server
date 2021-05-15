package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.ArrayList;

public class UserPostDTO {

    // added birth date to pass down and convert
    private String password;

    private String username;

    private String birthDate;

    private String userTag;

    private ArrayList<String> friends;

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getFriends(){return friends; }

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

    // get birth date
    public String getBirthDate() {return birthDate; }

    // set birth date
    public void setBirthDate(String birthDate) {this.birthDate = birthDate; }

    public String getUserTag() { return userTag; }

    public void setUserTag(String userTag) { this.userTag = userTag; }
}
