package graphics.app;

import Database.Saver;
import Login.Loginner;
import Objects.*;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ChatFXML {
    int replyID = 0;
    DirectMessenger dm;
    Group group;
    boolean isGroupType = false;

    LinkedList<Message> messages;
    @FXML Circle picture;
    @FXML Text name;
    @FXML GridPane picturePane, masterPane;
    @FXML Button sendButton;
    @FXML TextArea message;
    @FXML VBox display;
    @FXML Rectangle hbar, vbar;

    private void initContents(String name){
        this.name.setText(name);
        picture.radiusProperty().bind(picturePane.widthProperty().divide(2));
        vbar.heightProperty().bind(masterPane.heightProperty());
        vbar.widthProperty().bind(masterPane.widthProperty().divide(10));
        hbar.heightProperty().bind(masterPane.heightProperty().divide(10));
        hbar.widthProperty().bind(masterPane.widthProperty());
    }

    public void initialize(User user, DirectMessenger dm){
        isGroupType = false;
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

        List<Message> messageList = dm.getShownMessages().stream().toList();
        messages = new LinkedList<>(messageList);
        messages.sort(Comparator.comparing(Message::getDate));

        for (Message message : messages){
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
            try {display.getChildren().add(fxmlLoader.load());}
            catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();
                continue;}
            User messenger;
            if (dm.getUser().getUsername().equals(message.getUsername())) messenger = dm.getUser();
            else if (dm.getRecipient().getUsername().equals(message.getUsername())) messenger = dm.getRecipient();
            else{
                AppManager.alert(Alert.AlertType.ERROR, "Unknown error...", "Message did not correspond" +
                        " to any of the chat members... (@" + message.getUsername() + ")", "ERROR...?");
                continue;
            }
            ((MessageFXML)fxmlLoader.getController()).initialize(message, messenger);
        }
        initContents(user.getName());
    }

    public void initialize(Group group){
        isGroupType = true;
        if (group.getPfp().getHandle().equals(""))
            picture.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.GROUP_PICTURE_PATH))).toString())));
        else{
            try {picture.setFill(new ImagePattern(new Image(group.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                picture.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.GROUP_PICTURE_PATH))).toString())));}
        }

        List<GroupMessage> messageList = group.getShownMessages().stream().toList();
        List<User> participants = group.getParticipants().stream().toList();
        messages = new LinkedList<>(messageList);
        messages.sort(Comparator.comparing(Message::getDate));
        for (Message message : messages){
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
            try {display.getChildren().add(fxmlLoader.load());}
            catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();
                continue;}
            ((MessageFXML)fxmlLoader.getController()).initialize(message, participants.get(indexOf(message.getUsername(),
                    participants)));
        }

        initContents(group.getName());
    }

    private int indexOf(String username, List<User> participants) {
        for (int i = 0; i < participants.size(); i++)
            if (participants.get(i).getUsername().equals(username)) return i;
        return -1;
    }

    @FXML void send(){
        if (message.getText().equals("")) {
            AppManager.alert(Alert.AlertType.WARNING, "WARNING!",
                    "You cannot send an empty message.", "Empty message!");
            return;
        }
        if (isGroupType) sendMessage();
        else sendGroupMessage();
    }
    private void sendGroupMessage() {
        LocalDateTime now = LocalDateTime.now();
        GroupMessage newMessage = new GroupMessage();
        newMessage.setID(new SaveHandle(Saver.addToGroupMessages(group.getGroupID().getHandle(),
                Loginner.loginnedUser.getUsername(), Loginner.loginnedUser.getUsername(),
                now, message.getText(), replyID)));
        newMessage.setDate(now);
        newMessage.setContent(message.getText());
        newMessage.setOriginalUsername(Loginner.loginnedUser.getUsername());
        newMessage.setUsername(Loginner.loginnedUser.getUsername());
        newMessage.setGroup(group);
        newMessage.setReplyToID(group.getShownMessages().get(replyID).getID());
        group.getShownMessages().addLast(newMessage);

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR, "Exception occurred.",
                e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((MessageFXML)fxmlLoader.getController()).initialize(newMessage, Loginner.loginnedUser);
    }
    private void sendMessage() {
        LocalDateTime now = LocalDateTime.now();
        Message newMessage = new Message();
        newMessage.setID(new SaveHandle(Saver.addToMessages(dm.getUser().getUsername(), dm.getRecipient().getUsername(),
                dm.getUser().getUsername(), now, message.getText(), replyID)));
        newMessage.setDate(now);
        newMessage.setContent(message.getText());
        newMessage.setOriginalUsername(dm.getUser().getUsername());
        newMessage.setUsername(dm.getUser().getUsername());
        newMessage.setReplyToID(dm.getShownMessages().get(replyID).getID());
        dm.getShownMessages().addLast(newMessage);

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR, "Exception occurred.",
                e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((MessageFXML)fxmlLoader.getController()).initialize(newMessage, Loginner.loginnedUser);
    }

    @FXML void hoverSend(){new Pulse(sendButton).play();}
}
