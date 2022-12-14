package graphics.app;

import Builder.DirectMessengerBuilder;
import Builder.UserBuilder;
import Database.Changer;
import Database.Loader;
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
    GroupStatsFXML groupEditController;
    Group internalGroup;
    User internalUser;
    @FXML Button followButton, messageButton, blockButton, banButton, postsButton;
    @FXML Circle picture;
    @FXML Text name, username, bio, subtitle, date;
    @FXML ScrollPane bioPane;
    @FXML GridPane picturePane, masterPane;

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
        if (Loginner.loginnedUser.getUsername().equals(user.getUsername())){
            followButton.setDisable(true);
            blockButton.setDisable(true);
            banButton.setDisable(true);
        }
        if (Database.Loader.userFollows(Loginner.loginnedUser.getUsername(), user.getUsername()))
            followButton.setText("Unfollow");
        if (Database.Loader.isUserBlocked(Loginner.loginnedUser.getUsername(), internalUser.getUsername()))
            blockButton.setText("UNBLOCK");
    }
    public void initializeGroupOwnerMode(Group group, GroupStatsFXML groupStatsFXML){
        groupEditController = groupStatsFXML;
        banButton.setVisible(true);
        internalGroup = group;
    }

    @FXML void follow(){
        if (Database.Loader.userFollows(Loginner.loginnedUser.getUsername(), internalUser.getUsername())){
            Loginner.loginnedUser.unfollow(internalUser.getUsername());
            followButton.setText("Follow");
            return;
        }
        Loginner.loginnedUser.follow(internalUser.getUsername());
        followButton.setText("Unfollow");
    }
    @FXML void block(){
        if (Database.Loader.isUserBlocked(Loginner.loginnedUser.getUsername(), internalUser.getUsername())){
            Loginner.loginnedUser.unblock(internalUser.getUsername());
            blockButton.setText("BLOCK");
            return;
        }
        Loginner.loginnedUser.block(internalUser.getUsername());
        blockButton.setText("UNBLOCK");
    }
    @FXML void ban(){
        Changer.removeParticipant(internalGroup.getGroupID().getHandle(), internalUser.getUsername());
        internalGroup.getParticipants().remove(internalUser);
        groupEditController.updateParticipants();
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

    public void initializeGroupBehaviorAdd(Group group) {
        masterPane.setOnMouseClicked(e->addToGroup(group));
        masterPane.setOnMouseEntered(e->new Pulse(masterPane).play());
    }

    private void addToGroup(Group group) {
        if (group.getParticipants().stream().anyMatch(u->u.getUsername().equals(internalUser.getUsername()))){
            AppManager.alert(Alert.AlertType.ERROR, "User is already in your group!",
                    "Try other people.", "Already added!");
            return;
        }
        if (Loader.isUserBanned(group.getGroupID().getHandle(), internalUser.getUsername())){
            AppManager.alert(Alert.AlertType.ERROR, "User is banned!",
                    "Try unbanning them first.", "BANNED!");
            return;
        }

        group.getParticipants().add(internalUser);
        Changer.addUserToGroup(internalUser.getUsername(), group.getGroupID().getHandle());
        GroupStatsFXML.popup.close();
    }

    public void initializeGroupBehaviorUnban(Group group) {
        masterPane.setOnMouseClicked(e->unbanFromGroup(group));
        masterPane.setOnMouseEntered(e->new Pulse(masterPane).play());
    }

    private void unbanFromGroup(Group group) {
        if (group.getParticipants().stream().anyMatch(u->u.getUsername().equals(internalUser.getUsername()))){
            AppManager.alert(Alert.AlertType.ERROR, "User is already in your group!",
                    "Try other people.", "Already in!");
            return;
        }
        if (!Loader.isUserBanned(group.getGroupID().getHandle(), internalUser.getUsername())){
            AppManager.alert(Alert.AlertType.ERROR, "User is not banned!",
                    "Maybe you were looking for other users...", "NOT BANNED!");
            return;
        }

        Changer.removeFromBanList(group.getGroupID().getHandle(), internalUser.getUsername());
        GroupStatsFXML.popup.close();
    }
}
