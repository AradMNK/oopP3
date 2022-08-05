package Database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Changer {
    public static void removePostFromFeed(String username, int postID) {
        Connector.queryWithoutResult("DELETE FROM feed WHERE username = '" + username + "' AND ID = "
                                            + postID + " AND type = 'post';");
    }

    public static void removeCommentFromFeed(String username, int commentID) {
        Connector.queryWithoutResult("DELETE FROM feed WHERE username = '" + username
                                            + "' AND ID = " + commentID + " AND type = 'comment';");
    }

    public static void removeLikeFromFeed(String username, int handle) {
        Connector.queryWithoutResult("DELETE FROM feed WHERE username = '" + username
                                            + "' AND ID = " + handle + " AND type = 'like';");
    }

    public static void addViewForUser(int postID, String username) {
        Connector.queryWithoutResult("INSERT INTO views (username, postID) VALUES ('"
                                            + username + "', " + postID + ");");
    }
    public static void addLikeStat(int postID, String username) {
        Connector.queryWithoutResult("INSERT INTO likestat (username, postID) VALUES ('"
                                            + username + "', " + postID + ");");
    }

    public static void editMessage(int messageID, String line) {
        Connector.queryWithoutResult("UPDATE directmessage SET message = '" + line
                                            + "' WHERE messageID = " + messageID + ";");
    }

    public static void deleteMessage(int handle) {
        Connector.queryWithoutResult("DELETE FROM directmessage WHERE messageID = " + handle + ";");
    }

    public static void editGroupMessage(int messageID, String line) {
        Connector.queryWithoutResult("UPDATE groupmessage SET message = '" + line
                                            + "' WHERE messageID = " + messageID + ";");
    }

    public static void deleteGroupMessage(int handle) {
        Connector.queryWithoutResult("DELETE FROM groupmessage WHERE messageID = " + handle + ";");
    }

    public static void removeFromBlockList(String blocker, String blocked) {
        Connector.queryWithoutResult("DELETE FROM block WHERE blocker = '" + blocker
                                            +"' AND blocked = '" + blocked + "';");
    }

    public static void removeFromFollowers(String follower, String followed) {
        Connector.queryWithoutResult("DELETE FROM follow WHERE follower = '" + follower
                                            + "' AND followed = '" + followed + "';");
    }

    public static void removeLike(int postID, String username) {
        Connector.queryWithoutResult("DELETE FROM likes WHERE username = '"
                                            + username + "' AND postID = " + postID + ";");
    }

    public static void removeGroup(int groupID) {
        Connector.queryWithoutResult("DELETE FROM group_chats WHERE groupID = " + groupID + ";");
    }

    public static void changeGroupJoiner(int handle, String newID) {
        Connector.queryWithoutResult("UPDATE group_chats SET joinID = '" + newID +"' WHERE groupID = " + handle + ";");
    }

    public static void addUserToGroup(String username, int handle) {
        //declares group members
        String members;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT members FROM group_chats WHERE groupID = " + handle + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                members = resultSet.getString(1);

                //adds the member to the group
                members = (members + "," + username);
                Connector.queryWithoutResult("UPDATE group_chats SET members = '" + members
                                                    +"' WHERE groupID = " + handle + ";");
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
    }

    public static void removeParticipant(int handle, String username) {
        //declares a string for the members
        String members;

        //finds the members
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT members FROM group_chats WHERE groupID = " + handle + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                members = resultSet.getString(1);

                //removes the user
                members = members.replaceAll(username, "");

                //checks for ,
                if (members.charAt(0) == ','){
                    members = members.substring(1);
                }
                if (members.charAt(members.length()-1) == ','){
                    members = members.substring(members.length()-1);
                }
                members = members.replaceAll(",,", ",");

                //removes the member from group
                Connector.queryWithoutResult("UPDATE group_chats SET members = '" + members
                                                    + "' WHERE groupID = " + handle + ";");
                addToBanList(handle, username);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
    }

    public static void addToBanList (int handle, String username){
        //declares the ban list
        String banList;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT banList FROM group_chats WHERE groupID = "
                                                        + handle + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                banList = resultSet.getString(1);

                //adds the member to the ban list
                banList = (banList + "," + username);
                Connector.queryWithoutResult("UPDATE group_chats SET banList = '" + banList
                        +"' WHERE groupID = " + handle + ";");
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
    }

    public static void removeFromBanList(int handle, String username) {
        //declares a string for the ban list
        String banned;

        //finds the members
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT banList FROM group_chats WHERE groupID = " + handle + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                banned = resultSet.getString(1);

                //removes the user
                banned = banned.replaceAll(username, "");

                //checks for ,
                if (banned.charAt(0) == ','){
                    banned = banned.substring(1);
                }
                if (banned.charAt(banned.length()-1) == ','){
                    banned = banned.substring(banned.length()-1);
                }
                banned = banned.replaceAll(",,", ",");

                //removes the member from group
                Connector.queryWithoutResult("UPDATE group_chats SET banList = '" + banned
                                                    + "' WHERE groupID = " + handle + ";");
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
    }

    public static void changePassword(String username, String hash) {
        Connector.queryWithoutResult("UPDATE users SET hashPass = '" + hash + "' WHERE username = '" + username + "';");
    }

    public static void userSees(String username, int groupID) {
        Connector.queryWithoutResult("DELETE FROM unreadgroups WHERE forUsername = '"
                                            + username + "' AND groupID = " + groupID +";");
    }

    public static void userSees(String forUsername, String usernameSender) {
        Connector.queryWithoutResult("DELETE FROM unreadusers WHERE forUsername = '"
                                            + forUsername + "' AND username = '" + usernameSender +"';");
    }

    public static void setUserPfp(String username, String directory) {
        Connector.queryWithoutResult
                ("UPDATE users SET pfp = '" + directory + "' WHERE username = '" + username + "';");
    }

    public static void setUserName(String username, String value) {
        Connector.queryWithoutResult
                ("UPDATE users SET name = '" + value + "' WHERE username = '" + username + "';");
    }

    public static void setUserBio(String username, String value) {
        Connector.queryWithoutResult
                ("UPDATE users SET bio = '" + value + "' WHERE username = '" + username + "';");
    }

    public static void setUserSubtitle(String username, String value) {
        Connector.queryWithoutResult
                ("UPDATE users SET subtitle = '" + value + "' WHERE username = '" + username + "';");
    }


}
