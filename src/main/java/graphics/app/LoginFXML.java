package graphics.app;

import Login.Loginner;
import animatefx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginFXML {
    private static final double ANIMATION_SPEED = 1.8;

    @FXML Hyperlink forgotMyPassword;
    @FXML TextField usernameField;
    @FXML PasswordField passwordField;
    @FXML Button loginButton, createButton;
    @FXML Label description;

    public void initialize(Parent root) {
        int delay = 5; //ms
        AnimationFX rootAnim = new SlideInUp(root);
        rootAnim.setSpeed(ANIMATION_SPEED);
        AnimationFX usernameFX = new SlideInLeft(usernameField);
        usernameFX.setSpeed(ANIMATION_SPEED);
        AnimationFX passwordFX = new SlideInLeft(passwordField);
        passwordFX.setSpeed(ANIMATION_SPEED);
        AnimationFX loginFX = new SlideInLeft(loginButton);
        loginFX.setSpeed(ANIMATION_SPEED);
        AnimationFX createFX = new SlideInLeft(createButton);
        createFX.setSpeed(ANIMATION_SPEED);
        AnimationFX forgotFX = new FadeIn(forgotMyPassword);
        forgotFX.setSpeed(ANIMATION_SPEED);
        AnimationFX descriptionFX = new SlideInRight(description);
        descriptionFX.setSpeed(ANIMATION_SPEED);
        rootAnim.setOnFinished(e-> {usernameFX.play(); Utility.delay(delay, () -> usernameField.setVisible(true));});
        usernameFX.setOnFinished(e-> {passwordFX.play(); Utility.delay(delay, () -> passwordField.setVisible(true));});
        passwordFX.setOnFinished(e-> {loginFX.play(); createFX.play();
            Utility.delay(delay, () -> {loginButton.setVisible(true); createButton.setVisible(true);});});
        loginFX.setOnFinished(e-> {forgotFX.play(); descriptionFX.play();
            Utility.delay(delay, () -> {forgotMyPassword.setVisible(true); description.setVisible(true);});});
        rootAnim.play();
    }

    @FXML
    public void login(){
        String username = usernameField.getText(), password = passwordField.getText();
        if (username.equals("") || password.equals("")){
            AppManager.alert(Alert.AlertType.ERROR, "Some fields have been left out!",
                    "Please fill them up and try again.", "Some fields are empty...");
            return;
        }

        try {Loginner.attemptLoginGraphical(username, password);} catch (IOException e) {e.printStackTrace();}
    }
    @FXML
    public void create(){
        AnimationFX leaveLoginFX = new SlideOutLeft(AppManager.loginStage.getScene().getRoot());
        leaveLoginFX.play();
        leaveLoginFX.setOnFinished(e-> AppManager.switchToCreate());
    }
    @FXML
    public void forgot(){
        AnimationFX leaveLoginFX = new SlideOutLeft(AppManager.loginStage.getScene().getRoot());
        leaveLoginFX.play();
        leaveLoginFX.setOnFinished(e-> AppManager.switchToForgot());
    }

    @FXML public void hoverLogin(){new Pulse(loginButton).play();}
    @FXML public void hoverCreate(){new Pulse(createButton).play();}
    @FXML public void hoverForgot(){new Pulse(forgotMyPassword).play();}
    @FXML public void hoverUser(){new Pulse(usernameField).play();}
    @FXML public void hoverPassword(){new Pulse(passwordField).play();}
    @FXML public void hoverDescription(){new Pulse(description).play();}

    public void initializeLessAnimation(Parent root) {
        AnimationFX rootAnim = new SlideInRight(root);
        rootAnim.play();
        createButton.setVisible(true);
        loginButton.setVisible(true);
        forgotMyPassword.setVisible(true);
        description.setVisible(true);
        usernameField.setVisible(true);
        passwordField.setVisible(true);
    }
}
