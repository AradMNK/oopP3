package graphics.app;

import Login.Loginner;
import Objects.Group;
import animatefx.animation.Pulse;
import graphics.theme.Theme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class GroupStatsFXML {
    static Stage popup;
    Group group;
    @FXML Circle picture;
    @FXML Text name, link;
    @FXML Button addButton, unbanButton, editButton;
    @FXML GridPane participants, picturePane;

    void initialize(Group group){
        this.group = group;

        updateContent();
        updateParticipants();
    }

    @FXML void add(){
        popup = new Stage(StageStyle.UTILITY);
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_SEARCHER_FXML_PATH));
        try {popup.setScene(new Scene(fxmlLoader.load()));}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserSearcherFXML)fxmlLoader.getController()).initializeAdd(group);
        popup.getScene().getStylesheets().add(Theme.currentTheme.toString());
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        updateContent();
    }
    @FXML void unban(){
        popup = new Stage(StageStyle.UTILITY);
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_SEARCHER_FXML_PATH));
        try {popup.setScene(new Scene(fxmlLoader.load()));}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserSearcherFXML)fxmlLoader.getController()).initializeUnban(group);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
        updateContent();
    }
    @FXML void edit(){
        popup = new Stage(StageStyle.UTILITY);
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.EDIT_GROUP_FXML_PATH));
        try {popup.setScene(new Scene(fxmlLoader.load()));}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((EditGroupFXML)fxmlLoader.getController()).initialize(this, group);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
    }

    @FXML void hoverUnban(){new Pulse(unbanButton).play();}
    @FXML void hoverAdd(){new Pulse(addButton).play();}
    @FXML void hoverEdit(){new Pulse(editButton).play();}

    public void updateContent() {
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
        name.setText(group.getName());
        link.setText(group.getGroupJoiner());
    }
    private void updateParticipants() {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH));
        try {participants.getChildren().add(fxmlLoader.load());}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}

        if (Loginner.loginnedUser.getUsername().equals(group.getOwner().getUsername())) {
            unbanButton.setVisible(true);
            ((UsersFXML)fxmlLoader.getController()).initializeOwnerMode(group.getParticipants(), group);
        } else ((UsersFXML)fxmlLoader.getController()).initialize(group.getParticipants());
    }
}
