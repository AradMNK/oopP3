package graphics.app;

import Builder.DirectMessengerBuilder;
import Builder.UserBuilder;
import Database.Changer;
import Login.Loginner;
import animatefx.animation.Pulse;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import Objects.User;
import Objects.Group;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserFXML {
    Group internalGroup;
    User internalUser;
    boolean followed, blocked;
    @FXML Button followButton, messageButton, blockButton, banButton, postsButton;
    @FXML Circle picture;
    @FXML Text name, username, bio, subtitle, date;
    @FXML ScrollPane bioPane;
    @FXML GridPane picturePane;

    private void initContents(String name, String username, String bio, String subtitle, LocalDate time){
        this.name.setText(name);
        this.username.setText("@" + username);
        this.bio.setText(bio);
        this.subtitle.setText(subtitle);
        this.date.setText(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        this.bio.wrappingWidthProperty().bind(bioPane.widthProperty().subtract(Utility.POST_TEXT_MARGIN_FROM_RIGHT));
        picture.radiusProperty().bind(Bindings.min(picturePane.heightProperty(), picturePane.widthProperty()).divide(2));
    }


    public void initialize(User user){
        if (user.getPfp().getHandle().equals(""))
            picture.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {picture.setFill(new ImagePattern(new Image(user.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                picture.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }

        initContents(user.getName(), user.getUsername(), user.getBio(), user.getSubtitle(), user.getDateJoined());
        internalUser = user;

        followed = Database.Loader.userFollows(Loginner.loginnedUser.getUsername(), user.getUsername());
        blocked = Database.Loader.isUserBlocked(user.getUsername(), Loginner.loginnedUser.getUsername());
        if (Loginner.loginnedUser.getUsername().equals(user.getUsername())) followButton.setDisable(true);
        if (followed) followButton.setText("Unfollow");
        if (blocked) blockButton.setText("Unblock");
    }
    public void initializeGroupOwnerMode(Group group){
        banButton.setVisible(true);
        internalGroup = group;
    }

    @FXML void follow(){
        if (followed){
            Loginner.loginnedUser.unfollow(internalUser.getUsername());
            followButton.setText("Follow");
            followed = false;
            return;
        }
        Loginner.loginnedUser.follow(internalUser.getUsername());
        followButton.setText("Unfollow");
        followed = true;
    }
    @FXML void block(){
        if (blocked){
            Loginner.loginnedUser.unblock(internalUser.getUsername());
            blockButton.setText("Block");
            blocked = false;
            return;
        }
        Loginner.loginnedUser.block(internalUser.getUsername());
        blockButton.setText("Unblock");
        blocked = true;
    }
    @FXML void ban(){
        Changer.removeParticipant(internalGroup.getGroupID().getHandle(), internalUser.getUsername());
        internalGroup.getParticipants().remove(internalUser);
    }
    @FXML void posts(){
        User userWithPosts = UserBuilder.getUserFromDatabaseWithPosts(internalUser.getUsername());
        if (userWithPosts.getPosts().size() == 0){
            MainFXML.root.noResult("This user has no posts yet...");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POSTS_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((PostsFXML)fxmlLoader.getController()).initialize(userWithPosts.getPosts());
    }

    @FXML void message(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CHAT_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((ChatFXML)fxmlLoader.getController()).initialize(DirectMessengerBuilder.getDirectMessengerFromDatabase
                (Loginner.loginnedUser, internalUser.getUsername(), Utility.MESSAGES_TO_LOAD));
    }

    @FXML void hoverFollow(){new Pulse(followButton).play();}
    @FXML void hoverMessage(){new Pulse(messageButton).play();}
    @FXML void hoverBlock(){new Pulse(blockButton).play();}
    @FXML void hoverBan(){new Pulse(banButton).play();}
    @FXML void hoverPosts(){new Pulse(postsButton).play();}
}
