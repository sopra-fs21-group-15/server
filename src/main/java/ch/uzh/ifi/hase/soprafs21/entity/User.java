package ch.uzh.ifi.hase.soprafs21.entity;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    //creation date for the data base
    @Column(nullable = false)
    private String creationDate;

    //birth date for the data base
    @Column(nullable = true)
    private String birthDate;

    @Column(nullable = false)
    private UserStatus status;

    @Column(nullable = true)
    private String userTag;

    //the user's friends
    @Column(nullable = false)
    private ArrayList<String> friendsList = new ArrayList<String>();

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //get the creation date
    public String getCreationDate() {
        return creationDate;
    }

    // set the creation date
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    //get the birth date
    public String getBirthDate() {
        return birthDate;
    }

    // set the birth date
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getUserTag() { return userTag; }

    public void setUserTag(String userTag) { this.userTag = userTag; }

    //get, set & delete friendsList & their status
    public void setFriendsList(String friend) { friendsList.add(friend); }

    public ArrayList<String> getFriendsList() { return friendsList; }

    public void deleteFriendsList(String friend) { friendsList.remove(friend); }

}
