package graphics.app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AppManager {
    static Stage loginStage, mainStage = null;

    public static void launchLogin(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.LOGIN_FXML_PATH));
        Scene scene = new Scene(fxmlLoader.load(), Utility.PREF_WIDTH, Utility.PREF_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(Launcher.class.getResource
                (Utility.LIGHT_MODE_CSS_PATH)).toString());
        stage.setTitle(Utility.APP_TITLE);
        stage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResource
                (Utility.ICON_PATH)).toString()));
        stage.setScene(scene);
        stage.setMaximized(true);
        loginStage = stage;

        stage.show();
        ((LoginFXML)fxmlLoader.getController()).initialize(scene.getRoot());
        stage.requestFocus();
        stage.show();
    }

    public static void switchToCreate(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CREATE_FXML_PATH));
        try {loginStage.getScene().setRoot(fxmlLoader.load());}
        catch (IOException e) {e.printStackTrace(); return;}
        loginStage.getScene().getRoot().setVisible(false);

        ((CreateAccountFXML)fxmlLoader.getController()).initialize(loginStage.getScene().getRoot());
    }
    public static void switchToLogin() {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.LOGIN_FXML_PATH));
        try {loginStage.getScene().setRoot(fxmlLoader.load());}
        catch (IOException e) {e.printStackTrace(); return;}

        ((LoginFXML)fxmlLoader.getController()).initializeLessAnimation(loginStage.getScene().getRoot());
    }
    public static void switchToForgot() {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.FORGOT_FXML_PATH));
        try {loginStage.getScene().setRoot(fxmlLoader.load());}
        catch (IOException e) {e.printStackTrace(); return;}
        loginStage.getScene().getRoot().setVisible(false);

        ((ForgotPasswordFXML)fxmlLoader.getController()).initialize(loginStage.getScene().getRoot());
    }

    public static void launchMain(String themePath){
        loginStage.close(); //closing login stage
        if (mainStage != null) mainStage.close();

        mainStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MAIN_FXML_PATH));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), Utility.PREF_WIDTH, Utility.PREF_HEIGHT);
            scene.getStylesheets().add(Objects.requireNonNull(Launcher.class.getResource
                    (themePath)).toString());
            mainStage.setTitle(Utility.APP_TITLE);
            mainStage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getResource
                    (Utility.ICON_PATH)).toString()));
            mainStage.setScene(scene);
            mainStage.setMaximized(true);

            mainStage.show();
            ((MainFXML)fxmlLoader.getController()).initialize();
            mainStage.requestFocus();
            mainStage.show();
        } catch (IOException e) {
            AppManager.alert(Alert.AlertType.ERROR, "Exception occurred.",
                    e.getClass().toString(), "Exception");
            e.printStackTrace();}
    }







    public static void alert(Alert.AlertType alertType, String header, String msg, String title){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
