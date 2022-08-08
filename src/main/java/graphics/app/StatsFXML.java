package graphics.app;

import Database.Loader;
import Login.Loginner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import Objects.Post;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class StatsFXML {
    @FXML VBox display;
    @FXML Label views, likes;

    public void initialize(HashSet<Post> posts){
        views.setText(Integer.toString(Loader.getTotalViews(Loginner.loginnedUser.getUsername())));
        likes.setText(Integer.toString(Loader.getTotalLikes(Loginner.loginnedUser.getUsername())));

        List<Post> postList = posts.stream().toList();
        ArrayList<Post> sortedPosts = new ArrayList<>(postList);
        sortedPosts.sort(Comparator.comparing(Post::getDatePosted).reversed());
        for (Post post : sortedPosts){
            addPost(post);
            display.getChildren().add(new Label("This post has received "
                    + Loader.getNumberOfLikeStats(post.getPostID().getHandle()) + " likes and " +
                    Loader.getViews(post.getPostID().getHandle()) + " views today."));
        }
    }

    private void addPost(Post post) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((PostFXML)fxmlLoader.getController()).initialize(post);
    }
}
