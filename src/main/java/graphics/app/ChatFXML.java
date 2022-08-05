package graphics.app;

import Objects.*;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Objects;

public class ChatFXML {

    DirectMessenger dm;
    @FXML Circle picture;
    @FXML Text name;
    @FXML GridPane picturePane;
    @FXML Button sendButton;
    @FXML TextArea message;

    private void initContents(String name, String header, String content){
        this.name.setText(name);
        picture.radiusProperty().bind(picturePane.widthProperty().divide(2));
    }

    public void initialize(User user, Message message, DirectMessenger dm){
        this.dm = dm;
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
        initContents(user.getName(), "", message.getContent());
    }

    public void initialize(Group group, GroupMessage message){
        if (group.getPfp().getHandle().equals(""))
            picture.setFill(new ImagePattern(new Image
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));
        else{
            try {picture.setFill(new ImagePattern(new Image(group.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                picture.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.UNKNOWN_USER_PICTURE))).toString())));}
        }

        initContents(group.getName(), Database.Loader.getUserName(message.getUsername()), message.getContent());
    }

    @FXML void send(){
        //FIXME
    }

    @FXML void hoverSend(){new Pulse(sendButton).play();}
}
