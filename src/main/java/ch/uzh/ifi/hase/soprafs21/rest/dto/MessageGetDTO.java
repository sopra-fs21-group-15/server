package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class MessageGetDTO {

    private Long messageId;
    private String timeStamp;
    private String writerName;
    private String message;
    private boolean correctGuess;

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public String getTimeStamp() { return timeStamp; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

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

    public boolean getCorrectGuess() { return correctGuess; }
    public void setCorrectGuess(boolean correctGuess) { this.correctGuess = correctGuess; }
}