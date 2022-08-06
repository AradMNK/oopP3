package graphics.app;

import Database.Changer;
import Database.Loader;
import Objects.Group;
import animatefx.animation.Pulse;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.net.MalformedURLException;
import java.util.Objects;

public class EditGroupFXML {
    Group group;
    GroupStatsFXML root;
    String picturePath;

    @FXML TextField nameField, linkField;
    @FXML Button createButton, backButton, changeButton;
    @FXML Circle picture;
    @FXML GridPane picturePane;

    public void initialize(GroupStatsFXML root, Group group){
        this.group = group;
        picturePath = group.getPfp().getHandle();
        this.root = root;
        picture.setFill(new ImagePattern(new Image
                ((Objects.requireNonNull(Launcher.class.getResource(Utility.GROUP_PICTURE_PATH))).toString())));
        picture.radiusProperty().bind(Bindings.min(picturePane.heightProperty(), picturePane.widthProperty()).divide(2));
    }

    @FXML void change(){
        FileChooser fileChooser = new FileChooser();
        try {
            picturePath = fileChooser.showOpenDialog(AppManager.mainStage).toURI().toURL().toString();
            picture.setFill(new ImagePattern(new Image(picturePath)));
        } catch (MalformedURLException e) {e.printStackTrace();}
    }
    @FXML void create(){
        if (nameField.getText().equals("") || linkField.getText().equals("")){
            AppManager.alert(Alert.AlertType.ERROR, "Some fields have been left out!",
                    "Please fill them up and try again.", "Some fields are empty...");
            return;
        }

        if (Loader.groupJoinerExists(linkField.getText())){
            AppManager.alert(Alert.AlertType.ERROR, "This link already exists! [@" + linkField.getText() + "]",
                    "Please choose a new one and continue.", "Already exists!");
            return;
        }

        group.setName(nameField.getText());
        group.setGroupJoiner(linkField.getText());
        Changer.changeGroupJoiner(group.getGroupID().getHandle(), group.getGroupJoiner());
        Changer.changeGroupName(group.getGroupID().getHandle(), group.getName());

        root.update();
    }
    @FXML void back(){root.popup.close();}

    @FXML void hoverCreate(){new Pulse(createButton).play();}
    @FXML void hoverBack(){new Pulse(backButton).play();}
    @FXML void hoverChange(){new Pulse(changeButton).play();}
    @FXML void hoverLink(){new Pulse(linkField).play();}
    @FXML void hoverName(){new Pulse(nameField).play();}
}
