package TextController;

import Builder.GroupBuilder;
import Builder.PostBuilder;
import Login.Creator;
import Login.LoginState;
import Login.Loginner;
import Login.SecurityQuestion;
import Objects.Group;
import Objects.Post;
import Objects.SaveHandle;
import Objects.UserType;
import Objects.User;
import Recommender.AdRecommender;
import Recommender.UserRecommender;
import java.util.Scanner;

public class TextController {
    public static final Scanner scanner = new Scanner(System.in);

    public static String getLine(){
        String line = scanner.nextLine();
        if (line.equals("")) return getLine();
        return line;
    }
    public static String getNext(){
        String next = scanner.next();
        if (next.equals("")) return getNext();
        return next;
    }

    private static void actOnCommand(Command command){
        switch (command.getCommandType()){
            case LOGIN -> Loginner.attemptLogin(command.getArgs());
            case FORGOT -> Loginner.forgotPassword(command.getArgs()[0]);
            case CREATE_ACC -> Creator.attemptCreate(command.getArgs());
            case EDIT_ACC -> UserEditor.edit();
            case SIGNOUT -> Loginner.signout();
            case RELOAD -> Loginner.reload();

            case DETAILS -> Display.accountDetails(command.getArgs());

            case POST -> PostController.newPost();
            case COMMENT -> CommentController.newComment(command.getArgs()[0]);
            case LIKE -> like(command.getArgs()[0]);
            case UNLIKE -> unlike(command.getArgs()[0]);

            case LIKES -> likes(command.getArgs()[0]);
            case COMMENTS -> comments(command.getArgs()[0]);
            case LIKERS -> likers(command.getArgs()[0]);
            case POSTS -> Display.accountPosts(command.getArgs());

            case FEED -> FeedController.show();
            case UNREAD -> Display.unread();
            case FOLLOW -> follow(command.getArgs()[0]);
            case UNFOLLOW -> unfollow(command.getArgs()[0]);
            case FOLLOWERS -> Display.followers(command.getArgs());
            case FOLLOWINGS -> Display.followings(command.getArgs());
            case STATS -> StatsController.show(command.getArgs());

            case JOIN -> attemptJoin(command.getArgs()[0]);
            case DM -> DmController.attemptEntrance(command.getArgs()[0]);
            case BLOCK -> block(command.getArgs()[0]);
            case UNBLOCK -> unblock(command.getArgs()[0]);
            case SEARCH -> SearchController.search();

            case GROUPS -> GroupController.showGroups();
            case NEW_GROUP -> newGroup();
            case RECOMMEND_USER -> recommendUser();
            case RECOMMEND_AD -> recommendAd();

            case HELP -> writeHelp();
            case EXIT -> println("Paradoxical");
            case NONE -> println("You have typed in /" + CommandType.NONE + "! This command does nothing you idiot!");

            default -> println("No match for command /" + command.getCommandType());
        }
    }

    private static void attemptJoin(String joiner) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please sign in before trying to join a group.");
            return;
        }

        if (!Database.Loader.groupJoinerExists(joiner)){
            TextController.println("Group joiner \"" + joiner + "\" does not exist.");
            return;
        }

        int id = Database.Loader.getGroupID(joiner);

        if (Loginner.loginnedUser.getGroups().stream().noneMatch(g-> g.getGroupID().getHandle() == id)){
            TextController.println("You have already joined this group!");
            return;
        }

        //checks if user is banned
        if (Database.Loader.isUserBanned(id, Loginner.loginnedUser.getUsername())) {
            TextController.println("You cannot join this group because you have been banned.");
            return;
        }
        Loginner.loginnedUser.getGroups().add(GroupBuilder.getGroupFromDatabase(id));
        Database.Changer.addUserToGroup(Loginner.loginnedUser.getUsername(), id);
        TextController.println("Joined successfully.");
    }

    private static void writeHelp() {
        println("/" + CommandType.HELP + " (username) (password)");
        println("Brings up this menu.");
        println("/" + CommandType.LOGIN + " (username) (password)");
        println("Use this to login.");
        println("/" + CommandType.FORGOT + " (username)");
        println("Use this to login using your security question and change your password.");
        println("/" + CommandType.SIGNOUT);
        println("Use this to sign out.");
        println("/" + CommandType.CREATE_ACC + " (username) (password) (name)");
        println("Use this to create an account.");
        println("/" + CommandType.EDIT_ACC);
        println("Use this to edit your account. You will have to write its tags like bio=i am sad and in the next line sub=hello and/or name=my name there, for example.");
        println("/" + CommandType.RELOAD);
        println("Reloads you as the user with the same credentials if something isn't loading properly.");
        println("/" + CommandType.FEED);
        println("Use to enter feed mode and receive the latest notifications.");
        println("/" + CommandType.UNREAD);
        println("Use to see the chats where you have unread messages.");
        println("/" + CommandType.DETAILS + " (otherUsername)");
        println("Use with argument for another user or without argument for your own account to see details.");
        println("/" + CommandType.POSTS + " (otherUsername)");
        println("Use with argument for another user or without argument for your own account to see users' posts.");
        println("/" + CommandType.LIKES + " (postID)");
        println("Use with to see a post's likes.");
        println("/" + CommandType.LIKERS + " (postID)");
        println("Use with to see a post's likers.");
        println("/" + CommandType.COMMENTS + " (postID)");
        println("Use with to see a post's comments.");
        println("/" + CommandType.POST);
        println("Use to enter posting mode");
        println("/" + CommandType.COMMENT);
        println("Use to enter commenting mode");
        println("/" + CommandType.LIKE + " (postID)");
        println("Use to like a post.");
        println("/" + CommandType.UNLIKE + " (postID)");
        println("Use to unlike a post.");
        println("/" + CommandType.FOLLOW + " (username)");
        println("Use to follow a user.");
        println("/" + CommandType.UNFOLLOW + " (username)");
        println("Use to unfollow a user.");
        println("/" + CommandType.FOLLOWERS + " (username)");
        println("Use to see your followers, or with an argument to see followers of another user.");
        println("/" + CommandType.FOLLOWINGS);
        println("Use to see the people you follow, or with an argument to see followings of another user.");
        println("/" + CommandType.STATS + " (postID)");
        println("Use with argument to see statistics of your post or use without for overall statistics (both are business-user only).");
        println("/" + CommandType.DM + " (username)");
        println("Use this to directly message another user and enter chat mode." +
                "\nCommands there include:" + DmCommand.REPLY + " (number of messages starting from 0 at the bottom),\n"
                + DmCommand.EDIT + " (number of messages starting from 0 at the bottom), \n"
                + DmCommand.FORWARD + " (number of messages starting from 0 at the bottom) (@username OR groupID),\n"
                + DmCommand.DELETE + " (number of messages starting from 0 at the bottom),\n"
                + DmCommand.REFRESH + ", " + DmCommand.BLOCK + ", " + DmCommand.UNBLOCK + ", " + DmCommand.LEAVE +
                "\n" + "-".repeat(15));
        println("/" + CommandType.GROUPS);
        println("Use this to see your groups and type in the groupID to enter group chat mode after using this command." +
                "\nCommands there include:" + GroupCommand.REPLY + " (number of messages starting from 0 at the bottom),\n"
                + GroupCommand.EDIT + " (number of messages starting from 0 at the bottom), \n"
                + GroupCommand.FORWARD + " (number of messages starting from 0 at the bottom) (@username OR groupID),\n"
                + GroupCommand.DELETE + " (number of messages starting from 0 at the bottom),\n"
                + GroupCommand.REFRESH + ", " + GroupCommand.REVOKE + ", " + GroupCommand.ADD + " (username), " + GroupCommand.LEAVE +
                "\n" + GroupCommand.BAN + " (username [admin/owner only]), " + GroupCommand.UNBAN + " (username [admin/owner only]), "
                + GroupCommand.JOINER + ", " + GroupCommand.EXIT + "\n" + "-".repeat(15));
        println("/" + CommandType.JOIN + " (groupJoiner)");
        println("Use this command to join a group via invite link.");
        println("/" + CommandType.NEW_GROUP);
        println("Use this command to create a new group where you are the owner/admin and enter in the details.");
        println("/" + CommandType.RECOMMEND_USER);
        println("Recommends a user to you.");
        println("/" + CommandType.RECOMMEND_AD);
        println("Recommends an ad (business type post).");
        println("/" + CommandType.EXIT);
        println("Exits the program.");
    }

    private static void recommendUser() {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login before trying to get recommended users for you.");
            return;
        }

        User[] recommendedUsers = UserRecommender.recommendUser();

        println("The top " + recommendedUsers.length + " usernames for you are (in order):");
        for (User recommendedUser: recommendedUsers) {
            println("[@" + recommendedUser.getUsername() + "]");
        }
    }
    private static void recommendAd() {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login before trying to get recommended ads for you.");
            return;
        }

        Integer[] recommendedAds = AdRecommender.recommendAd();

        println("The top " + recommendedAds.length + " posts for you are (in order):");
        for (int postID: recommendedAds) {
            Post post = PostBuilder.getPostFromDatabase(postID);
            TextController.print("** AD **");
            TextController.println("(" + post.getDatePosted() + ") "
                    + post.getPoster().getName() + "[@" + post.getPoster().getUsername() + "]:" );
            TextController.println(post.getDescription());
            TextController.println("postID: \"" + post.getPostID() + "\"");
        }
    }

    private static void newGroup() {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login before trying to create a group chat.");
            return;
        }

        Group group = new Group();
        TextController.println("Please enter a name for your group chat:");
        group.setName(TextController.getLine());
        TextController.println("Please enter a joining ID for inviting people.");
        group.setGroupJoiner(TextController.getLine());
        if (Database.Loader.groupJoinerExists(group.getGroupJoiner())){
            TextController.println("Joining ID already exists. Please choose a new one. Aborting command /"
                    + CommandType.NEW_GROUP);
            return;
        }
        group.setGroupID(new SaveHandle(Database.Saver.createGroup
                (Loginner.loginnedUser.getUsername(), group.getName(), group.getGroupJoiner())));
        group.getParticipants().add(Loginner.loginnedUser);
        group.setOwner(Loginner.loginnedUser);

        GroupController.attemptEntrance(group);
    }

    private static void unblock(String username) {
        if (!Loginner.loginnedUser.getBlocklist().contains(username)){
            TextController.println("The user [@" + username + "] was not in your blocklist.");
            return;
        }

        Loginner.loginnedUser.unblock(username);
    }
    private static void block(String username) {
        if (Loginner.loginnedUser.getBlocklist().contains(username)){
            TextController.println("The user [@" + username + "] was already in your blocklist.");
            return;
        }

        if (!Database.Loader.usernameExists(username)){
            TextController.println("The user [@" + username + "] does not exist.");
            return;
        }

        Loginner.loginnedUser.block(username);
    }

    private static void likers(String postIDasString) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            println("Please login first to like a post.");
            return;
        }

        int postID;
        try {postID = Integer.parseInt(postIDasString);} catch (NumberFormatException e){e.printStackTrace(); return;}

        if (!Database.Loader.postIdExists(postID)) {
            println("PostID \"" + postID + "\" does not exist.");
            return;
        }

        Display.writeUsers(Database.Loader.getLikerUsernames(postID));
    }
    private static void likes(String postIDasString) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            println("Please login first to see likes.");
            return;
        }
        int postID;
        try {postID = Integer.parseInt(postIDasString);} catch (NumberFormatException e){e.printStackTrace(); return;}

        if (!Database.Loader.postIdExists(postID)) {
            println("PostID \"" + postID + "\" does not exist.");
            return;
        }

        println("Total likes on your post [" + postID + "] is: " + Database.Loader.getNumberOfLikes(postID));
    }
    private static void comments(String postIDasString) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            println("Please login first to see likes.");
            return;
        }
        int postID;
        try {postID = Integer.parseInt(postIDasString);} catch (NumberFormatException e){e.printStackTrace(); return;}

        if (!Database.Loader.postIdExists(postID)) {
            println("PostID \"" + postID + "\" does not exist.");
            return;
        }

        Display.writeComments(postID);
    }

    private static void like(String postIDasString) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            println("Please login first to like a post.");
            return;
        }

        int postID;
        try {postID = Integer.parseInt(postIDasString);} catch (NumberFormatException e){e.printStackTrace(); return;}

        if (!Database.Loader.postIdExists(postID)) {
            println("PostID \"" + postID + "\" does not exist.");
            return;
        }

        if (!Loginner.loginnedUser.like(postID)){
            println("You have already liked this post.");
        }
        else {
            println("You liked this post.");
        }
    }
    private static void unlike(String postIDasString) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            println("Please login first to like a post.");
            return;
        }

        int postID;
        try {postID = Integer.parseInt(postIDasString);} catch (NumberFormatException e){e.printStackTrace(); return;}

        if (!Database.Loader.postIdExists(postID)) {
            println("PostID \"" + postID + "\" does not exist.");
            return;
        }

        if (!Loginner.loginnedUser.unlike(postID)) println("You did not like this post in the first place.");
        else {
            println("You unliked this post.");
        }
    }

    private static void follow(String username) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            println("Please login first before trying to follow anyone.");
            return;
        }

        if (username.equals(Loginner.loginnedUser.getUsername())){
            println("You cannot follow yourself... get a life dude.");
            return;
        }

        if (!Database.Loader.usernameExists(username)){
            println("Username \"@" + username + "\" does not exist.");
            return;
        }

        if (Loginner.loginnedUser.follow(username)) println("Followed @" + username);
        else println("You already follow [@" + username + "].");
    }
    private static void unfollow(String username) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            println("Please login first before trying to unfollow anyone.");
            return;
        }

        if (!Database.Loader.usernameExists(username)){
            println("Username \"@" + username + "\" does not exist.");
            return;
        }

        if (Loginner.loginnedUser.unfollow(username)) println("Unfollowed @" + username);
        else println("You don't follow [@" + username + "] anyway.");
    }

    public static void inputCommand(){
        println("Successfully launched terminal mode.");

        Command command;
        println("Enter a new command:");
        String line = getLine();
        try{command = new Command(line);}
        catch (CommandException e){
            TextController.println("What you just typed in was not defined." +
                "\nUse /" + CommandType.HELP + " to see the available commands.");
            inputCommand();
            return;
        }

        while (!command.getCommandType().equals(CommandType.EXIT)){
            actOnCommand(command);
            println("Enter a new command:");
            try {line = getLine(); if (!line.equals("")) command = new Command(line);}
            catch (CommandException e){
                TextController.println("What you just typed in was not defined." +
                        "\nUse /" + CommandType.HELP + " to see the available commands.");
            } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e){
                TextController.println("Argument problem. Please try again.");}
        }
    }

    public static UserType getUserType(){
        println("Please enter 1 for normal user and 2 for business user:");
        int type = scanner.nextInt();
        if (type == 1) return UserType.NORMAL;
        else if (type == 2) return UserType.BUSINESS;
        else return getUserType();
    }

    public static int getSecurityQuestionNumber() {
        println("Choose a question by picking the number.");
        SecurityQuestion.write();
        int type = scanner.nextInt();
        if (type <= SecurityQuestion.numOfQuestions() && type > 0)
            return type;
        else return getSecurityQuestionNumber();
    }

    public static void println(String message){System.out.println(message);}
    public static void print(String message){System.out.print(message);}
}
