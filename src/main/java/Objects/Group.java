package Objects;

import java.util.HashSet;
import java.util.LinkedList;

public class Group {
    private SaveHandle groupID;
    private User owner;
    private String name, groupJoiner;
    private Handle pfp;

    private final HashSet<User> participants = new HashSet<>();
    private final LinkedList<GroupMessage> shownMessages = new LinkedList<>();

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public SaveHandle getGroupID() {return groupID;}
    public void setGroupID(SaveHandle groupID) {this.groupID = groupID;}

    public Handle getPfp() {return pfp;}
    public void setPfp(Handle pfp) {this.pfp = pfp;}

    public User getOwner() {return owner;}
    public void setOwner(User owner) {this.owner = owner;}

    public String getGroupJoiner() {return groupJoiner;}
    public void setGroupJoiner(String groupJoiner) {this.groupJoiner = groupJoiner;}

    public HashSet<User> getParticipants() {return participants;}
    public LinkedList<GroupMessage> getShownMessages() {return shownMessages;}
}
