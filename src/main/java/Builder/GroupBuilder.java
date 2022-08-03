package Builder;

import Objects.Group;
import Objects.SaveHandle;

public class GroupBuilder {
    public static Group getGroupFromDatabase(int groupID){
        return getGroupFromDatabaseWithName(groupID);
    }

    public static Group getGroupFromDatabaseWithName(int groupID){
        Group group = new Group();
        group.setGroupID(new SaveHandle(groupID));
        group.setName(Database.Loader.getGroupName(groupID));
        return group;
    }

    public static Group getGroupFromDatabaseFull(int groupID, int howMany){
        Group group = getGroupFromDatabase(groupID);
        group.setGroupJoiner(Database.Loader.getGroupJoiner(groupID));
        group.setOwner(UserBuilder.getUserFromDatabase(Database.Loader.getGroupOwner(groupID)));
        int[] groupMessageIDs = Database.Loader.getGroupMessageIDsOfGroup(groupID, howMany);
        for (int groupMessageID: groupMessageIDs) group.
                getShownMessages().add(GroupMessageBuilder.getGroupMessageFromDatabase(groupMessageID));
        return group;
    }
}
