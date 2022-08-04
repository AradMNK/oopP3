package graphics.app;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class NoResultsFXML {
    @FXML Text text;

    public void initialize(String msg){text.setText(msg);}
}
