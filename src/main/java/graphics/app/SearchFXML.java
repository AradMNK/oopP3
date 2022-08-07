package graphics.app;

import Builder.GroupMessageBuilder;
import Builder.MessageBuilder;
import Builder.UserBuilder;
import Database.Loader;
import Login.Loginner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import Objects.GroupMessage;
import Objects.Message;

import java.io.IOException;
import java.util.*;

public class SearchFXML {
    @FXML
    VBox displayUsers, displayDirects, displayGroups;

    public void initialize(String pattern){
        String[] usernames = Loader.searchForUsers(pattern);
        if (usernames.length == 0){
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(Utility.NO_RESULTS_FXML_PATH));
            try {
                displayUsers.getChildren().add(loader.load());}
            catch (IOException e) {e.printStackTrace();}
            ((NoResultsFXML)loader.getController()).initialize("No new posts from your followers yet...");
        }
        else{
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource(Utility.USERS_FXML_PATH));
            try {
                displayUsers.getChildren().add(loader.load());}
            catch (IOException e) {e.printStackTrace();}
            ((UsersFXML)loader.getController()).initialize(usernames);
        }

        int[] directs = Loader.searchDirects(Loginner.loginnedUser.getUsername(), pattern);
        Message[] messages = new Message[directs.length];
        int i = 0;
        for (int direct : directs) messages[i++] = MessageBuilder.getMessageFromDatabase(direct);

        List<Message> directsList = Arrays.stream(messages).toList();
        ArrayList<Message> sortedDirects = new ArrayList<>(directsList);
        sortedDirects.sort(Comparator.comparing(Message::getDate));
        for (Message message : sortedDirects) addMessageDirect(message);

        int[] groups = Loader.searchGroups(Loginner.loginnedUser.getUsername(), pattern);
        GroupMessage[] groupMessages = new GroupMessage[groups.length];
        i = 0;
        for (int group_message: groups) groupMessages[i++] = GroupMessageBuilder.getGroupMessageFromDatabase(group_message);

        List<GroupMessage> groupMessageList = Arrays.stream(groupMessages).toList();
        ArrayList<GroupMessage> sortedGroups = new ArrayList<>(groupMessageList);
        sortedGroups.sort(Comparator.comparing(GroupMessage::getDate));
        for (GroupMessage message : sortedGroups) addMessageGroup(message);
    }

    private void addMessageGroup(GroupMessage message) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
        try {
            displayDirects.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((MessageFXML)fxmlLoader.getController()).initialize(message, UserBuilder.getUserFromDatabase(message.getUsername()));
    }

    private void addMessageDirect(Message message) {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource(Utility.MESSAGE_FXML_PATH));
        try {
            displayDirects.getChildren().add(fxmlLoader.load());} catch (IOException e) {AppManager.alert(Alert.AlertType.ERROR,
                "Exception occurred.", e.getCause().getMessage(), "Exception"); e.printStackTrace(); return;}
        ((MessageFXML)fxmlLoader.getController()).initialize(message, UserBuilder.getUserFromDatabase(message.getUsername()));
    }


}
