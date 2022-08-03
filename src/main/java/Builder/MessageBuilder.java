package Builder;

import Objects.Message;
import Objects.SaveHandle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageBuilder {
    public static Message getMessageFromDatabase(int messageID) {
        Message message = new Message();

        String[] details = Database.Loader.getMessageDetails(messageID);

        message.setID(new SaveHandle(messageID));
        int i = 0; //sender, message, date, replyMessageID, originalSender
        message.setUsername(details[i++]);
        message.setContent(details[i++]);
        message.setDate(LocalDateTime.parse(details[i++].substring(0, details[3].length() - 2),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        message.setReplyToID(new SaveHandle(Integer.parseInt(details[i++])));
        message.setOriginalUsername(details[i]);


        return message;
    }
}
