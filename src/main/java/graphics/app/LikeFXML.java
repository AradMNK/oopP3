package graphics.app;

import Builder.UserBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import Objects.Post;
import Objects.User;
import Objects.Like;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class LikeFXML {
    User liker;
    Post post;
    @FXML Circle pfp;
    @FXML Text name;
    @FXML GridPane picturePane;
    @FXML Hyperlink username, postID;

    private void initContents(String name, String username, int postID) {
        this.name.setText(name);
        this.username.setText("@" + username);
        this.postID.setText(Integer.toString(postID));
        pfp.radiusProperty().bind(picturePane.heightProperty().divide(2));
    }

    public void initialize(Like like){
        liker = UserBuilder.getUserFromDatabase(like.getLikerUsername());
        post = like.getPost();
        if (liker.getPfp().getHandle().equals(""))
            pfp.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {pfp.setFill(new ImagePattern(new Image(liker.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                pfp.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }
        initContents(liker.getName(), liker.getUsername(), like.getPost().getPostID().getHandle());
    }

    @FXML void usernameClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(liker);
    }

    @FXML void postIDClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((PostFXML)fxmlLoader.getController()).initialize(post);
    }
}
