package graphics.app;

import Login.Loginner;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import java.io.IOException;
import java.net.MalformedURLException;

public class MyAccountFXML {
    @FXML GridPane preview;
    @FXML TextField nameField;
    @FXML TextArea bioField, subtitleField;
    @FXML Button pictureButton, updatePreviewButton;
    @FXML Label nameLabel, bioLabel, subtitleLabel;

    private void updatePreview(){
        update();
        preview.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POST_FXML_PATH));
        try {preview.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((PostFXML)fxmlLoader.getController()).initializeSample();
    }

    public void initialize(){
        nameField.setText(Loginner.loginnedUser.getName());
        bioField.setText(Loginner.loginnedUser.getBio());
        subtitleField.setText(Loginner.loginnedUser.getSubtitle());
        updatePreview();
    }

    @FXML void changePicture(){
        String directory;
        FileChooser fileChooser = new FileChooser();
        try {
            directory = fileChooser.showOpenDialog(AppManager.mainStage).toURI().toURL().toExternalForm();
            Database.Changer.setUserPfp(Loginner.loginnedUser.getUsername(), directory);
            Loginner.loginnedUser.getPfp().setHandle(directory);
            updatePreview();
        }
        catch (MalformedURLException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace();}
    }

    @FXML void hoverNameLabel(){new Pulse(nameLabel).play();}
    @FXML void hoverBioLabel(){new Pulse(bioLabel).play();}
    @FXML void hoverSubtitleLabel(){new Pulse(subtitleLabel).play();}
    @FXML void hoverNameField(){new Pulse(nameField).play();}
    @FXML void hoverBioField(){new Pulse(bioField).play();}
    @FXML void hoverSubtitleField(){new Pulse(subtitleField).play();}
    @FXML void hoverChangeButton(){new Pulse(pictureButton).play();}
    @FXML void hoverUpdateButton(){new Pulse(updatePreviewButton).play();}

    @FXML void update(){
        boolean name = Loginner.loginnedUser.getName().equals(nameField.getText()),
                bio = Loginner.loginnedUser.getBio().equals(bioField.getText()),
                subtitle = Loginner.loginnedUser.getSubtitle().equals(subtitleField.getText());

        if (!name) {
            Database.Changer.setUserName(Loginner.loginnedUser.getUsername(), nameField.getText());
            Loginner.loginnedUser.setName(nameField.getText());
        }
        if (!bio) {
            Database.Changer.setUserBio(Loginner.loginnedUser.getUsername(), bioField.getText());
            Loginner.loginnedUser.setBio(bioField.getText());
        }
        if (!subtitle) {
            Database.Changer.setUserSubtitle(Loginner.loginnedUser.getUsername(), subtitleField.getText());
            Loginner.loginnedUser.setSubtitle(subtitleField.getText());
        }


        if (!(name && bio && subtitle)){
            updatePreview();
        }
    }
}
