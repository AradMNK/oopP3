package graphics.app;

import Objects.SaveHandle;
import javafx.concurrent.Task;

public class Utility {
    public static final String
    APP_TITLE = "Instakilogram",

    LOGIN_FXML_PATH = "Login.fxml",
    FORGOT_FXML_PATH = "Forgot Password.fxml",
    CREATE_FXML_PATH = "Create Account.fxml",

    MAIN_FXML_PATH = "Main.fxml",
    NO_RESULTS_FXML_PATH = "No Results.fxml",
    MY_ACCOUNT_FXML_PATH = "My Account.fxml",
    LIKE_FXML_PATH = "Like.fxml",
    POST_FXML_PATH = "Post.fxml",
    POSTS_FXML_PATH = "Posts.fxml",
    POSTMAKER_FXML_PATH = "Postmaker.fxml",
    COMMENTS_FXML_PATH = "Comments.fxml",
    COMMENTMAKER_FXML_PATH = "Commentmaker.fxml",
    USER_FXML_PATH = "User.fxml",
    USERS_FXML_PATH = "Users.fxml",
    FOLLOWERS_FXML_PATH = "Followers.fxml",

    FEED_FXML_PATH = "Feed.fxml",
    EXPLORE_FXML_PATH = "Explore.fxml",
    STATS_FXML_PATH = "Stats.fxml",

    NEW_GROUP_FXML_PATH = "New Group.fxml",
    JOIN_GROUP_FXML_PATH = "Join Group.fxml",
    GROUP_STATS_FXML_PATH = "Group Stats.fxml",
    EDIT_GROUP_FXML_PATH = "Edit Group.fxml",
    USER_SEARCHER_FXML_PATH = "User Searcher.fxml",
    SEARCH_FXML_PATH = "Search.fxml",

    CHAT_FXML_PATH = "Chat.fxml",
    CHAT_PREVIEW_FXML_PATH = "Chat Preview.fxml",
    CHATS_FXML_PATH = "Chats.fxml",
    MESSAGE_FXML_PATH = "Message.fxml",
    REPLYING_FXML_PATH = "Replying.fxml",
    EDIT_FXML_PATH = "Edit.fxml",
    FORWARD_FXML_PATH = "Forward.fxml",

    LIGHT_MODE_CSS_PATH = "Light.css",
    DARK_MODE_CSS_PATH = "Dark.css",
    THEME_SAVER = "Theme saver.txt",
    THEME_SAVED_LIGHT = "light",
    THEME_SAVED_DARK = "dark",
    SCENE_FILL_DARK = "000000",
    SCENE_FILL_LIGHT = "ffffff",

    UNKNOWN_USER_PICTURE = "Default/Default User.jpg",
    GROUP_PICTURE_PATH = "Default/Default Group.jpg",
    ICON_PATH = "instakilogram.png",

    SAVED_MESSAGES = "Saved messages";

    public static final int PREF_WIDTH = 900, PREF_HEIGHT = 600,
    POST_TEXT_MARGIN_FROM_RIGHT = 60, IMAGE_POST_FIT_WIDTH = 500,
    MESSAGES_TO_LOAD = 10, MAX_PREVIEW_CHARS = 100,
    EDIT_PREF_WIDTH = 500, EDIT_PREF_HEIGHT = 150,
    FORWARD_PREF_WIDTH = 600, FORWARD_PREF_HEIGHT = 400;
    public static final int HIDE_WIDTH_VALUE = 300;
    public static final SaveHandle SAVE_HANDLE_0 = new SaveHandle(0);

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override protected Void call() {
                try {Thread.sleep(millis);} catch (InterruptedException ignored) {}
                return null;}};
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }
}
