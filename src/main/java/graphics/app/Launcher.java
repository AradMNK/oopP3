package graphics.app;

import Database.Connector;
import TextController.TextController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppManager.launchLogin(stage);
    }

    public static void main(String[] args) {
        if (connectAndSelectMode()) launch();
        else Database.Changer.truncate(LocalDateTime.now());
    }

    public static boolean connectAndSelectMode() {
        tryToConnect();
        return chooseMode();
    }

    private static boolean chooseMode() {
        TextController.println("Enter 1 for graphical mode and 2 for terminal mode:");
        int choice;
        try {choice = TextController.scanner.nextInt();} catch (NumberFormatException e){
            TextController.println("Please enter a number.");
            return chooseMode();
        }
        if (choice == 1) return true;
        else if (choice == 2) return false;
        TextController.println("Please enter 1 or 2.");
        return chooseMode();
    }

    private static void tryToConnect(){
        TextController.println("Enter your database password: System.Recreate1");
        Connector.setPass(TextController.getLine());
        if (!Connector.checkConnection()){
            TextController.println("Unable to make connection to database.");
            tryToConnect();
            return;
        }
        TextController.println("Successfully connected to database.");
    }
}