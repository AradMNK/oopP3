package graphics.app;

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
    POST_FXML_PATH = "Post.fxml",
    POSTS_FXML_PATH = "Posts.fxml",
    POSTMAKER_FXML_PATH = "Postmaker.fxml",
    COMMENTS_FXML_PATH = "Comments.fxml",
    COMMENTMAKER_FXML_PATH = "Commentmaker.fxml",
    USER_FXML_PATH = "User.fxml",
    USERS_FXML_PATH = "Users.fxml",
    FOLLOWERS_FXML_PATH = "Followers.fxml",

    CHAT_PREVIEW_FXML_PATH = "Chat Preview",

    LIGHT_MODE_CSS_PATH = "Light.css",
    DARK_MODE_CSS_PATH = "Dark.css",

    UNKNOWN_USER_PICTURE = "unknown user.jpg",
    ICON_PATH = "instakilogram.png";

    public static final int PREF_WIDTH = 900, PREF_HEIGHT = 600,
    POST_TEXT_MARGIN_FROM_RIGHT = 60, IMAGE_POST_FIT_WIDTH = 500;

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<>() {
            @Override protected Void call() {
                try {Thread.sleep(millis);} catch (InterruptedException ignored) {}
                return null;}};
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }
}
