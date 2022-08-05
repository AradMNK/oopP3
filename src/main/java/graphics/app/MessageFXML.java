package graphics.app;

import Builder.UserBuilder;
import Database.Changer;
import Login.Loginner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import Objects.Message;
import Objects.User;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class MessageFXML {
    Message message;
    User user;
    @FXML Button replyButton, editButton, forwardButton, deleteButton;
    @FXML Circle pfp;
    @FXML Text name, date, msg, forwarded, replied;
    @FXML GridPane picturePane, messagePane;
    @FXML Hyperlink forwardedUser, repliedMessage;

    @FXML void reply(){
        ChatFXML.chatFXML.replyMode(message.getID());
    }
    @FXML void edit(){
        //FIXME
    }
    @FXML void forward(){
        //FIXME
    }
    @FXML void delete(){
        Changer.deleteMessage(message.getID().getHandle());
        ChatFXML.chatFXML.refreshForDelete(message);
    }

    private void initContents(String name, String msg, LocalDateTime time){
        this.name.setText(name);
        this.msg.setText(msg);
        this.date.setText(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        pfp.radiusProperty().bind(picturePane.heightProperty().divide(2));
    }

    public void initialize(Message message, User user){
        this.user = user;
        this.message = message;
        if (user.getPfp().getHandle().equals(""))
            pfp.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {
                pfp.setFill(new ImagePattern(new Image(user.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                pfp.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }
        if (!user.getUsername().equals(Loginner.loginnedUser.getUsername())){
            editButton.setDisable(true);
            forwardButton.setDisable(true);
            deleteButton.setDisable(true);
        }
        initContents(user.getName(), message.getContent(), message.getDate());

        if (message.getUsername().equals(message.getOriginalUsername())){
            forwarded.setVisible(false);
            forwardedUser.setVisible(false);
        }
        else forwardedUser.setText("@" + message.getOriginalUsername());
        if (message.getReplyToID().getHandle() == 0){
            replied.setVisible(false);
            repliedMessage.setVisible(false);
        }
    }

    @FXML void usernameClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(UserBuilder.getUserFromDatabase(message.getUsername()));
    }
    @FXML void forwardedUserClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(user);
    }
    @FXML void repliedMessageClick(){
        ChatFXML.chatFXML.replyMode(message.getReplyToID());
    }
}
