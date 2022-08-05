package graphics.app;

import Builder.UserBuilder;
import Objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Set;

public class UsersFXML {
    @FXML VBox display;

    private void addUser(User user){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(user);
    }

    public void initialize(Set<String> users){
        for (String user: users) addUser(UserBuilder.getUserFromDatabase(user));
    }

    public void initialize(User[] users) {
        for (User user: users) addUser(user);
    }
}
