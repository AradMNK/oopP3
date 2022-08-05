package Objects;

import java.time.LocalDateTime;
import java.util.HashSet;

public class Post {
    private SaveHandle postID;
    private String description;
    private Handle picture;
    private LocalDateTime datePosted;
    private User poster;

    public Post(){}
    public Post(int postID, String description, LocalDateTime datePosted, User poster, String picturePath) {
        this.postID = new SaveHandle(postID);
        this.description = description;
        this.datePosted = datePosted;
        this.poster = poster;
        this.picture = new Handle(picturePath);
    }

    private final HashSet<Comment> comments = new HashSet<>();

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public Handle getPicture() {return picture;}
    public void setPicture(Handle picture) {this.picture = picture;}

    public LocalDateTime getDatePosted() {return datePosted;}
    public void setDatePosted(LocalDateTime datePosted) {this.datePosted = datePosted;}

    public User getPoster(){return poster;}
    public void setPoster(User poster){this.poster = poster;}

    public SaveHandle getPostID() {return postID;}
    public void setPostID(SaveHandle postID) {this.postID = postID;}

    public HashSet<Comment> getComments() {return comments;}
}
