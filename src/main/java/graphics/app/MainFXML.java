package graphics.app;

import animatefx.animation.Pulse;
import graphics.theme.Theme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

public class MainFXML {
    @FXML Pane rootDisplay;
    @FXML Button homeButton, myAccountButton, chatsButton, feedButton, exploreButton,
            blocklistButton, postButton, searchButton, themeButton;
    @FXML TextField searchField;

    public void initialize(){
    }

    @FXML void search(){

    }
    @FXML void home(){

    }
    @FXML void myAccount(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MY_ACCOUNT_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace();}
    }
    @FXML void chats(){

    }
    @FXML void feed(){

    }
    @FXML void explore(){

    }
    @FXML void blocklist(){

    }
    @FXML void post(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POSTMAKER_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace();}
        ((PostmakerFXML)fxmlLoader.getController()).root = this;
    }
    @FXML void theme(){
        if (Theme.currentTheme == Theme.LIGHT){
            Theme.currentTheme = Theme.DARK;
            AppManager.mainStage.getScene().getStylesheets().clear();
            AppManager.mainStage.getScene().getStylesheets().add(Objects.requireNonNull
                    (Launcher.class.getResource(Utility.DARK_MODE_CSS_PATH)).toString());
            return;
        }
        Theme.currentTheme = Theme.LIGHT;
        AppManager.mainStage.getScene().getStylesheets().clear();
        AppManager.mainStage.getScene().getStylesheets().add(Objects.requireNonNull
                (Launcher.class.getResource(Utility.LIGHT_MODE_CSS_PATH)).toString());
    }

    @FXML void hoverSearchButton(){new Pulse(searchButton).play();}
    @FXML void hoverSearchField(){new Pulse(searchField).play();}
    @FXML void hoverHome(){new Pulse(homeButton).play();}
    @FXML void hoverMyAccount(){new Pulse(myAccountButton).play();}
    @FXML void hoverChats(){new Pulse(chatsButton).play();}
    @FXML void hoverFeed(){new Pulse(feedButton).play();}
    @FXML void hoverExplore(){new Pulse(exploreButton).play();}
    @FXML void hoverBlocklist(){new Pulse(blocklistButton).play();}
    @FXML void hoverPost(){new Pulse(postButton).play();}
    @FXML void hoverTheme(){new Pulse(themeButton).play();}

    void setDisplayTo(Parent root){
        rootDisplay.getChildren().clear();
        rootDisplay.getChildren().add(root);
    }
    void removeDisplay(){
        rootDisplay.getChildren().clear();
    }
}
