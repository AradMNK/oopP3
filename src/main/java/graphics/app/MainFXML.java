package graphics.app;

import Login.LoginState;
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
import Objects.Feed;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainFXML {
    public static MainFXML root;
    @FXML GridPane rootDisplay;
    @FXML Button homeButton, myAccountButton, chatsButton, exploreButton, blocklistButton,
            postButton, searchButton, themeButton, followersButton, myPostsButton, logoutButton;
    @FXML TextField searchField;

    public void initialize(){
        root = this;
    }

    @FXML void search(){
        if (searchField.getText().equals("")){
            AppManager.alert(Alert.AlertType.WARNING,
                    "Empty search...", "Cannot search for empty string.", "Empty search");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.SEARCH_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();}
        ((SearchFXML)fxmlLoader.getController()).initialize(searchField.getText());
    }
    @FXML void home(){
        Feed feed = Loginner.loginnedUser.getFeed();
        if (feed.getLikes().size() == 0 && feed.getComments().size() == 0 && feed.getPosts().size() == 0){
            noResult("You have nothing new in your feed... for now.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.FEED_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();}
        ((FeedFXML)fxmlLoader.getController()).initialize(feed.getPosts(), feed.getComments(), feed.getLikes());
    }
    @FXML void myAccount(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MY_ACCOUNT_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();}
    }
    @FXML void chats(){
        if (!Database.Loader.doesUserHaveChat(Loginner.loginnedUser.getUsername())){
            noResult("You have no chats yet! Find someone and message them!");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CHATS_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((ChatsFXML)fxmlLoader.getController()).initialize(Loginner.loginnedUser.getChats(),
                Loginner.loginnedUser.getGroups());
    }
    @FXML void explore(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.EXPLORE_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace();}
    }
    @FXML void blocklist(){
        if (Loginner.loginnedUser.getBlocklist().size() == 0){
            noResult("You have no blocked users...!");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH));
        Parent root;
        try {root = fxmlLoader.load();}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((UsersFXML)fxmlLoader.getController()).initialize(Loginner.loginnedUser.getBlocklist());
        setDisplayTo(root);
    }
    @FXML void logout(){
        Loginner.loginnedUser = null;
        Loginner.loginState = LoginState.SIGN_OUT;

        AppManager.mainStage.close();
        try {AppManager.launchLogin(new Stage());
        } catch (IOException e) {e.printStackTrace();}
    }
    @FXML void post(){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POSTMAKER_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace();}
    }
    @FXML void myPosts(){
        if (Loginner.loginnedUser.getPosts().size() == 0){
            noResult("You have no posts yet! Use the new post button to post your first!");
            return;
        }
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.POSTS_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((PostsFXML)fxmlLoader.getController()).initialize(Loginner.loginnedUser.getPosts());
    }
    @FXML void theme(){
        if (Theme.currentTheme == Theme.LIGHT){
            Theme.currentTheme = Theme.DARK;
            AppManager.mainStage.getScene().getStylesheets().clear();
            AppManager.mainStage.getScene().getStylesheets().add(Objects.requireNonNull
                    (Launcher.class.getResource(Utility.DARK_MODE_CSS_PATH)).toString());
            Theme.currentTheme.saveTheme();
            return;
        }
        Theme.currentTheme = Theme.LIGHT;
        AppManager.mainStage.getScene().getStylesheets().clear();
        AppManager.mainStage.getScene().getStylesheets().add(Objects.requireNonNull
                (Launcher.class.getResource(Utility.LIGHT_MODE_CSS_PATH)).toString());
        Theme.currentTheme.saveTheme();
    }
    @FXML void followers(){
        FXMLLoader fxmlLoader_r = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH)),
                   fxmlLoader_l = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH)),
                   fxmlLoader_master = new FXMLLoader(Launcher.class.getResource(Utility.FOLLOWERS_FXML_PATH));
        Parent root_r, root_l, root;
        try {root = fxmlLoader_master.load();}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}

        if (Loginner.loginnedUser.getFollowers().size() == 0){
            root_r = noResultRoot("You have no followers yet...");}
        else {try {root_r = fxmlLoader_r.load();} catch (IOException e) {
                AppManager.alert(Alert.AlertType.ERROR,
                        "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
            ((UsersFXML)fxmlLoader_r.getController()).initialize(Loginner.loginnedUser.getFollowers());}

        if (Loginner.loginnedUser.getFollowings().size() == 0){
            root_l = noResultRoot("You do not follow anyone yet...");}
        else {try {root_l = fxmlLoader_l.load();} catch (IOException e) {
                AppManager.alert(Alert.AlertType.ERROR,
                        "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
            ((UsersFXML)fxmlLoader_l.getController()).initialize(Loginner.loginnedUser.getFollowings());}

        ((FollowersFXML)fxmlLoader_master.getController()).initialize(root_r, root_l);

        setDisplayTo(root);
    }

    @FXML void hoverSearchButton(){new Pulse(searchButton).play();}
    @FXML void hoverSearchField(){new Pulse(searchField).play();}
    @FXML void hoverHome(){new Pulse(homeButton).play();}
    @FXML void hoverMyAccount(){new Pulse(myAccountButton).play();}
    @FXML void hoverChats(){new Pulse(chatsButton).play();}
    @FXML void hoverExplore(){new Pulse(exploreButton).play();}
    @FXML void hoverBlocklist(){new Pulse(blocklistButton).play();}
    @FXML void hoverPost(){new Pulse(postButton).play();}
    @FXML void hoverTheme(){new Pulse(themeButton).play();}
    @FXML void hoverFollowers(){new Pulse(followersButton).play();}
    @FXML void hoverMyPosts(){new Pulse(myPostsButton).play();}
    @FXML void hoverLogout(){new Pulse(logoutButton).play();}

    void setDisplayTo(Parent root){
        rootDisplay.getChildren().clear();
        rootDisplay.getChildren().add(root);
    }
    void removeDisplay(){
        rootDisplay.getChildren().clear();
    }
    public void noResult(String msg) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.NO_RESULTS_FXML_PATH));
        try {setDisplayTo(fxmlLoader.load());
        } catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return;}
        ((NoResultsFXML)fxmlLoader.getController()).initialize(msg);
    }
    Parent noResultRoot(String msg){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.NO_RESULTS_FXML_PATH));
        Parent root;
        try {root = fxmlLoader.load();
        } catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getClass().toString(), "Exception"); e.printStackTrace(); return null;}
        ((NoResultsFXML)fxmlLoader.getController()).initialize(msg);
        return root;
    }
}
