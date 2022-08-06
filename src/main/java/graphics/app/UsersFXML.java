package graphics.app;

import Builder.UserBuilder;
import Objects.Group;
import Objects.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashSet;
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

    public void initialize(String[] users){
        for (String user: users) addUser(UserBuilder.getUserFromDatabase(user));
    }

    public void initializeGroupForAdd(String[] users, Group group) {
        for (String user: users) addUserForAddGroup(UserBuilder.getUserFromDatabase(user), group);
    }
    public void initializeGroupForUnban(String[] users, Group group) {
        for (String user: users) addUserForUnbanGroup(UserBuilder.getUserFromDatabase(user), group);
    }

    public void initializeOwnerMode(HashSet<User> users, Group group){
        for (User user: users) addUserOwnerMode(user, group);
    }

    private void addUserOwnerMode(User user, Group group) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(user);
        ((UserFXML)fxmlLoader.getController()).initializeGroupOwnerMode(group);
    }

    public void initialize(HashSet<User> users) {
        for (User user: users) addUser(user);
    }

    private void addUserForAddGroup(User user, Group group){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(user);
        ((UserFXML)fxmlLoader.getController()).initializeGroupBehaviorAdd(group);
    }

    private void addUserForUnbanGroup(User user, Group group){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USER_FXML_PATH));
        try {display.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((UserFXML)fxmlLoader.getController()).initialize(user);
        ((UserFXML)fxmlLoader.getController()).initializeGroupBehaviorUnban(group);
    }
}
