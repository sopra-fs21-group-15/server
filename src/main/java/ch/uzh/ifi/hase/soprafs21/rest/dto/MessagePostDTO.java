package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class MessagePostDTO {

    private String message;
    private String writerName;
    private String timeStamp;

    // methods to access message
    public String getMessage() { return message; }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() { return timeStamp; }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getWriterName() { return writerName; }
    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

}



