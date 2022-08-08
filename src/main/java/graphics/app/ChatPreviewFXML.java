package graphics.app;

import Database.Changer;
import Database.Loader;
import Login.Loginner;
import animatefx.animation.Pulse;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import Objects.User;
import Objects.Message;
import Objects.Group;
import Objects.GroupMessage;
import Objects.DirectMessenger;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Objects;

public class ChatPreviewFXML {
    boolean isGroupType = false;
    Group group;
    DirectMessenger dm;
    @FXML Circle picture;
    @FXML Text name, header, content;
    @FXML GridPane picturePane, chatPane;
    @FXML Label unreadLabel;

    private void initContents(String name, String header, String content){
        this.name.setText(name);
        this.header.setText(header);
        this.content.setText(stripContent(content));
        picture.radiusProperty().bind(Bindings.min(picturePane.heightProperty(), picturePane.widthProperty()).divide(2));
    }

    private String stripContent(String content) {
        if (content.length() > Utility.MAX_PREVIEW_CHARS)
            return content.substring(0, Utility.MAX_PREVIEW_CHARS - 3) + "...";
        return content;
    }

    public void initialize(User user, Message message, DirectMessenger dm){
        this.dm = dm;
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
        if (dm.getRecipient().getUsername().equals(Loginner.loginnedUser.getUsername()))
            initContents(Utility.SAVED_MESSAGES, "", message.getContent());
        else initContents(user.getName(), "", message.getContent());

        int unread = Loader.getUnreadCountForUsername(dm.getUser().getUsername(), dm.getRecipient().getUsername());
        if (unread == 0) unreadLabel.setVisible(false);
        else unreadLabel.setText(Integer.toString(unread));
    }

    public void initialize(Group group, GroupMessage message){
        isGroupType = true;
        this.group = group;
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
        initContents(group.getName(), Database.Loader.getUserName(message.getUsername()), message.getContent());
        int unread = Loader.getUnreadCountForGroupID(Loginner.loginnedUser.getUsername(), group.getGroupID().getHandle());
        if (unread == 0) unreadLabel.setVisible(false);
        else unreadLabel.setText(Integer.toString(unread));
    }
    public void initialize(Group group) {
        isGroupType = true;
        this.group = group;
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
        initContents(group.getName(), "", "(No one has messaged in this group yet...)");
        int unread = Loader.getUnreadCountForGroupID(Loginner.loginnedUser.getUsername(), group.getGroupID().getHandle());
        if (unread == 0) unreadLabel.setVisible(false);
        else unreadLabel.setText(Integer.toString(unread));
    }

    @FXML void hoverChatPane(){new Pulse(chatPane).play();}
    @FXML void clickChatPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CHAT_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        if (isGroupType){
            ((ChatFXML)fxmlLoader.getController()).initialize(group);
            Changer.userSees(Loginner.loginnedUser.getUsername(), group.getGroupID().getHandle());
        } else {
            ((ChatFXML)fxmlLoader.getController()).initialize(dm);
            Changer.userSees(Loginner.loginnedUser.getUsername(), dm.getRecipient().getUsername());
        }
    }

    void changeChatPaneClickToForward(){chatPane.setOnMouseClicked(e->clickChatPaneToForward());}
    private void clickChatPaneToForward() {
        if (isGroupType) ChatFXML.chatFXML.forward(group);
        else ChatFXML.chatFXML.forward(dm);
    }
}
