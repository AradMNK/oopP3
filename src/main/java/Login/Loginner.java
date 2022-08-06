package Login;

import Objects.User;
import TextController.TextController;
import Builder.UserBuilder;
import graphics.app.AppManager;
import javafx.scene.control.Alert;

import java.io.IOException;

public class Loginner {
    public static LoginState loginState = LoginState.SIGN_OUT;
    public static User loginnedUser = null;

    public static void attemptLogin(String[] args){
        String username, password;

        if (args.length == 0) {
            TextController.println("Please enter username and password: (username) (password)");
            attemptLogin(TextController.getLine().split("\\s"));
            return;
        }
        if (args.length < 2){
            username = args[0];
            TextController.println("Enter password for " + username + ":");
            String newLine = TextController.getLine();
            int index = newLine.indexOf(' ');
            if (index == -1) password = newLine;
            else password = newLine.substring(0, index);
        }
        else{
            username = args[0];
            password = args[1];
        }

        attemptLogin(username, password);
    }

    public static void attemptLogin(String user, String pass) {
        if (loginState == LoginState.SIGN_IN) {
            TextController.println("You are already logged in as " + loginnedUser);
            return;
        }

        if (!Database.Loader.usernameExists(user)){
            TextController.println("Username does not exist. Please try again.");
            return;
        }

        boolean worked;
        worked = Database.Loader.loginMatch(user, Hasher.hash(pass));

        if (worked){
            loginState = LoginState.getLoginState(user);
            loginnedUser = UserBuilder.getUserFromDatabaseFull(user);
            TextController.println("Successfully loginned as " + user);
        } else
            TextController.println("Could not match the credentials.");
    }

    public static void attemptLoginGraphical(String user, String pass) throws IOException {
        if (!Database.Loader.usernameExists(user)){
            graphics.app.AppManager.alert(Alert.AlertType.ERROR, "Username not found...", "Check for spelling errors " +
                    "because usernames are case-sensitive.", "Not found!");
            return;
        }

        if (Database.Loader.loginMatch(user, Hasher.hash(pass))){ //login successful?
            loginState = LoginState.getLoginState(user);
            loginnedUser = UserBuilder.getUserFromDatabaseFull(user);
            AppManager.launchMain();
        } else
            graphics.app.AppManager.alert(Alert.AlertType.ERROR, "Incorrect password...", "Check for spelling errors " +
                    "because usernames are case-sensitive.", "Wrong password!");
    }

    public static void signout() {
        loginState = LoginState.SIGN_OUT;
        loginnedUser = null;
        TextController.println("Signed out.");
    }

    public static void reload(){
        if (loginState == LoginState.SIGN_OUT) {
            TextController.println("You have to be loginned first to reload.");
            return;
        }

        loginnedUser = UserBuilder.getUserFromDatabaseFull(loginnedUser.getUsername());
    }

    public static void forgotPassword(String username) {
        if (!Database.Loader.usernameExists(username)){
            TextController.println("Username [@" + username + "] does not exist.");
            return;
        }

        int secQuestionNum = Database.Loader.getSecurityQuestionNumber(username);

        SecurityQuestion securityQuestion = SecurityQuestion.getSecurityQuestionByNumber(secQuestionNum);
        TextController.println("Please answer the following question:");
        TextController.println("\"" + securityQuestion + "\"");

        String answer = TextController.getLine();
        TextController.println("You have typed in \"" + answer + "\".");

        if (!Database.Loader.doesSecurityQuestionAnswerMatch(username, Hasher.hash(answer))){
            TextController.println("Answer does not match. Consider the fact that the answers are case sensitive.");
            return;
        }
        TextController.println("Please enter your new password:");
        String password = TextController.getLine();
        Database.Changer.changePassword(username, Hasher.hash(password));
    }
}
