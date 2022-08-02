package Objects;

public class Like {
    private SaveHandle likeID;
    private Post post;

    public SaveHandle getLikeID() {return likeID;}
    public void setLikeID(SaveHandle likeID) {this.likeID = likeID;}

    public Post getPost() {return post;}
    public void setPost(Post post) {this.post = post;}

    public String getLikerUsername() {return likerUsername;}
    public void setLikerUsername(String likerUsername) {this.likerUsername = likerUsername;}

    private String likerUsername;
}
