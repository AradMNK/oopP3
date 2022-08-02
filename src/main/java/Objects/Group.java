package Objects;

import java.util.ArrayList;
import java.util.HashSet;

public class Group {
    private SaveHandle groupID;
    private User owner;
    private String name, groupJoiner;

    private final HashSet<User> participants = new HashSet<>();
    private final ArrayList<GroupMessage> shownMessages = new ArrayList<>();

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public SaveHandle getGroupID() {return groupID;}
    public void setGroupID(SaveHandle groupID) {this.groupID = groupID;}

    public User getOwner() {return owner;}
    public void setOwner(User owner) {this.owner = owner;}

    public String getGroupJoiner() {return groupJoiner;}
    public void setGroupJoiner(String groupJoiner) {this.groupJoiner = groupJoiner;}

    public HashSet<User> getParticipants() {return participants;}
    public ArrayList<GroupMessage> getShownMessages() {return shownMessages;}
}
