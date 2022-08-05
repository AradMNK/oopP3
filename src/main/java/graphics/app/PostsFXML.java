package graphics.app;

import Objects.Post;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class PostsFXML {
    @FXML VBox display;

    private void addPost(Post post){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((PostFXML)fxmlLoader.getController()).initialize(post);
    }

    public void initialize(HashSet<Post> posts){
        List<Post> postList = posts.stream().toList();
        ArrayList<Post> sortedPosts = new ArrayList<>(postList);
        sortedPosts.sort(Comparator.comparing(Post::getDatePosted));
        for (Post post: sortedPosts) addPost(post);
    }

    public void initialize(Post[] posts) {
        for (Post post: posts) addPost(post);
    }
}
