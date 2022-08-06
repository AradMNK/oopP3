package graphics.app;

import Login.Loginner;
import Objects.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.controlsfx.control.Notifications;

public class CommentmakerFXML {
    Post post;
    @FXML TextArea inputMessage;
    @FXML Button cancelButton, postButton;

    @FXML void cancel(){
        MainFXML.root.removeDisplay();
    }
    @FXML void post(){
        if (inputMessage.getText().equals("")){
            AppManager.alert(Alert.AlertType.ERROR, "Empty comment!", "Comments need to have a message.",
                    "Empty comment...");
            return;
        }


        post.getComments().add
                (Loginner.loginnedUser.comment(post.getPostID().getHandle(), inputMessage.getText()));

        Notifications notification = Notifications.create();
        notification.title("Success!");
        notification.text("Successfully commented your opinion that totally matters!");
        notification.showInformation();
        cancel();
    }
}
