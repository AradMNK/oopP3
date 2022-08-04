package graphics.app;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import Objects.Post;
import Objects.Comment;
import Objects.Like;

import java.util.HashSet;

public class FeedFXML {
    @FXML
    VBox displayPosts, displayComments, displayLikes;

    public void initialize(HashSet<Post> posts, HashSet<Comment> comments, HashSet<Like> likes){

    }
}
