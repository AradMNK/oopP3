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

import java.io.IOException;

public class UserSearcherFXML {
    @FXML TextField searchField;
    @FXML Button searchButton;
    @FXML GridPane rootDisplay;

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
        ((UsersFXML)fxmlLoader.getController()).initialize(matches);
        rootDisplay.getChildren().add(root);
    }

    @FXML void hoverSearchButton(){new Pulse(searchButton).play();}
    @FXML void hoverSearchField(){new Pulse(searchField).play();}
}
