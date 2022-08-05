package graphics.app;

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
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.*;

public class ChatFXML {
    LinkedList<Message> messages;
    @FXML Circle picture;
    @FXML Text name;
    @FXML GridPane picturePane;
    @FXML Button sendButton;
    @FXML TextArea message;
    @FXML VBox display;

    private void initContents(String name, DirectMessenger dm){
        this.name.setText(name);
        for (Message message : messages){
            FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
            try {display.getChildren().add(fxmlLoader.load());}
            catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();
                    continue;}
            User user;
            if (dm.getUser().getUsername().equals(message.getUsername())) user = dm.getUser();
            else if (dm.getRecipient().getUsername().equals(message.getUsername())) user = dm.getRecipient();
            else{
                AppManager.alert(Alert.AlertType.ERROR, "Unknown error...", "Message did not correspond" +
                        " to any of the chat members... (@" + message.getUsername() + ")", "ERROR...?");
                continue;
            }
            ((MessageFXML)fxmlLoader.getController()).initialize(message, user);
        }
        picture.radiusProperty().bind(picturePane.widthProperty().divide(2));
    }

    public void initialize(User user, DirectMessenger dm){
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
        initContents(user.getName(), dm);
    }

    public void initialize(Group group){
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

        //initContents(group.getName(), Database.Loader.getUserName(message.getUsername()), message.getContent());
    }

    @FXML void send(){

    }

    @FXML void hoverSend(){new Pulse(sendButton).play();}
}
