package Objects;

import java.util.HashSet;

public class Feed {
    private final HashSet<Post> posts = new HashSet<>();
    private final HashSet<Comment> comments = new HashSet<>();
    private final HashSet<Like> likes = new HashSet<>();

    public HashSet<Post> getPosts() {return posts;}
    public HashSet<Comment> getComments() {return comments;}
    public HashSet<Like> getLikes() {return likes;}
}