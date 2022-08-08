package graphics.app;

import Objects.Comment;
import Objects.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CommentsFXML {
    Post post;
    @FXML VBox display;
    @FXML Button cancelButton, commentButton;

    private void addComment(Comment comment) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {
            display.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception");
            e.printStackTrace();
            return;
        }
        ((PostFXML) fxmlLoader.getController()).initialize(comment);
    }

    private void addPost(Post post) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());
        } catch (IOException e) {
            AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception");
            e.printStackTrace();
            return;
        }
        ((PostFXML) fxmlLoader.getController()).initialize(post);
    }

    public void initialize(Post post, Set<Comment> comments) {
        this.post = post;
        addPost(post);
        List<Comment> commentList = comments.stream().toList();
        ArrayList<Comment> sortedComments = new ArrayList<>(commentList);
        sortedComments.sort(Comparator.comparing(Comment::getDate));
        for (Comment comment: sortedComments) addComment(comment);
    }

    @FXML void cancel(){
        MainFXML.root.removeDisplay();
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POSTS_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();}
        ((PostsFXML)fxmlLoader.getController()).initialize(post.getPoster().getPosts());
    }
    @FXML void comment(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.COMMENTMAKER_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();}
        ((CommentmakerFXML)fxmlLoader.getController()).post = post;
    }
}
