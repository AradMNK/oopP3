package graphics.app;

import Builder.PostBuilder;
import Objects.Post;
import Objects.User;
import Recommender.AdRecommender;
import Recommender.UserRecommender;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class ExploreFXML {
    @FXML VBox displayR, displayL;

    public void initialize(){
        Parent root_l = null, root_r = null;
        FXMLLoader fxmlLoader_posts = new FXMLLoader(Launcher.class.getResource(Utility.POSTS_FXML_PATH)),
                   fxmlLoader_users = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH));


        User[] users = UserRecommender.recommendUser();
        if (users.length == 0){
            displayR.getChildren().add(MainFXML.root.noResultRoot
                    ("We cannot recommend any new users for now :(.\nPlease try again later."));
        } else{
            try {root_r = fxmlLoader_users.load();}
            catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,"Exception occurred.",
                    e.getCause().getMessage(), "Exception"); e.printStackTrace();}
            ((UsersFXML)fxmlLoader_users.getController()).initialize(users);
            displayR.getChildren().add(root_r);
        }

        Integer[] postIDs = AdRecommender.recommendAd();
        Post[] posts = new Post[postIDs.length];
        int i = 0;
        for (Integer postID: postIDs) posts[i++] = PostBuilder.getPostFromDatabase(postID);
        if (postIDs.length == 0){
            displayL.getChildren().add(MainFXML.root.noResultRoot
                    ("We cannot recommend any new ads for now :(.\nPlease try again later."));
        } else{
            try {root_l = fxmlLoader_posts.load();}
            catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,"Exception occurred.",
                    e.getCause().getMessage(), "Exception"); e.printStackTrace();}
            ((PostsFXML)fxmlLoader_posts.getController()).initialize(posts);
            displayL.getChildren().add(root_l);
        }

    }
}
