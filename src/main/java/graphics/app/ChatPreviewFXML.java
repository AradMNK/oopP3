package graphics.app;

import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
import java.util.Objects;

public class ChatPreviewFXML {
    DirectMessenger dm;
    @FXML Circle picture;
    @FXML Text name, header, content;
    @FXML GridPane picturePane, chatPane;

    private void initContents(String name, String header, String content){
        this.name.setText(name);
        this.header.setText(header);
        this.content.setText(stripContent(content));
        picture.radiusProperty().bind(picturePane.widthProperty().divide(2));
    }

    private String stripContent(String content) {
        if (content.length() > Utility.MAX_PREVIEW_CHARS)
            return content.substring(0, Utility.MAX_PREVIEW_CHARS - 3) + "...";
        return content;
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
                    ((Objects.requireNonNull(Launcher.class.getResource(Utility.GROUP_PICTURE_PATH))).toString())));
        else{
            try {picture.setFill(new ImagePattern(new Image(group.getPfp().getHandle())));}
            catch (IllegalArgumentException e){AppManager.alert(Alert.AlertType.ERROR, "Unsupported image file!",
                    "Please choose another image.", "Image could not load!");
                picture.setFill(new ImagePattern(new Image((Objects.requireNonNull
                        (Launcher.class.getResource(Utility.GROUP_PICTURE_PATH))).toString())));}
        }

        initContents(group.getName(), Database.Loader.getUserName(message.getUsername()), message.getContent());
    }

    @FXML void hoverChatPane(){new Pulse(chatPane).play();}

    @FXML void clickChatPane(){
        //FIXME
    }
}
