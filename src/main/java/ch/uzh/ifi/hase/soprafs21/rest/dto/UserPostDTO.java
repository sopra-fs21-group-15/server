package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class UserPostDTO {

    // added birth date to pass down and convert
    private String password;

    private String username;

    private String birth_date;

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
    public String getBirth_date() {return birth_date; }

    // set birth date
    public void setBirth_date(String birth_date) {this.birth_date = birth_date; }
}
