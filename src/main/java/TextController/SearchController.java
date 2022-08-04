package TextController;

import Database.Loader;
import Login.LoginState;
import Login.Loginner;
import Objects.*;

public class SearchController {
    public static void search () {
        //checks if the user has logged in
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login first to see account details.");
            return;
        }

        //declares the user
        User user = Loginner.loginnedUser;

        searchMenu(user);
    }

    public static void searchMenu (User user){
        boolean hasDirect = Loader.doesUserHaveChat(user.getUsername()),
                hasGroups = Loader.doesUserHaveGroups(user.getUsername());

        TextController.println("Please choose:");
        TextController.println("1. Search for a message in a single direct");
        TextController.println("2. Search for a message in a single group");
        TextController.println("3. Search for a message in a directs");
        TextController.println("4. Search for a message in a groups");
        TextController.println("5. Search among users");

        int choice;

        try{choice = Integer.parseInt(TextController.getNext());}
        catch (Exception e){TextController.println("Please enter a number."); searchMenu(user); return;}

        if (choice == 1){
            if (hasDirect) searchInDirect(user);
            else TextController.println("Theres no direct to search a message in it.");
        }
        else if (choice == 2){
            if (hasGroups) searchInGroup(user);
            else TextController.println("Theres no group to search a message in it.");
        }
        else if (choice == 3){
            if (hasDirect) searchDirect(user);
            else TextController.println("No direct found.");
        }
        else if (choice == 4){
            if (hasGroups) searchGroup(user);
            else TextController.println("no group found.");
        }
        else if (choice == 5) {
            searchUsers();
        }
        else TextController.println("Please enter a valid number.");
    }
    public static void searchInDirect (User user){
        //gets the username and pattern
        TextController.println("Please enter a username.");
        String targetUser = TextController.getNext();

        //checks if the user has chats
        if (!Loader.usersHaveDm(user.getUsername(), targetUser)){
            TextController.println("No messages.");
            return;
        }

        //gets the pattern
        TextController.println("Please enter a pattern.");
        String pattern = TextController.getNext();

        //getsTheMessageIDs
        int[] messageIDs = Loader.searchInDirect(user.getUsername(), targetUser, pattern);

        //displays the messages
        for (int messageID : messageIDs) {
            //prints the message
            TextController.println(Loader.getMessageDetails(messageID)[0] + ": "
                    + Loader.getMessageDetails(messageID)[1]);
            //prints the date
            TextController.println(Loader.getMessageDetails(messageID)[2]);
        }
    }

    public static void searchInGroup (User user){
        //prints the groups of the user
        TextController.println("Please choose a group.");

        int [] groupIDs = Loader.getGroupsOfUser(user.getUsername());
        String [] groupJoiners = new String [groupIDs.length];

        for (int i = 0; i < groupIDs.length; i++){
            groupJoiners[i] = Loader.getGroupJoiner(groupIDs[i]);
            TextController.println(i + 1 + ". @" + groupJoiners[i]);
        }

        //gets the group
        int choice;

        try{choice = Integer.parseInt(TextController.getNext());}
        catch (Exception e){TextController.println("Please enter a number."); searchMenu(user); return;}

        if (choice < 1 || choice > groupJoiners.length){
            TextController.println("Please enter a valid number.");
            searchInGroup(user);
            return;
        }

        //gets the pattern
        TextController.println("Please enter a pattern.");
        String pattern = TextController.getNext();

        //getsTheMessageIDs
        int[] messageIDs = Loader.searchInGroup(user.getUsername(), groupIDs[choice - 1], pattern);

        //displays the messages
        for (int messageID : messageIDs) {
            //prints the message
            TextController.println(Loader.getGroupMessageDetails(messageID)[1]
                    + ": " + Loader.getGroupMessageDetails(messageID)[2]);
            //prints the date
            TextController.println(Loader.getGroupMessageDetails(messageID)[3]);
        }
    }

    public static void searchDirect (User user){
        //gets the pattern
        TextController.println("Please enter a pattern.");
        String pattern = TextController.getNext();

        //getsTheMessageIDs
        int[] messageIDs = Loader.searchDirects(user.getUsername(), pattern);

        //displays the messages
        for (int messageID : messageIDs) {
            //prints the message
            TextController.println(Loader.getMessageDetails(messageID)[0] + ": "
                    + Loader.getMessageDetails(messageID)[1]);
            //prints the date
            TextController.println(Loader.getMessageDetails(messageID)[2]);
        }
    }

    public static void searchGroup (User user){
        //gets the pattern
        TextController.println("Please enter a pattern.");
        String pattern = TextController.getNext();

        //getsTheMessageIDs
        int[] messageIDs = Loader.searchGroups(user.getUsername(), pattern);

        //declares a groupID
        int groupID;

        //displays the messages
        for (int messageID : messageIDs) {
            groupID = Integer.parseInt(Loader.getGroupMessageDetails(messageID)[0]);
            //prints the joiner
            TextController.println("@" + Loader.getGroupJoiner(groupID));
            //prints the message
            TextController.println(Loader.getGroupMessageDetails(messageID)[1] + ": "
                    + Loader.getGroupMessageDetails(messageID)[2]);
            //prints the date
            TextController.println(Loader.getGroupMessageDetails(messageID)[3]);
        }
    }

    public static void searchUsers (){
        //gets the pattern
        TextController.println("Please enter a pattern.");
        String pattern = TextController.getNext();

        //gets the users
        String [] users = Loader.searchForUsers(pattern);

        //prints the users
        for (String user : users) {
            //prints the username
            TextController.println("@" + user);
            //prints the name
            TextController.println(Loader.getUserName(user));
        }
    }
}
