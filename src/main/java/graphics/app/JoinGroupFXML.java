package graphics.app;

import Builder.GroupBuilder;
import Database.Loader;
import Login.Loginner;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class JoinGroupFXML {
    ChatsFXML root;

    void initialize(ChatsFXML chatsFXML){root = chatsFXML;}

    @FXML TextField link;
    @FXML Button confirmButton, cancelButton;

    @FXML void confirm(){
        if (!Loader.groupJoinerExists(link.getText())){
            AppManager.alert(Alert.AlertType.ERROR, "This group does not exist!", "Please try another link.",
                    "Group does not exist");
            return;
        }

        int id = Database.Loader.getGroupID(link.getText());

        if (Loginner.loginnedUser.getGroups().stream().anyMatch(g-> g.getGroupID().getHandle() == id)){
            AppManager.alert(Alert.AlertType.ERROR, "You are already in this group!", "What are you doing?",
                    "Already in group");
            return;
        }
        if (Database.Loader.isUserBanned(id, Loginner.loginnedUser.getUsername())) {
            AppManager.alert(Alert.AlertType.ERROR, "You have been banned from this group!",
                    "Ask the owner to unban you.", "Banned");
            return;
        }

        Loginner.loginnedUser.getGroups().add(GroupBuilder.getGroupFromDatabase(id));
        Database.Changer.addUserToGroup(Loginner.loginnedUser.getUsername(), id);
    }
    @FXML void cancel(){root.popup.close();}
}
