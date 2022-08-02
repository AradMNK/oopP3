package graphics.app;

import Login.Loginner;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import Objects.Post;
import Objects.User;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PostFXML {
    @FXML Button likeButton, commentButton;
    @FXML Circle picture;
    @FXML Text name, username, bio, postContent, subtitle, date;
    @FXML ScrollPane postPane, bioPane;
    @FXML GridPane picturePane;
    @FXML ToolBar toolbar;

    private void initContents(String name, String username, String bio, String post, String subtitle, LocalDateTime time){
        this.name.setText(name);
        this.username.setText("@" + username);
        this.bio.setText(bio);
        this.postContent.setText(post);
        this.subtitle.setText(subtitle);
        this.date.setText(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        postContent.wrappingWidthProperty().bind(postPane.widthProperty().subtract(Utility.POST_TEXT_MARGIN_FROM_RIGHT));
        this.bio.wrappingWidthProperty().bind(bioPane.widthProperty().subtract(Utility.POST_TEXT_MARGIN_FROM_RIGHT));
        picture.radiusProperty().bind(picturePane.heightProperty().divide(2));

        commentButton.setDisable(true);
        likeButton.setDisable(true);
    }

    private void initPicture(String handle) {
        if (handle.equals("")) return;

        ImageView imageView = new ImageView();
        imageView.setImage(new Image(handle));
    }

    public void initialize(Post post){
        if (post.getPoster().getPfp().getHandle().equals(""))
            picture.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {picture.setFill(new ImagePattern(new Image(post.getPoster().getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
            "Please choose another image.", "Image could not load!");
                picture.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }

        User poster = post.getPoster();
        initContents(poster.getName(), poster.getUsername(), poster.getBio(),
                post.getDescription(), poster.getSubtitle(), post.getDatePosted());
        initPicture(post.getPicture().getHandle());
    }

    public void initializeSample(){
        if (Loginner.loginnedUser.getPfp().getHandle().equals(""))
            picture.setFill(new ImagePattern(new Image(Objects.requireNonNull
                    (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE)).toString())));
        else {
            try {picture.setFill(new ImagePattern(new Image(Loginner.loginnedUser.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                picture.setFill(new ImagePattern(new Image(Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE)).toString())));}
        }

        User poster = Loginner.loginnedUser;
        initContents(poster.getName(), poster.getUsername(), poster.getBio(),
                postContent.getText(), poster.getSubtitle(), LocalDateTime.now());
    }
}
