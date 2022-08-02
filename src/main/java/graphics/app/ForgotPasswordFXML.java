package graphics.app;

import Login.Hasher;
import Login.SecurityQuestion;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class ForgotPasswordFXML {
    private String username;
    ImageView imageView;
    @FXML TextField usernameField, answerField;
    @FXML PasswordField passwordField;
    @FXML Button changeButton, backButton, applyButton, checkButton;
    @FXML Label securityQuestionShower;

    public void initialize(Parent root) {
        AnimationFX rootAnim = new SlideInRight(root);
        rootAnim.play();
        Utility.delay(50, () -> root.setVisible(true));
    }

    @FXML public void change() {
        String password = passwordField.getText();
        if (password.equals("")){
            AppManager.alert(Alert.AlertType.ERROR, "You cannot have an empty password!",
                    "Please fill it up and try again.", "Some fields are empty...");
            return;
        }

        Database.Changer.changePassword(username, Hasher.hash(password));

        AppManager.alert(Alert.AlertType.INFORMATION, "Successfully changed user @" +
                username + "'s password!", "You can now login with it!", "Success");

        AnimationFX leaveCreateAccountFX = new SlideOutLeft(AppManager.loginStage.getScene().getRoot());
        leaveCreateAccountFX.setOnFinished(e-> AppManager.switchToLogin());
        leaveCreateAccountFX.play();
    }
    @FXML public void check() {
        String answer = answerField.getText();
        if (!Database.Loader.doesSecurityQuestionAnswerMatch(username, Hasher.hash(answer))){
            AppManager.alert(Alert.AlertType.ERROR, "Answer does not match-up with the question answered.",
                    "Please try again.", "Wrong answer!");
            return;
        }

        AppManager.alert(Alert.AlertType.INFORMATION, "Your input answer was correct.",
                "You may now proceed to change your password. If you did not mean to, use the back button.",
                "Correct answer!");

        securityQuestionShower.setDisable(true);
        answerField.setDisable(true);
        checkButton.setDisable(true);
        passwordField.setDisable(false);
        changeButton.setDisable(false);
    }
    @FXML public void back() {
        AnimationFX leaveCreateAccountFX = new SlideOutLeft(AppManager.loginStage.getScene().getRoot());
        leaveCreateAccountFX.setOnFinished(e-> AppManager.switchToLogin());
        leaveCreateAccountFX.play();
    }
    @FXML public void apply() {
        username = usernameField.getText();
        if (username.equals("")){
            AppManager.alert(Alert.AlertType.ERROR, "Are you seriously searching for an empty username?",
                    "Please fill the username up and try again.", "Some fields are empty...");
            return;
        }

        if (!Database.Loader.usernameExists(username)){
            graphics.app.AppManager.alert(Alert.AlertType.ERROR, "Username not found...", "Check for spelling errors " +
                    "because usernames are case-sensitive.", "Not found!");
            username = null;
            return;
        }

        AppManager.alert(Alert.AlertType.INFORMATION, "Successfully fetched @" +
                username + "'s security question.", "Please answer the question now.", "Success");

        securityQuestionShower.setText(SecurityQuestion.getSecurityQuestionByNumber
                (Database.Loader.getSecurityQuestionNumber(username)).toString());
        securityQuestionShower.setDisable(false);
        answerField.setDisable(false);
        checkButton.setDisable(false);
        passwordField.setDisable(true);
        changeButton.setDisable(true);
    }


    @FXML public void hoverBack() {new Pulse(backButton).play();}
    @FXML public void hoverChange() {new Pulse(changeButton).play();}
    @FXML public void hoverApply() {new Pulse(applyButton).play();}
    @FXML public void hoverCheck() {new Pulse(checkButton).play();}
    @FXML public void hoverUser() {new Pulse(usernameField).play();}
    @FXML public void hoverPassword() {new Pulse(passwordField).play();}
    @FXML public void hoverAnswer() {new Pulse(answerField).play();}
    @FXML public void hoverQuestion() {new Pulse(securityQuestionShower).play();}
}
