package graphics.app;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class FollowersFXML {
    @FXML VBox displayR, displayL;

    public void initialize(Parent l, Parent r){
        displayL.getChildren().add(l);
        displayR.getChildren().add(r);
    }
}
