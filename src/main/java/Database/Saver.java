package Database;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Saver {
    public static void saveLogin(String username, String hashPass, String name, LocalDate dateJoined, String userType,
                                 int securityQuestionNum, String securityQuestionAnswer){

        Connector.queryWithoutResult
                ("INSERT INTO users (username, hashPass, name, date, questionID, answer, type) VALUES ('"
                        + username +"', '" + hashPass + "', '" + name + "', '" + dateJoined + "', "
                        + securityQuestionNum + ", '" + securityQuestionAnswer + "', '" + userType + "');");
    }

    public static int addToPosts(String username, LocalDateTime now, String description, String postType) {
        //declares the postID
        int postID = 0;

        //formats date and time
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatObj);

        //adds the post to the posts
        Connector.queryWithoutResult
                ("INSERT INTO posts (username, date, description, type) VALUES ('"
                        + username +"', '" + formattedDate + "', '" + description + "', '" + postType + "');");

        //gets the handle
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement
                    ("SELECT postID FROM posts ORDER BY postID DESC;").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                postID = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return postID;
    }

    public static int addToComments(String username, LocalDateTime now, int postID, String msg) {
        //declares the commentID
        int commentID = 0;

        //formats date and time
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatObj);

        //adds the comment to the comments
        Connector.queryWithoutResult
                ("INSERT INTO comments (postID, username, comment, date) VALUES ("
                        + postID + ", '" + username + "', '" + msg + "', '" + formattedDate + "');");

        //gets the handle
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT commentID FROM comments ORDER BY commentID DESC;").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                commentID = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return commentID;
    }

    public static void addToFollowers(String usernameFollower, String usernameFollowed) {
        Connector.queryWithoutResult
                ("INSERT INTO follow (follower, followed) VALUES ('" + usernameFollower + "', '"
                        + usernameFollowed + "');");
    }

    public static int addToLikes(int postID, String username) {
        Connector.queryWithoutResult
                ("INSERT INTO likes (postID, username) VALUES (" + postID + ", '" + username + "');");

        //declares the likeID
        int likeID = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            //adds to new messages
            resultSet = connection.prepareStatement("SELECT likeID FROM likes"
                                                        + " ORDER BY likeID DESC LIMIT 1;").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                likeID = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return likeID;
    }

    public static void updateFeedsFromLike(String username, int ID) {
        Connector.queryWithoutResult
                ("INSERT INTO feed (username, ID, type) VALUES ('" + username + "', " + ID + ", 'like');");
    }


    public static void updateFeedsFromPost(String username, int ID) {
        Connector.queryWithoutResult
                ("INSERT INTO feed (username, ID, type) VALUES ('" + username + "', " + ID + ", 'post');");
    }

    public static void updateFeedsFromComment(String username, int ID) {
        Connector.queryWithoutResult
                ("INSERT INTO feed (username, ID, type) VALUES ('" + username + "', " + ID + ", 'comment');");
    }

    public static int addToMessages(String sender, String receiver, String originalSender, int originalID,
                                     LocalDateTime now, String line, int replyMsgID) {
        //adds to direct
        if (!Loader.usersHaveDm(sender, receiver)){
            Connector.queryWithoutResult("INSERT INTO directs (user1, user2) VALUES ('" + sender
                    + "', '" + receiver + "');");
        }

        //formats date and time
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatObj);

        Connector.queryWithoutResult("INSERT INTO directmessage (sender, receiver, message, date"
                + ", replyMessageID, originalSender) VALUES ('" + sender + "', '"
                + receiver + "', '" + line + "', '" + formattedDate + "', "
                + replyMsgID + ", '" + originalSender + "');");

        //declares the messageID
        int messageID = 0;

        //declares new message count
        int newMessages;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            //returns the messageID
            resultSet = connection.prepareStatement("SELECT messageID FROM directmessage"
                    + " ORDER BY messageID DESC LIMIT 1;").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                messageID = resultSet.getInt(1);
            }

            //saves the originalID
            if (originalID == 0){
                Connector.queryWithoutResult("UPDATE directmessage SET originalID = " + messageID
                                                    + " WHERE messageID = " + messageID + ";");
            }
            else {
                Connector.queryWithoutResult("UPDATE directmessage SET originalID = " + originalID
                                                    + " WHERE messageID = " + messageID + ";");
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}

        connection = Connector.connector.connect();
        try {
            //adds to new messages
            resultSet = connection.prepareStatement("SELECT count FROM unreadusers WHERE forUsername = '"
                    + receiver + "' AND username = '" + sender + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                newMessages = resultSet.getInt(1);
                newMessages++;
                Connector.queryWithoutResult("UPDATE unreadusers SET count = " + newMessages
                        + " WHERE forUsername = '" + receiver + "' AND username = '" + sender + "';");
            } else {
                Connector.queryWithoutResult("INSERT INTO unreadusers (forUsername, username, count) VALUES ('"
                        + receiver + "', '" + sender + "', 1);");
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageID;
    }

    public static void addToBlocklist(String blocker, String blocked) {
        Connector.queryWithoutResult("INSERT INTO block (blocker, blocked) VALUES ('"
                                            + blocker + "', '" + blocked + "');");
    }

    public static int createGroup(String ownerUsername, String name, String joiner) {
        //declares the groupID
        int groupID = 0;
        Connector.queryWithoutResult("INSERT INTO group_chats (name, admin, members, joinID) VALUES ('"
                                            + name + "', '" + ownerUsername + "', '" + ownerUsername + "', '"
                                            + joiner + "');");

        //gets the handle
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT groupID FROM group_chats ORDER BY groupID DESC;").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                groupID = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return groupID;
    }

    public static int addToGroupMessages(int handle, String sender, String originalSender, int originalID,
                                          LocalDateTime now, String content, int notReplyID) {
        //formats date and time
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatObj);

        Connector.queryWithoutResult("INSERT INTO groupmessage (groupID, sender, message, date"
                + ", replyMessageID, originalSender) VALUES ('" + handle + "', '"
                + sender + "', '" + content + "', '" + formattedDate + "', "
                + notReplyID + ", '" + originalSender + "');");

        //declares the messageID
        int messageID = 0;

        //declares the group members
        String membersList;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            //returns the messageID
            resultSet = connection.prepareStatement("SELECT messageID FROM groupmessage"
                    + " ORDER BY messageID DESC LIMIT 1;").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                messageID = resultSet.getInt(1);
            }

            //saves the originalID
            if (originalID == 0){
                Connector.queryWithoutResult("UPDATE groupmessage SET originalID = " + messageID
                        + " WHERE messageID = " + messageID + ";");
            }
            else {
                Connector.queryWithoutResult("UPDATE groupmessage SET originalID = " + originalID
                        + " WHERE messageID = " + messageID + ";");
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}

        connection = Connector.connector.connect();
        try {
            resultSet = connection.prepareStatement("SELECT members FROM group_chats WHERE groupID = "
                    + handle + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                membersList = resultSet.getString(1);

                //splits the messages and adds the new message
                String[] members = membersList.split(",");
                for (String member : members) {
                    if (!sender.equals(member)) {
                        Saver.addGroupMessageToUnreadMessages(handle, member);
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}

        return messageID;
    }

    public static void addGroupMessageToUnreadMessages(int groupID, String username){
        //declares new message count
        int newMessages;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT count FROM unreadgroups WHERE forUsername = '"
                                                        + username + "' AND groupID = " + groupID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                newMessages = resultSet.getInt(1);
                newMessages ++;
                Connector.queryWithoutResult("UPDATE unreadgroups SET count = " + newMessages
                                                    + " WHERE forUsername = '" + username + "' AND groupID = "
                                                    + groupID + ";");
            }
            else {
                Connector.queryWithoutResult("INSERT INTO unreadgroups (forUsername, groupID, count) VALUES ('"
                                                    + username + "', " + groupID + ", 1);");
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
    }

    public static int addToPosts(String username, LocalDateTime now,
                                 String description, String postType, String mediaID) {
        //declares the postID
        int postID = 0;

        //formats date and time
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatObj);

        //adds the post to the posts
        Connector.queryWithoutResult
                ("INSERT INTO posts (username, description, mediaID, date, type) VALUES ('"
                        + username +"', '" + description + "', '" + mediaID + "', '" + formattedDate + "', '"
                        + postType + "');");

        //gets the handle
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement
                    ("SELECT postID FROM posts ORDER BY postID DESC;").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                postID = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return postID;
    }
}
