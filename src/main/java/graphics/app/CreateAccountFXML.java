package graphics.app;

import Login.Creator;
import Login.SecurityQuestion;
import Objects.UserType;
import animatefx.animation.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.util.Arrays;

public class CreateAccountFXML {
    @FXML TextField usernameField, nameField, answerField;
    @FXML PasswordField passwordField;
    @FXML Button createButton, backButton;
    @FXML ChoiceBox<UserType> userTypePicker;
    @FXML ComboBox<SecurityQuestion> securityQuestionPicker;

    public void initialize(Parent root) {
        AnimationFX rootAnim = new SlideInRight(root);
        rootAnim.play();
        Utility.delay(50, () -> root.setVisible(true));

        securityQuestionPicker.setItems(FXCollections.observableList(Arrays.asList(SecurityQuestion.getValues())));
        userTypePicker.setItems(FXCollections.observableList(Arrays.asList(UserType.getValues())));
        userTypePicker.getSelectionModel().select(0);
    }

    @FXML
    public void create(){
        String username = usernameField.getText(), password = passwordField.getText(),
                name = nameField.getText(), answer = answerField.getText();
        UserType type = userTypePicker.getValue();
        SecurityQuestion question = securityQuestionPicker.getValue();
        if (username.equals("") || password.equals("") || name.equals("") || answer.equals("") ||
                question == null){
            AppManager.alert(Alert.AlertType.ERROR, "Some fields have been left out!",
                    "Please fill them up and try again.", "Some fields are empty...");
            return;
        }

        if (Creator.attemptCreateGraphical(username, password, name, question.saverID, answer, type.toString())){
            AppManager.alert(Alert.AlertType.INFORMATION, "Successfully created an account with " +
                        "username @" + username + "!", "You can now login with it!", "Success");
            AnimationFX leaveCreateAccountFX = new SlideOutLeft(AppManager.loginStage.getScene().getRoot());
            leaveCreateAccountFX.setOnFinished(e-> AppManager.switchToLogin());
            leaveCreateAccountFX.play();
        }
    }
    @FXML
    public void back(){
        AnimationFX leaveCreateAccountFX = new SlideOutLeft(AppManager.loginStage.getScene().getRoot());
        leaveCreateAccountFX.setOnFinished(e-> AppManager.switchToLogin());
        leaveCreateAccountFX.play();
    }


    @FXML public void hoverBack(){new Pulse(backButton).play();}
    @FXML public void hoverCreate(){new Pulse(createButton).play();}
    @FXML public void hoverName(){new Pulse(nameField).play();}
    @FXML public void hoverUser(){new Pulse(usernameField).play();}
    @FXML public void hoverPassword(){new Pulse(passwordField).play();}
    @FXML public void hoverAnswer(){new Pulse(answerField).play();}
    @FXML public void hoverQuestion(){ if (securityQuestionPicker.isArmed()) return;
        new Pulse(securityQuestionPicker).play();}
    @FXML public void hoverType(){new Pulse(userTypePicker).play();}
}
