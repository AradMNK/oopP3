package graphics.app;

import Objects.Group;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.Objects;

public class GroupStatsFXML {
    Group group;
    @FXML Circle picture;
    @FXML Text name, link;
    @FXML Button addButton, unbanButton;
    @FXML GridPane display, picturePane;

    void initialize(Group group){
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
    }

    @FXML void add(){

    }
    @FXML void unban(){

    }

    @FXML void hoverUnban(){new Pulse(unbanButton).play();}
    @FXML void hoverAdd(){new Pulse(addButton).play();}
}
