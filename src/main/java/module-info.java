module graphics {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires AnimateFX;

    opens graphics.app to javafx.fxml;
    exports graphics.app;
    exports Objects;
}