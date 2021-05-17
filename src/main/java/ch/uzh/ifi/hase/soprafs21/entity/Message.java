package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.io.Serializable;


@Entity
@Table(name = "MESSAGE")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue
    private Long messageId;

    @Column(nullable = false)
    private String timeStamp;

    @Column
    private String writerName;

    @NotBlank
    @NotEmpty
    @Column(nullable = false)
    private String message;

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

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

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
