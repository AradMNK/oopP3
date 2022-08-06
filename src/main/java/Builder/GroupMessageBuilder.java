package Builder;

import Objects.GroupMessage;
import Objects.SaveHandle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupMessageBuilder {

    public static GroupMessage getGroupMessageFromDatabase(int groupMessageID) {
        GroupMessage message = new GroupMessage();

        String[] details = Database.Loader.getGroupMessageDetails(groupMessageID);

        message.setID(new SaveHandle(groupMessageID));
        int i = 0; //sender, message, date, replyMessageID, originalSender, originalID
        message.setUsername(details[i++]);
        message.setContent(details[i++]);
        message.setDate(LocalDateTime.parse(details[i].substring(0, details[i].length() - 2),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        i++;
        message.setReplyToID(new SaveHandle(Integer.parseInt(details[i++])));
        message.setOriginalUsername(details[i++]);
        message.setOriginalMessage(new SaveHandle(Integer.parseInt(details[i])));
        return message;
    }
}
