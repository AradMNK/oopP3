package graphics.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import Objects.Post;
import Objects.Comment;
import Objects.Like;

import java.io.IOException;
import java.util.*;

public class FeedFXML {
    @FXML
    VBox displayPosts, displayComments, displayLikes;

    public void initialize(HashSet<Post> posts, HashSet<Comment> comments, HashSet<Like> likes){
        List<Post> postList = posts.stream().toList();
        ArrayList<Post> sortedPost = new ArrayList<>(postList);
        sortedPost.sort(Comparator.comparing(Post::getDatePosted));

        if (sortedPost.size() == 0){
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(Utility.NO_RESULTS_FXML_PATH));
            try {displayPosts.getChildren().add(loader.load());}
            catch (IOException e) {e.printStackTrace();}
            ((NoResultsFXML)loader.getController()).initialize("No new posts from your followers yet...");
        }
        else{
            for (Post post: sortedPost) {addPost(post);}
        }

        List<Comment> commentList = comments.stream().toList();
        ArrayList<Comment> sortedComments = new ArrayList<>(commentList);
        sortedComments.sort(Comparator.comparing(Comment::getDate));

        if (sortedComments.size() == 0){
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(Utility.NO_RESULTS_FXML_PATH));
            try {displayComments.getChildren().add(loader.load());}
            catch (IOException e) {e.printStackTrace();}
            ((NoResultsFXML)loader.getController()).initialize("No new comments on your posts yet...");
        }
        else{
            for (Comment comment: sortedComments) {addComment(comment);}
        }

        if (likes.size() == 0){
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(Utility.NO_RESULTS_FXML_PATH));
            try {displayLikes.getChildren().add(loader.load());}
            catch (IOException e) {e.printStackTrace();}
            ((NoResultsFXML)loader.getController()).initialize("No new likes on your posts yet...");
        }
        else{
            for (Like like: likes) {addLike(like);}
        }
    }

    private void addLike(Like like) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.LIKE_FXML_PATH));
        try {displayPosts.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((LikeFXML)fxmlLoader.getController()).initialize(like);
    }

    private void addPost(Post post) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {displayPosts.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((PostFXML)fxmlLoader.getController()).initialize(post);
    }

    private void addComment(Comment comment) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {displayPosts.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((PostFXML)fxmlLoader.getController()).initialize(comment);
    }


}
