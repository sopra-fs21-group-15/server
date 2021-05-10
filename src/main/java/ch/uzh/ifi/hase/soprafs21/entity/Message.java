package ch.uzh.ifi.hase.soprafs21.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
/*
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
*/
import java.io.Serializable;
import java.time.LocalTime;


@Entity
@Table(name = "MESSAGE")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(nullable = false)
    @JsonFormat(pattern = "hh:mm:ss")
    private LocalTime timeStamp;

    @Column
    private String writerName;
/*
    @NotBlank
    @NotEmpty
*/
    @Column(nullable = false)
    private String message;

    public String getWriterName() {
        return writerName;
    }
    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public LocalTime getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(LocalTime timeStamp) {
        this.timeStamp = timeStamp;
    }
}
