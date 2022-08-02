package Builder;

import Objects.GroupMessage;
import Objects.SaveHandle;

import java.time.LocalDateTime;

public class GroupMessageBuilder {

    public static GroupMessage getGroupMessageFromDatabase(int groupMessageID) {
        GroupMessage message = new GroupMessage();

        String[] details = Database.Loader.getGroupMessageDetails(groupMessageID);

        message.setID(new SaveHandle(groupMessageID));
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
