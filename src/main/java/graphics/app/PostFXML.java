package graphics.app;

import Database.Changer;
import Login.Loginner;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import Objects.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PostFXML {
    User poster;
    Post post;
    @FXML Button likeButton, commentButton;
    @FXML Circle pfp;
    @FXML Text name, bio, postContent, subtitle, date;
    @FXML ScrollPane postPane, bioPane;
    @FXML GridPane picturePane;
    @FXML Hyperlink username;
    @FXML Label adLabel;

    @FXML void comment(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.COMMENTS_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace();}
        ((CommentsFXML)fxmlLoader.getController()).initialize(post, post.getComments());
    }
    @FXML void like(){
        if (Database.Loader.isPostLiked(post.getPostID().getHandle(), Loginner.loginnedUser.getUsername())){
            Loginner.loginnedUser.unlike(post.getPostID().getHandle());
            likeButton.setText(Integer.toString(Integer.parseInt(likeButton.getText()) - 1));
        }
        else {
            Loginner.loginnedUser.like(post.getPostID().getHandle());
            likeButton.setText(Integer.toString(Integer.parseInt(likeButton.getText()) + 1));
        }
    }

    private void initContents(String name, String username, String bio, String post, String subtitle, LocalDateTime time){
        this.name.setText(name);
        this.username.setText("@" + username);
        this.bio.setText(bio);
        this.postContent.setText(post);
        this.subtitle.setText(subtitle);
        this.date.setText(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));

        postContent.wrappingWidthProperty().bind(postPane.widthProperty().subtract(Utility.POST_TEXT_MARGIN_FROM_RIGHT));
        this.bio.wrappingWidthProperty().bind(bioPane.widthProperty().subtract(Utility.POST_TEXT_MARGIN_FROM_RIGHT));
        pfp.radiusProperty().bind(Bindings.min(picturePane.heightProperty(), picturePane.widthProperty()).divide(2));
    }

    private void initPicture(String handle) {
        if (handle.equals("")) return;

        ImageView imageView = new ImageView();
        imageView.setImage(new Image(handle));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(Utility.IMAGE_POST_FIT_WIDTH);
        VBox group = new VBox(postContent, imageView);
        postPane.setContent(group);
    }

    public void initialize(Post post){
        this.post = post;
        if (!post.getPoster().getUsername().equals(Loginner.loginnedUser.getUsername())
                && post.getPoster().getUserType() == UserType.BUSINESS)
        Changer.addViewForUser(post.getPostID().getHandle(), Loginner.loginnedUser.getUsername());
        if (post.getPoster().getPfp().getHandle().equals(""))
            pfp.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {
                pfp.setFill(new ImagePattern(new Image(post.getPoster().getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
            "Please choose another image.", "Image could not load!");
                pfp.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }
        likeButton.setText(Integer.toString(Database.Loader.getNumberOfLikes(post.getPostID().getHandle())));
        commentButton.setText(Integer.toString(Database.Loader.getNumberOfComments(post.getPostID().getHandle())));

        poster = post.getPoster();
        initContents(poster.getName(), poster.getUsername(), poster.getBio(),
                post.getDescription(), poster.getSubtitle(), post.getDatePosted());
        initPicture(post.getPicture().getHandle());

        if (post.getPoster().getUserType() == UserType.BUSINESS) adLabel.setVisible(true);
    }

    public void initializeSample(){
        if (Loginner.loginnedUser.getPfp().getHandle().equals(""))
            pfp.setFill(new ImagePattern(new Image(Objects.requireNonNull
                    (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE)).toString())));
        else {
            try {
                pfp.setFill(new ImagePattern(new Image(Loginner.loginnedUser.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                pfp.setFill(new ImagePattern(new Image(Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE)).toString())));}
        }

        poster = Loginner.loginnedUser;
        initContents(poster.getName(), poster.getUsername(), poster.getBio(),
                postContent.getText(), poster.getSubtitle(), LocalDateTime.now());
        commentButton.setDisable(true);
        likeButton.setDisable(true);
        username.setDisable(true);
    }

    @FXML void usernameClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(poster);
    }

    public void initialize(Comment comment) {
        if (comment.getCommenter().getPfp().getHandle().equals(""))
            pfp.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {
                pfp.setFill(new ImagePattern(new Image(comment.getCommenter().getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                pfp.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }
        likeButton.setVisible(false);
        commentButton.setVisible(false);
        poster = comment.getCommenter();
        initContents(poster.getName(), poster.getUsername(), poster.getBio(),
                comment.getMsg(), poster.getSubtitle(), comment.getDate());
    }
}
