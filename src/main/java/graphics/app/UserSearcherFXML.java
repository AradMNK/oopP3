package graphics.app;

import Database.Loader;
import animatefx.animation.Pulse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class UserSearcherFXML {
    @FXML TextField searchField;
    @FXML Button searchButton;
    @FXML GridPane rootDisplay;

    @FXML void search(){
        Loader.searchForUsers(searchField.getText());
    }

    @FXML void hoverSearchButton(){new Pulse(searchButton).play();}
    @FXML void hoverSearchField(){new Pulse(searchField).play();}
}
