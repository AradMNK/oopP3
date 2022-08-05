package graphics.app;

import Builder.UserBuilder;
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
import Objects.DirectMessenger;
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
    @FXML Text name, date, msg, forwarded;
    @FXML GridPane picturePane, messagePane;
    @FXML Hyperlink forwardedUser;

    @FXML void reply(){

    }
    @FXML void edit(){
    }
    @FXML void forward(){

    }
    @FXML void delete(){

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
            forwardButton.setDisable(true);
            deleteButton.setDisable(true);
        }
        initContents(user.getName(), message.getContent(), message.getDate());

        if (!message.getUsername().equals(message.getOriginalUsername())){

        }
    }

    @FXML void usernameClick(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {MainFXML.root.setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(UserBuilder.getUserFromDatabase(message.getUsername()));
    }
    @FXML void forwardedUserClick(){

    }
}
