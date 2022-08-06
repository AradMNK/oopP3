package graphics.app;

import Database.Loader;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import Objects.Group;

import java.io.IOException;

public class UserSearcherFXML {
    Group group;
    boolean isAdding;
    @FXML TextField searchField;
    @FXML Button searchButton;
    @FXML GridPane rootDisplay;

    void initializeAdd(Group group){
        isAdding = true;
        this.group = group;
    }

    void initializeUnban(Group group){
        isAdding = false;
        this.group = group;
    }

    @FXML void search(){
        String[] matches = Loader.searchForUsers(searchField.getText());
        rootDisplay.getChildren().clear();
        if (matches.length == 0){
            rootDisplay.getChildren().add(MainFXML.root.noResultRoot("No results were found..."));
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH));
        Parent root;
        try {root = fxmlLoader.load();}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        if (isAdding) ((UsersFXML)fxmlLoader.getController()).initializeGroupForAdd(matches, group);
        else ((UsersFXML)fxmlLoader.getController()).initializeGroupForUnban(matches, group);
        rootDisplay.getChildren().add(root);
    }

    @FXML void hoverSearchButton(){new Pulse(searchButton).play();}
    @FXML void hoverSearchField(){new Pulse(searchField).play();}
}
