package Builder;

import Objects.*;

public class DirectMessengerBuilder {
    public static DirectMessenger getDirectMessengerFromDatabase(User user, String recipientUsername, int howMany){
        DirectMessenger directMessenger = new DirectMessenger(user, UserBuilder.getUserFromDatabase(recipientUsername));
        fillDirectMessengerWithMessages(directMessenger, howMany);
        return directMessenger;
    }

    public static DirectMessenger getDirectMessengerFromDatabase(User user, User recipient, int howMany){
        DirectMessenger directMessenger = new DirectMessenger(user, recipient);
        fillDirectMessengerWithMessages(directMessenger, howMany);
        return directMessenger;
    }

    private static void fillDirectMessengerWithMessages(DirectMessenger dm, int howMany){
        int[] messageIDs = Database.Loader.getMessageIDsOfUsers
                (dm.getUser().getUsername(), dm.getRecipient().getUsername(), howMany);
        for (int messageID: messageIDs) dm.
                getShownMessages().addLast(MessageBuilder.getMessageFromDatabase(messageID));
    }
}
