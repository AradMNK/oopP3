package graphics.app;

import Login.Loginner;
import animatefx.animation.Pulse;
import graphics.theme.Theme;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;

public class MainFXML {
    public static MainFXML root;
    @FXML GridPane rootDisplay;
    @FXML Button homeButton, myAccountButton, chatsButton, feedButton, exploreButton,
            blocklistButton, postButton, searchButton, themeButton, followersButton, myPostsButton;
    @FXML TextField searchField;

    public void initialize(){
        root = this;}


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
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH));
        Parent root;
        try {root = fxmlLoader.load();}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((UsersFXML)fxmlLoader.getController()).initialize(Loginner.loginnedUser.getBlocklist());
        setDisplayTo(root);
    }
    @FXML void post(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POSTMAKER_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace();}
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
    @FXML void followers(){
        FXMLLoader fxmlLoader_r = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH)),
                   fxmlLoader_l = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH)),
                   fxmlLoader_master = new FXMLLoader(Launcher.class.getResource(Utility.FOLLOWERS_FXML_PATH));
        Parent root_r, root_l, root;
        try {root = fxmlLoader_master.load(); root_r = fxmlLoader_r.load(); root_l = fxmlLoader_l.load();}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}

        ((UsersFXML)fxmlLoader_r.getController()).initialize(Loginner.loginnedUser.getFollowers());
        ((UsersFXML)fxmlLoader_l.getController()).initialize(Loginner.loginnedUser.getFollowings());
        ((FollowersFXML)fxmlLoader_master.getController()).initialize
                (root_r, root_l);

        setDisplayTo(root);
    }
    @FXML void myPosts(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POSTS_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((PostsFXML)fxmlLoader.getController()).initialize(Loginner.loginnedUser.getPosts());
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
    @FXML void hoverFollowers(){new Pulse(followersButton).play();}
    @FXML void hoverMyPosts(){new Pulse(myPostsButton).play();}

    void setDisplayTo(Parent root){
        rootDisplay.getChildren().clear();
        rootDisplay.getChildren().add(root);
    }
    void removeDisplay(){
        rootDisplay.getChildren().clear();
    }
}
