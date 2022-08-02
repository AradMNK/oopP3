package Objects;

import java.time.LocalDateTime;

public class Message {
    private SaveHandle ID, replyToID;
    private String content;
    private String username, userName, originalUsername;
    private LocalDateTime date;

    public SaveHandle getID() {return ID;}
    public void setID(SaveHandle ID) {this.ID = ID;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}

    public String getOriginalUsername() {return originalUsername;}
    public void setOriginalUsername(String originalUsername) {this.originalUsername = originalUsername;}

    public LocalDateTime getDate() {return date;}
    public void setDate(LocalDateTime date) {this.date = date;}

    public SaveHandle getReplyToID() {return replyToID;}
    public void setReplyToID(SaveHandle replyToID) {this.replyToID = replyToID;}
}
