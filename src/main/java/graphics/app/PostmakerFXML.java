package graphics.app;

import Login.Loginner;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.controlsfx.control.Notifications;

import java.net.MalformedURLException;

public class PostmakerFXML {
    MainFXML root;
    String picturePath = "";

    @FXML TextArea inputMessage;
    @FXML Button attachButton, cancelButton, postButton;
    @FXML ImageView picture;

    @FXML void attach(){
        FileChooser fileChooser = new FileChooser();
        try {
            picturePath = fileChooser.showOpenDialog(AppManager.mainStage).toURI().toURL().toString();
            picture = new ImageView(picturePath);
        } catch (MalformedURLException e) {e.printStackTrace();}
    }
    @FXML void cancel(){
        root.removeDisplay();
    }
    @FXML void post(){
        if (inputMessage.getText().equals("") && picturePath.equals("")){
            AppManager.alert(Alert.AlertType.ERROR, "Empty post!", "Posts need to have at least a picture or caption,",
                    "Empty post...");
            return;
        }

        if (picturePath.equals("")) Loginner.loginnedUser.post(inputMessage.getText());
        else Loginner.loginnedUser.post(inputMessage.getText(), picturePath);

        Notifications notification = Notifications.create();
        notification.title("Success!");
        notification.text("Successfully posted your post!");
        notification.showInformation();
        cancel();
    }
}
