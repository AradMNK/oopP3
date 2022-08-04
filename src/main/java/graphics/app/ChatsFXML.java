package graphics.app;

import Builder.DirectMessengerBuilder;
import Login.Loginner;
import Objects.DirectMessenger;
import Objects.Group;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ChatsFXML {
    @FXML VBox displayGroups, displayDirects;

    private void addChat(DirectMessenger directMessenger){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CHAT_PREVIEW_FXML_PATH));
        try {displayDirects.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((ChatPreviewFXML)fxmlLoader.getController()).initialize
                (directMessenger.getRecipient(), directMessenger.getShownMessages().get(0));
    }

    private void addGroup(Group group){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CHAT_PREVIEW_FXML_PATH));
        try {displayGroups.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((ChatPreviewFXML)fxmlLoader.getController()).initialize(group, group.getShownMessages().get(0));
    }

    public void initialize(Set<String> chats, Set<Group> groups){
        DirectMessenger[] directs = new DirectMessenger[chats.size()];
        int i = 0;
        for (String username: chats) {
            directs[i++] = DirectMessengerBuilder.getDirectMessengerFromDatabase
                    (Loginner.loginnedUser, username, Utility.MESSAGES_TO_LOAD);
        }
        List<DirectMessenger> directsList = Arrays.asList(directs);
        ArrayList<DirectMessenger> sortedDirects = new ArrayList<>(directsList);
        LocalDateTime[] dateTimes = new LocalDateTime[directs.length];
        i = 0;
        for (DirectMessenger direct: directs) {
            dateTimes[i++] = direct.getShownMessages().get(0).getDate();
        }
        sortedDirects.sort(Comparator.comparing(dm -> dateTimes[directsList.indexOf(dm)]));

        for (DirectMessenger direct : sortedDirects) addChat(direct);

        List<Group> groupsList = groups.stream().toList();
        ArrayList<Group> sortedGroups = new ArrayList<>(groupsList);
        LocalDateTime[] dateTimesGroups = new LocalDateTime[groups.size()];
        i = 0;
        for (Group group: groupsList) {dateTimesGroups[i++] = group.getShownMessages().get(0).getDate();}
        sortedGroups.sort(Comparator.comparing(group -> dateTimesGroups[groupsList.indexOf(group)]));
        for (Group group : sortedGroups) addGroup(group);
    }
}
