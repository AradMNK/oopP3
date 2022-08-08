package graphics.app;

import Builder.GroupMessageBuilder;
import Builder.MessageBuilder;
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
import Objects.GroupMessage;
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
        ChatFXML.chatFXML.replyMode(message);
    }
    @FXML void edit(){
        ChatFXML.chatFXML.applyEdit(message);
    }
    @FXML void forward(){ChatFXML.chatFXML.initForward(message);}
    @FXML void delete(){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete message");
        confirmation.setHeaderText("You are about to delete this message forever!");
        confirmation.setContentText("Proceed?");
        confirmation.showAndWait();
        if (confirmation.getResult().getText().equals("OK")){
            Changer.deleteMessage(message.getID().getHandle());
            ChatFXML.chatFXML.refreshForDelete(message);
        }
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
            deleteButton.setDisable(true);
        }
        if (!message.getOriginalMessage().equals(message.getID())) editButton.setDisable(true);
        initContents(user.getName(), message.getContent(), message.getDate());

        if (message.getID().equals(message.getOriginalMessage())){
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
        ((UserFXML)fxmlLoader.getController()).initialize(UserBuilder.getUserFromDatabase(message.getOriginalUsername()));
    }
    @FXML void repliedMessageClick(){
        if (message.getClass().equals(GroupMessage.class))
            ChatFXML.chatFXML.replyMode(GroupMessageBuilder.getGroupMessageFromDatabase(message.getReplyToID().getHandle()));
        else ChatFXML.chatFXML.replyMode(MessageBuilder.getMessageFromDatabase(message.getReplyToID().getHandle()));
    }
}
