package graphics.app;

import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import Objects.User;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UserFXML {
    @FXML Button followButton;
    @FXML Circle picture;
    @FXML Text name, username, bio, subtitle, date;
    @FXML ScrollPane bioPane;
    @FXML GridPane picturePane;

    private void initContents(String name, String username, String bio, String subtitle, LocalDate time){
        this.name.setText(name);
        this.username.setText("@" + username);
        this.bio.setText(bio);
        this.subtitle.setText(subtitle);
        this.date.setText(time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        this.bio.wrappingWidthProperty().bind(bioPane.widthProperty().subtract(Utility.POST_TEXT_MARGIN_FROM_RIGHT));
        picture.radiusProperty().bind(picturePane.heightProperty().divide(2));
    }


    public void initialize(User user){
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

        initContents(user.getName(), user.getUsername(), user.getBio(), user.getSubtitle(), user.getDateJoined());
    }

    @FXML void follow(){

    }

    @FXML void hoverFollow(){new Pulse(followButton).play();}
}
