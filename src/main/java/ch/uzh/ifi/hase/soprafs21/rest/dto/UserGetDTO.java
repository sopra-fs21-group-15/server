package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class UserGetDTO {

    private Long id;
    private String password;
    private String username;
    // added creation & birth date
    private String creation_date;
    private String birth_date;
    private UserStatus status;


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
    public String getCreation_date() {
        return creation_date;
    }

    // set creation date
    public void setCreation_date(String creation_date) { this.creation_date = creation_date; }

    // get birth date
    public String getBirth_date() {
        return birth_date;
    }

    // set birth date
    public void setBirth_date(String birth_date) { this.birth_date = birth_date; }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
