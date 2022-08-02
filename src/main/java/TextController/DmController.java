package TextController;

import Builder.DirectMessengerBuilder;
import Builder.GroupBuilder;
import Login.LoginState;
import Login.Loginner;
import Objects.Group;
import Objects.Message;
import Objects.DirectMessenger;

import java.time.LocalDateTime;

public class DmController {
    final static int replyShowNum = 10, notReplyID = 0, showMessagesIncrement = 10, showMessagesInit = 10;
    static int showMessages = showMessagesInit;
    final static String inReplyTo = "In reply to: ", ellipsis = "...";
    public static DirectMessenger dm;
    public static boolean uBlocked, uBlocker;

    public static void attemptEntrance(String username) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login before trying to message anyone.");
            return;
        }
        if (!Database.Loader.usernameExists(username)){
           TextController.println("Could not find match for username [@" + username + "]!");
           return;
        }


        dm = DirectMessengerBuilder.getDirectMessengerFromDatabase(Loginner.loginnedUser, username, showMessages);
        //if users have dm load else create

        showMessages = showMessagesInit;
        showPreviousChats();

        uBlocked = Database.Loader.isUserBlocked(dm.getUser().getUsername(), dm.getRecipient().getUsername());
        uBlocker = Loginner.loginnedUser.getBlocklist().contains(dm.getRecipient().getUsername());
        blockMessage();

        enterChatMode();

        dm = null;
    }

    private static void showPreviousChats() {
        Database.Changer.userSees(dm.getUser().getUsername(), dm.getRecipient().getUsername());
        for (Message message : dm.getShownMessages()) {
            if (message.getReplyToID().getHandle() != 0) { //it's a replied message
                String repliedMessage = Database.Loader.getDirectMessageContent(message.getReplyToID().getHandle());
                TextController.print("[" + getInReplyTo(repliedMessage) + "] ");
                TextController.println(getMessage(message));
            }
            else if (message.getOriginalUsername().equals(dm.getUser().getUsername())){ //it's normal message
                TextController.println(getMessage(message));
            } else { //it's a forwarded message
                TextController.print("[Forwarded from @" + message.getOriginalUsername() + "] ");
                TextController.println(getMessage(message));
            }
        }
    }

    private static String getMessage(Message message) {
        return ("(" + message.getDate() + ") " + message.getUserName() + " :" + message.getContent());
    }

    private static void enterChatMode() {
        String line;
        TextController.println("You can leave with +" + DmCommand.LEAVE + ".");
        while (true){
            line = TextController.getLine();
            if (actOnCommand(line)) break;
            else {
                if (uBlocked || uBlocker) {blockMessage(); continue;}
                Database.Saver.addToMessages(dm.getUser().getUsername(), dm.getRecipient().getUsername(),
                        dm.getUser().getUsername(), LocalDateTime.now(), line, notReplyID);
            }
        }
    }

    private static boolean actOnCommand(String line) {
        String[] split = line.split("\\s*");
        DmCommand dmCommand = DmCommand.toDmCommand(split[0]);
        try {
            switch (dmCommand) {
                case EDIT -> {try {edit(Integer.parseInt(split[1]));} catch (NumberFormatException e) {e.printStackTrace();}}
                case DELETE -> {try {delete(Integer.parseInt(split[1]));} catch (NumberFormatException e) {e.printStackTrace();}}
                case REPLY -> {try {reply(Integer.parseInt(split[1]));} catch (NumberFormatException e) {e.printStackTrace();}}
                case FORWARD -> {try {forward(Integer.parseInt(split[1]), split[2]);} catch (NumberFormatException e) {e.printStackTrace();}}
                case BLOCK -> block();
                case UNBLOCK -> unblock();

                case REFRESH -> refresh();
                case MORE -> more();

                case LEAVE -> {return true;}
                default -> {}
            }
        }
        catch (ArrayIndexOutOfBoundsException e){
            if (line.startsWith("\\")) TextController.println("You need to provide an argument for " + line);}
        return false;
    }

    private static void more() {
        showMessages += showMessagesIncrement;
        refresh();
    }

    private static void unblock() {
        if (!Loginner.loginnedUser.unblock(dm.getRecipient().getUsername())) {
            TextController.println("The user [@" + dm.getRecipient().getUsername() + "] was not in your blocklist.");
            uBlocker = false;
        }
    }
    private static void block(){
        if (!Loginner.loginnedUser.block(dm.getRecipient().getUsername())){
            TextController.println("The user [@" + dm.getRecipient().getUsername() + "] was already in your blocklist.");
            uBlocker = true;
        }
    }

    private static void forward(int num, String where) {
        final int size = dm.getShownMessages().size();
        if (num > size){
            TextController.println("The number entered exceeds the total messages. (" + size + ")");
            return;
        }

        if (where.startsWith("@"))
            forwardToUser(where.substring(1), dm.getShownMessages().get(num));
        else
            try {forwardToGroup(Integer.parseInt(where), dm.getShownMessages().get(num));}
            catch (NumberFormatException e){TextController.println
                ("Entered argument did not start with @ to send to a user and was also not a number to indicate groupID.");}
    }
    private static void forwardToGroup(int groupID, Message message) {
        if (!Database.Loader.groupExists(groupID)){
            TextController.println("No match for group ID \"" + groupID + "\"");
            return;
        }

        Group group = GroupBuilder.getGroupFromDatabase(groupID);
        if (!Loginner.loginnedUser.getGroups().contains(group)){
            TextController.println("You are not part of this group!");
            return;
        }

        Database.Saver.addToGroupMessages(groupID, Loginner.loginnedUser.getUsername(), message.getOriginalUsername(),
                LocalDateTime.now(), message.getContent(), notReplyID);
        TextController.println("Message forwarded to \"" + group.getName() + "\"");
    }
    private static void forwardToUser(String username, Message message) {
        if (!Database.Loader.usernameExists(username)){
            TextController.println("The username [@" + username + "] does not exist.");
            return;
        }
        if (Database.Loader.isUserBlocked(username, Loginner.loginnedUser.getUsername())){
            TextController.println("You can't forward a message to someone who has blocked you.");
            return;
        } else if (Database.Loader.isUserBlocked(Loginner.loginnedUser.getUsername(), username)){
            TextController.println("You can't forward a message to someone whom you have blocked.");
            return;
        }

        Database.Saver.addToMessages(Loginner.loginnedUser.getUsername(), username,
                message.getOriginalUsername(), LocalDateTime.now(),
                message.getContent(),notReplyID);
    }

    private static void reply(int num) {
        if (uBlocked || uBlocker) {blockMessage(); return;}

        final int size = dm.getShownMessages().size();
        if (num > size){
            TextController.println("The number entered exceeds the total messages. (" + size + ")");
            return;
        }

        TextController.println("[" + getInReplyTo(num) + "]");
        Database.Saver.addToMessages(dm.getUser().getUsername(), dm.getRecipient().getUsername(),
                dm.getUser().getUsername(), LocalDateTime.now(), TextController.getLine(),
                dm.getShownMessages().get(num).getID().getHandle());
    }
    private static void edit(int num) {
        final int size = dm.getShownMessages().size();
        if (num > size){
            TextController.println("The number entered exceeds the total messages. (" + size + ")");
            return;
        }
        if (!dm.getShownMessages().get(num).getUsername().equals(dm.getUser().getUsername())){
            TextController.println("You cannot edit another person's message!");
            return;
        }

        Message message = dm.getShownMessages().get(num);
        Database.Changer.editMessage(message.getID().getHandle(), TextController.getLine());
        TextController.println("SYSTEM: Successfully edited your message.");
    }
    private static void delete(int num) {
        final int size = dm.getShownMessages().size();
        if (num > size){
            TextController.println("The number entered exceeds the total messages. (" + size + ")");
            return;
        }
        if (!dm.getShownMessages().get(num).getUsername().equals(dm.getUser().getUsername())){
            TextController.println("You cannot edit another person's message!");
            return;
        }

        Message message = dm.getShownMessages().get(num);
        Database.Changer.deleteMessage(message.getID().getHandle());
        TextController.println("SYSTEM: Successfully deleted your message. Reloading chat: ");
        refresh();
    }
    private static void refresh() {
        dm = DirectMessengerBuilder.getDirectMessengerFromDatabase(dm.getUser(), dm.getRecipient(),  showMessages);
        showPreviousChats();

        uBlocked = Database.Loader.isUserBlocked(dm.getUser().getUsername(), dm.getRecipient().getUsername());
        uBlocker = Loginner.loginnedUser.getBlocklist().contains(dm.getRecipient().getUsername());
        blockMessage();
    }

    private static String getInReplyTo(int num){
        Message message = dm.getShownMessages().get(num);
        String out = (message.getContent().length() > replyShowNum + ellipsis.length()) ?
                message.getContent().substring(0, replyShowNum) + ellipsis : message.getContent();
        return inReplyTo + "[" + out + "]";
    }
    private static String getInReplyTo(String msg){
        String out = (msg.length() > replyShowNum + ellipsis.length()) ?
                msg.substring(0, replyShowNum) + ellipsis : msg;
        return inReplyTo + "[" + out + "]";
    }

    private static void blockMessage(){
        if (uBlocked && uBlocker){
            TextController.println("You have blocked each other. Be the first to break the ice by unblocking using \\unblock!");
        } else if (uBlocked) {
            TextController.println("You have been blocked by the other user and cannot send messages to them anymore.");
        } else if (uBlocker){
            TextController.println("You have blocked the user. Break the ice by unblocking using \\unblock!");}
    }
}

enum DmCommand{
    REPLY("\\reply"),
    EDIT("\\edit"),
    REFRESH("\\ref"),
    FORWARD("\\forward"),
    DELETE("\\del"),
    MORE("\\more"),
    LEAVE("/leave"),

    BLOCK("\\block"),
    UNBLOCK("\\unblock"),

    NONE("");

    private final String name;

    @Override
    public String toString(){return name;}

    DmCommand(String name){this.name = name;}

    static DmCommand toDmCommand(String string){
        for (DmCommand commandType: DmCommand.values())
            if (commandType.name.equals(string)) return commandType;
        return DmCommand.NONE;
    }
}
