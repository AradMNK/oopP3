package graphics.app;

import Database.Changer;
import Database.Loader;
import Database.Saver;
import Login.Loginner;
import Objects.*;
import animatefx.animation.Pulse;
import animatefx.animation.SlideInUp;
import graphics.theme.Theme;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ChatFXML {
    Message editingMessage, forwardingMessage;
    Stage popupStage;
    PopOver replyContainer;
    Parent replyNode;
    SaveHandle replyID = new SaveHandle(0);
    DirectMessenger dm;
    Group group;
    boolean isGroupType = false;

    static ChatFXML chatFXML;

    LinkedList<Message> messages;
    @FXML Circle picture;
    @FXML Text name;
    @FXML GridPane picturePane, masterPane;
    @FXML Button sendButton;
    @FXML TextArea message;
    @FXML VBox display;
    @FXML Rectangle hbar, vbar;

    private void initContents(String name){
        chatFXML = this;
        this.name.setText(name);
        picture.radiusProperty().bind(Bindings.min(picturePane.heightProperty(), picturePane.widthProperty()).divide(2));
        vbar.heightProperty().bind(masterPane.heightProperty());
        vbar.widthProperty().bind(masterPane.widthProperty().divide(10));
        hbar.heightProperty().bind(masterPane.heightProperty().divide(10));
        hbar.widthProperty().bind(masterPane.widthProperty().multiply(0.9));
    }

    public void initialize(DirectMessenger dm){
        this.dm = dm;
        isGroupType = false;
        if (dm.getRecipient().getPfp().getHandle().equals(""))
            picture.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {picture.setFill(new ImagePattern(new Image(dm.getRecipient().getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                picture.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }

        messages = dm.getShownMessages();

        for (Message message : messages){
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
            try {display.getChildren().add(fxmlLoader.load());}
            catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.toString(), "Exception"); e.printStackTrace();
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
        initContents(dm.getRecipient().getName());
    }
    public void initialize(Group group){
        this.group = group;
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
            MessageFXML messageFXML = fxmlLoader.getController();
            messageFXML.initialize(message, participants.get(indexOf(message.getUsername(), participants)));
        }

        initContents(group.getName());
    }
    private int indexOf(String username, List<User> participants) {
        for (int i = 0; i < participants.size(); i++)
            if (participants.get(i).getUsername().equals(username)) return i;
        return -1;
    }

    @FXML void usernameClicked(){
        if (isGroupType){
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.GROUP_STATS_FXML_PATH));
            try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
            ((GroupStatsFXML)fxmlLoader.getController()).initialize(group);
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(dm.getRecipient());
    }

    @FXML void send(){
        if (message.getText().equals("")) {
            AppManager.alert(Alert.AlertType.WARNING, "WARNING!",
                    "You cannot send an empty message.", "Empty message!");
            return;
        }
        if (isGroupType) sendGroupMessage();
        else sendMessage();
        message.clear();
        removeReply();
    }
    private void sendGroupMessage() {
        LocalDateTime now = LocalDateTime.now();
        GroupMessage newMessage = new GroupMessage();
        newMessage.setID(new SaveHandle(Saver.addToGroupMessages(group.getGroupID().getHandle(),
                Loginner.loginnedUser.getUsername(), Loginner.loginnedUser.getUsername(), 0,
                now, message.getText(), replyID.getHandle())));
        newMessage.setDate(now);
        newMessage.setContent(message.getText());
        newMessage.setOriginalMessage(newMessage.getID());
        newMessage.setOriginalUsername(Loginner.loginnedUser.getUsername());
        newMessage.setUsername(Loginner.loginnedUser.getUsername());
        newMessage.setGroup(group);
        newMessage.setReplyToID(replyID);

        messages.addFirst(newMessage);

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
        try {display.getChildren().add(0, fxmlLoader.load());}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR, "Exception occurred.",
                e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((MessageFXML)fxmlLoader.getController()).initialize(newMessage, Loginner.loginnedUser);
    }
    private void sendMessage() {
        if (Loader.isUserBlocked(dm.getRecipient().getUsername(), Loginner.loginnedUser.getUsername())){
            AppManager.alert(Alert.AlertType.ERROR, "You have been blocked :(",
                    "You cannot send messages to this user anymore.", "BLOCKED!");
            return;
        }
        if (Loginner.loginnedUser.getBlocklist().stream().anyMatch(u-> u.equals(dm.getRecipient().getUsername()))){
            AppManager.alert(Alert.AlertType.ERROR, "You have blocked this user!",
                    "You cannot send messages to users whom you have blocked.", "BLOCK!");
            return;
        }


        LocalDateTime now = LocalDateTime.now();
        Message newMessage = new Message();
        newMessage.setID(new SaveHandle(Saver.addToMessages(dm.getUser().getUsername(), dm.getRecipient().getUsername(),
                Loginner.loginnedUser.getUsername(), 0, now, message.getText(), replyID.getHandle())));
        newMessage.setDate(now);
        newMessage.setContent(message.getText());
        newMessage.setOriginalMessage(newMessage.getID());
        newMessage.setOriginalUsername(Loginner.loginnedUser.getUsername());
        newMessage.setUsername(dm.getUser().getUsername());
        newMessage.setReplyToID(replyID);

        messages.addFirst(newMessage);

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
        try {display.getChildren().add(0, fxmlLoader.load());}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR, "Exception occurred.",
                e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((MessageFXML)fxmlLoader.getController()).initialize(newMessage, Loginner.loginnedUser);

        if (Loginner.loginnedUser.getChats().stream().noneMatch(username -> username.equals(dm.getRecipient().getUsername())))
            Loginner.loginnedUser.getChats().add(dm.getRecipient().getUsername());
    }

    @FXML void hoverSend(){new Pulse(sendButton).play();}

    public void replyMode(Message toMessage) {
        if (replyContainer != null) replyContainer.getRoot().getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.REPLYING_FXML_PATH));
        try {replyNode = fxmlLoader.load();} catch (IOException e)
        {AppManager.alert(Alert.AlertType.ERROR, "Exception occurred.", e.getCause().getMessage(),
                "Exception"); e.printStackTrace(); return;}
        ((ReplyingFXML)fxmlLoader.getController()).initialize(toMessage);
        replyContainer = new PopOver(replyNode);
        replyContainer.show(name);
        replyContainer.autoHideProperty().setValue(false);
        replyID = toMessage.getID();
    }
    public void removeReply() {
        if (replyContainer != null){
            replyContainer.getRoot().getChildren().clear();
            replyContainer.hide();
        }
        replyID = new SaveHandle(0);
    }

    public void applyEdit(Message message){
        editingMessage = message;
        popupStage = new Stage(StageStyle.UTILITY);
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.EDIT_FXML_PATH));
        try {popupStage.setScene(new Scene(fxmlLoader.load(), Utility.EDIT_PREF_WIDTH, Utility.EDIT_PREF_HEIGHT));}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((EditFXML)fxmlLoader.getController()).initialize(message.getContent());
        popupStage.getScene().getStylesheets().add(Theme.currentTheme.toString());
        new SlideInUp(popupStage.getScene().getRoot()).play();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }
    public void cancelEdit() {
        popupStage.close();
        editingMessage = null;
    }
    public void confirmEdit(String newMsg) {
        popupStage.close();
        editingMessage.setContent(newMsg);
        Changer.editMessage(editingMessage.getID().getHandle(), newMsg);
        refresh();
    }

    void initForward(Message message){
        forwardingMessage = message;
        popupStage = new Stage(StageStyle.UTILITY);
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.FORWARD_FXML_PATH));
        try {popupStage.setScene(new Scene(fxmlLoader.load(), Utility.FORWARD_PREF_WIDTH, Utility.FORWARD_PREF_HEIGHT));}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((ForwardFXML)fxmlLoader.getController()).initialize(Loginner.loginnedUser.getChats(),
                Loginner.loginnedUser.getGroups());
        popupStage.getScene().getStylesheets().add(Theme.currentTheme.toString());
        new SlideInUp(popupStage.getScene().getRoot()).play();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.showAndWait();
    }
    public void forward(Group group) {
        LocalDateTime now = LocalDateTime.now();
        GroupMessage newMessage = new GroupMessage();
        newMessage.setID(new SaveHandle(Saver.addToGroupMessages(group.getGroupID().getHandle(),
                Loginner.loginnedUser.getUsername(), forwardingMessage.getOriginalUsername(), forwardingMessage.getOriginalMessage().getHandle(),
                now, forwardingMessage.getContent(), 0)));
        if (group.getGroupID().equals(this.group.getGroupID())) {
            newMessage.setDate(now);
            newMessage.setContent(forwardingMessage.getContent());
            newMessage.setOriginalMessage(forwardingMessage.getID());
            newMessage.setOriginalUsername(Loginner.loginnedUser.getUsername());
            newMessage.setUsername(Loginner.loginnedUser.getUsername());
            newMessage.setGroup(group);
            newMessage.setReplyToID(new SaveHandle(0));

            group.getShownMessages().addFirst(newMessage);
            refresh();
        }

        popupStage.close();
    }
    public void forward(DirectMessenger dm) {
        LocalDateTime now = LocalDateTime.now();
        Message newMessage = new Message();
        newMessage.setID(new SaveHandle(Saver.addToMessages(dm.getUser().getUsername(), dm.getRecipient().getUsername(),
                forwardingMessage.getOriginalUsername(), forwardingMessage.getOriginalMessage().getHandle(),
                now, forwardingMessage.getContent(), 0)));
        if (dm.getRecipient().getUsername().equals(this.dm.getRecipient().getUsername())) {
            newMessage.setDate(now);
            newMessage.setContent(forwardingMessage.getContent());
            newMessage.setOriginalMessage(forwardingMessage.getID());
            newMessage.setOriginalUsername(Loginner.loginnedUser.getUsername());
            newMessage.setUsername(this.dm.getUser().getUsername());
            newMessage.setReplyToID(new SaveHandle(0));

            this.dm.getShownMessages().addFirst(newMessage);
            refresh();
        }
        popupStage.close();
    }

    void refresh() {
        display.getChildren().clear();
        if (isGroupType) initialize(group);
        else initialize(dm);
    }
    void refreshForDelete(Message message) {
        display.getChildren().clear();
        if (isGroupType){
            group.getShownMessages().remove((GroupMessage) message);
            initialize(group);
        } else {
            dm.getShownMessages().remove(message);
            initialize(dm);
        }
    }
}
