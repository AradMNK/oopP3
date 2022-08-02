package Builder;

import Objects.Message;
import Objects.SaveHandle;

import java.time.LocalDateTime;

public class MessageBuilder {
    public static Message getMessageFromDatabase(int messageID) {
        Message message = new Message();

        String[] details = Database.Loader.getMessageDetails(messageID);

        message.setID(new SaveHandle(messageID));
        int i = 0;
        message.setUsername(details[i++]);
        message.setUserName(details[i++]);
        message.setReplyToID(new SaveHandle(Integer.parseInt(details[i++])));
        message.setOriginalUsername(details[i++]);
        message.setContent(details[i++]);
        message.setDate(LocalDateTime.parse(details[i++])); //FIXME sequence

        return message;
    }
}
