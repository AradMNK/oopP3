package graphics.app;

import Builder.DirectMessengerBuilder;
import Login.Loginner;
import Objects.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ChatsFXML {
    Stage popup;
    @FXML Button newGroupButton, joinGroupButton;
    @FXML VBox displayGroups, displayDirects;

    private void addChat(DirectMessenger directMessenger){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CHAT_PREVIEW_FXML_PATH));
        try {displayDirects.getChildren().add(0, fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((ChatPreviewFXML)fxmlLoader.getController()).initialize
                (directMessenger.getRecipient(), directMessenger.getShownMessages().get(0), directMessenger);
    }

    private void addGroup(Group group){
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.CHAT_PREVIEW_FXML_PATH));
        try {displayGroups.getChildren().add(0, fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((ChatPreviewFXML)fxmlLoader.getController()).initialize(group, group.getShownMessages().get(0));
    }

    public void initialize(Set<String> chats, Set<Group> groups){
        if (chats.size() == 0)
            displayDirects.getChildren().add(MainFXML.root.noResultRoot("You have no direct messages yet..."));
        else initDirects(chats);
        if (groups.size() == 0)
            displayGroups.getChildren().add(MainFXML.root.noResultRoot("You have no groups yet... But you can make one!"));
        else initGroups(groups);
    }

    private void initGroups(Set<Group> groups) {
        List<Group> groupsList = groups.stream().toList();
        ArrayList<Group> sortedGroups = new ArrayList<>(groupsList);
        LocalDateTime[] dateTimesGroups = new LocalDateTime[groups.size()];
        int i = 0;
        for (Group group: groupsList) {dateTimesGroups[i++] = group.getShownMessages().get(0).getDate();}
        sortedGroups.sort(Comparator.comparing(group -> dateTimesGroups[groupsList.indexOf(group)]));
        for (Group group : sortedGroups) addGroup(group);
    }

    private void initDirects(Set<String> chats) {
        DirectMessenger[] directs = new DirectMessenger[chats.size()];
        int i = 0;
        for (String username: chats)
            directs[i++] = DirectMessengerBuilder.getDirectMessengerFromDatabase
                    (Loginner.loginnedUser, username, Utility.MESSAGES_TO_LOAD);
        List<DirectMessenger> directsList = Arrays.asList(directs);
        ArrayList<DirectMessenger> sortedDirects = new ArrayList<>(directsList);
        LocalDateTime[] dateTimes = new LocalDateTime[directs.length];
        i = 0;
        for (DirectMessenger direct: directs)
            dateTimes[i++] = direct.getShownMessages().get(0).getDate();
        sortedDirects.sort(Comparator.comparing(dm -> dateTimes[directsList.indexOf(dm)]));
        for (DirectMessenger direct : sortedDirects) addChat(direct);
    }

    @FXML void newGroup(){
        popup = new Stage(StageStyle.UTILITY);
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.NEW_GROUP_FXML_PATH));
        try {popup.setScene(new Scene(fxmlLoader.load()));}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                    "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((NewGroupFXML)fxmlLoader.getController()).initialize(this);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
    }
    public void createGroup(String name, String joiner, String picturePath) {
        Group group = new Group();
        group.setGroupID(new SaveHandle(Database.Saver.createGroup(Loginner.loginnedUser.getUsername(),
                name, joiner)));
        group.setGroupJoiner(joiner);
        group.setOwner(Loginner.loginnedUser);
        group.setName(name);
        group.setPfp(new Handle(picturePath));
        Loginner.loginnedUser.getGroups().add(group);
        addGroup(group);
        popup.close();
    }

    @FXML void joinGroup(){
        popup = new Stage(StageStyle.UTILITY);
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.JOIN_GROUP_FXML_PATH));
        try {popup.setScene(new Scene(fxmlLoader.load()));}
        catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((JoinGroupFXML)fxmlLoader.getController()).initialize(this);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.showAndWait();
    }
}
