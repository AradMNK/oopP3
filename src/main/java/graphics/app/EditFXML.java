package graphics.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class EditFXML {
    @FXML TextArea msg;
    @FXML Button cancelButton, confirmButton;

    void initialize(String msg){this.msg.setText(msg);}
    @FXML void cancel(){ChatFXML.chatFXML.cancelEdit();}
    @FXML void confirm(){ChatFXML.chatFXML.confirmEdit(msg.getText());}
}
