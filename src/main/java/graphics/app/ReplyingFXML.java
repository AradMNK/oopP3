package graphics.app;

import Objects.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ReplyingFXML {
    @FXML GridPane masterPane;
    @FXML Text name, msg;
    @FXML Rectangle hbar;
    @FXML Button closeButton;

    public void initialize(Message message) {
        name.setText(message.getUsername());
        msg.setText(message.getContent());
        hbar.widthProperty().bind(masterPane.widthProperty());
        hbar.heightProperty().bind(masterPane.heightProperty());
    }

    @FXML void close(){
        ChatFXML.chatFXML.removeReply();
    }
}
