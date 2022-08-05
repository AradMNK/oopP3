package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class Loader {
    public static boolean loginMatch(String username, String hashPass){
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM users WHERE username = '" + username
                                                        + "' AND hashPass = '" + hashPass + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean usernameExists(String username){
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM users WHERE username = '" + username
                                                        + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean userHasCommentFeed(String username) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM feed WHERE username = '" + username
                                                        +"' AND type = 'comment';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean userHasLikeFeed(String username) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM feed WHERE username = '" + username
                                                        +"' AND type = 'like';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean userHasPostFeed(String username) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM feed WHERE username = '" + username
                                                        +"' AND type = 'post';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean postIdExists(int postID) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM posts WHERE postID = "
                                                        + postID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static int getNumberOfLikes(int postID) {
        //declares the number of likes
        int numberOfLikes = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(likeID) FROM likes WHERE postID = " + postID
                                                        + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                numberOfLikes = Integer.parseInt(resultSet.getString(1));
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return numberOfLikes;
    }

    public static String[] getLikerUsernames(int postID) {
        //declares the empty array
        String[] likerUsernames = new String[0];

        //declares the number of likes
        int numberOfLikes;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(username) FROM likes WHERE postID = " + postID
                                                        + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of likes
                numberOfLikes = resultSet.getInt(1);

                if (numberOfLikes != 0) {
                    //declares the array and gets the usernames
                    likerUsernames = new String[numberOfLikes];

                    //gets the likes
                    resultSet = connection.prepareStatement("SELECT username FROM likes WHERE postID = " + postID
                                                                + ";").executeQuery();

                    for (int i = 0; i < numberOfLikes; i++) {
                        if (resultSet.next()){
                            likerUsernames[i] = resultSet.getString(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return likerUsernames;
    }

    public static String getUserName (String username){
        //declares the name
        String name = null;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT name FROM users WHERE username = '" + username
                                                        + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                name = resultSet.getString(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return name;
    }

    public static String[] getUserDetails (String username){
        //declares a string array to store the details
        String[] userDetails = new String[6];

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT name, bio, subtitle, date, pfp, type FROM users WHERE username = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                for (int i = 0; i < userDetails.length; i++){
                    userDetails[i] = resultSet.getString(i+1);
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return userDetails;
    }

    public static boolean postIsAd(int postID) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM posts WHERE postID = " + postID
                                                        + " AND type = 'business';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static int getViews(int postID) {
        //declares the view count
        int viewCount = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(DISTINCT username) FROM views WHERE postID = " + postID
                                                        +";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                viewCount = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return viewCount;
    }

    public static int getNumberOfLikeStats(int postID) {
        //declares the like count
        int likeCount = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(DISTINCT username) FROM likestat WHERE postID = " + postID
                                                        +";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                likeCount = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return likeCount;
    }

    public static boolean usersHaveDm(String username1, String username2) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM directmessage WHERE (sender = '" + username1
                                                        + "' AND receiver = '" + username2 + "') OR (sender = '"
                                                        + username2 + "' AND receiver = '" + username1
                                                        + "');").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static String getDirectMessageContent (int handle){
        //declares the message found in the result set
        String message;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT message FROM directmessage WHERE messageID = "
                                                        + handle + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                message = resultSet.getString(1);
                return message;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return "message was deleted";
    }

    public static boolean isUserBlocked(String blocker, String blocked) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM block WHERE blocker = '" + blocker
                                                        + "' and blocked ='" + blocked + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean isPostLiked(int postID, String username) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM likes WHERE postID = " + postID
                                                        + " AND username = '" + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean userFollows(String follower, String followed) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM follow WHERE follower = '" + follower
                                                        + "' AND followed = '" + followed + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static int getTotalViews(String username) {
        //declares the view count
        int viewCount = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(DISTINCT view.username) FROM posts INNER JOIN views"
                                                        + " ON posts.postID = views.postID AND"
                                                        + " posts.username = '" + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                viewCount = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return viewCount;
    }

    public static int getTotalLikes(String username) {
        //declares the view count
        int likeCount = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(DISTINCT likestat.username) FROM posts INNER JOIN likestat"
                                                        + " ON posts.postID = likestat.postID AND"
                                                        + " posts.username = '" + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                likeCount = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return likeCount;
    }

    public static String getGroupMessageContent(int handle) {
        //declares the message
        String message;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT message FROM groupmessage WHERE messageID = "
                                                        + handle +";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                message = resultSet.getString(1);
                return message;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return "message was deleted";
    }

    public static boolean groupExists(int groupID) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM group_chats WHERE groupID = " + groupID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean groupJoinerExists(String groupJoiner) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM group_chats WHERE joinID = '" + groupJoiner + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static String getPostPoster(int postID) {
        //declares the post poster
        String postPoster = "";

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT username FROM posts WHERE postID = " + postID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                postPoster = resultSet.getString(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return postPoster;
    }

    public static HashSet<Integer> getLikedAds(String username) {
        //declares a HashSet to store the ads
        HashSet<Integer> likedAds = new HashSet<>();

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT postID FROM likestat WHERE username = '" + username
                                                        + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                do {
                    likedAds.add(resultSet.getInt(1));
                }
                while (resultSet.next());
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return likedAds;
    }

    public static int getGroupID(String joiner) {
        //declares the groupID
        int groupID = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT groupID FROM group_chats WHERE joinID = '"
                                                        + joiner + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                groupID = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return groupID;
    }

    public static int getSecurityQuestionNumber(String username) {
        //declares the security question number
        int securityQuestionNumber = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT questionID FROM users WHERE username = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                securityQuestionNumber = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return securityQuestionNumber;
    }

    public static boolean doesSecurityQuestionAnswerMatch(String username, String answer) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM users WHERE username = '" + username
                                                        + "' AND answer = '" + answer + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static String[] getUnreadUsers (String username){
        //declares the empty array
        String[] unreadUsers = new String[0];

        //declares the number of users
        int numberOfUsers;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(DISTINCT username) FROM unreadusers WHERE forUsername = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of likes
                numberOfUsers = resultSet.getInt(1);

                if (numberOfUsers != 0) {
                    //declares the array and gets the usernames
                    unreadUsers = new String[numberOfUsers];

                    //gets the likes
                    resultSet = connection.prepareStatement("SELECT DISTINCT username FROM unreadusers WHERE forUsername = '"
                                                                + username + "';").executeQuery();

                    for (int i = 0; i < numberOfUsers; i++) {
                        if (resultSet.next()){
                            unreadUsers[i] = resultSet.getString(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return unreadUsers;
    }

    public static int[] getUnreadGroups (String username){
        //declares the empty array
        int[] unreadGroups = new int[0];

        //declares the number of groups
        int numberOfGroups;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(DISTINCT groupID) FROM unreadgroups WHERE forUsername = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of likes
                numberOfGroups = resultSet.getInt(1);

                if (numberOfGroups != 0) {
                    //declares the array and gets the usernames
                    unreadGroups = new int[numberOfGroups];

                    //gets the likes
                    resultSet = connection.prepareStatement("SELECT DISTINCT groupID FROM unreadgroups WHERE forUsername = '"
                                                                + username + "';").executeQuery();

                    for (int i = 0; i < numberOfGroups; i++) {
                        if (resultSet.next()) {
                            unreadGroups[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return unreadGroups;
    }

    public static int getUnreadCountForUsername(String forUsername, String username) {
        //declares the message count
        int count = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT count FROM unreadusers WHERE forUsername = '"
                                                        + forUsername + "' AND username = '" + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                count = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return count;
    }

    public static int getUnreadCountForGroupID(String forUsername, int groupID) {
        //declares the message count
        int count = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT count FROM unreadgroups WHERE forUsername = '"
                                                        + forUsername + "' AND groupID = " + groupID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                count = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return count;
    }

    public static String[] getBlocklist(String username) {
        //declares the empty array
        String[] blockList = new String[0];

        //declares the number of blocked users
        int blockedCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(blocked) FROM block WHERE blocker = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of blocked users
                blockedCount = resultSet.getInt(1);

                if (blockedCount != 0) {
                    //declares the array
                    blockList = new String[blockedCount];

                    //gets the usernames
                    resultSet = connection.prepareStatement("SELECT blocked FROM block WHERE blocker = '"
                                                                + username + "';").executeQuery();

                    for (int i = 0; i < blockedCount; i++) {
                        if (resultSet.next()) {
                            blockList[i] = resultSet.getString(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return blockList;
    }

    public static String[] getFollowers(String username) {
        //declares the empty array
        String[] followers = new String[0];

        //declares the number of followers
        int followerCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(follower) FROM follow WHERE followed = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of followers
                followerCount = resultSet.getInt(1);

                if (followerCount != 0) {
                    //declares the array
                    followers = new String[followerCount];

                    //gets the usernames
                    resultSet = connection.prepareStatement("SELECT follower FROM follow WHERE followed = '"
                                                                + username + "';").executeQuery();

                    for (int i = 0; i < followerCount; i++) {
                        if (resultSet.next()) {
                            followers[i] = resultSet.getString(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return followers;
    }

    public static String[] getFollowings(String username) {
        //declares the empty array
        String[] followings = new String[0];

        //declares the number of followings
        int followingCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(followed) FROM follow WHERE follower = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of followings
                followingCount = resultSet.getInt(1);

                if(followingCount != 0) {
                    //declares the array
                    followings = new String[followingCount];

                    //gets the usernames
                    resultSet = connection.prepareStatement("SELECT followed FROM follow WHERE follower = '"
                                                                 + username + "';").executeQuery();

                    for (int i = 0; i < followingCount; i++) {
                        if (resultSet.next()) {
                            followings[i] = resultSet.getString(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return followings;
    }

    public static int[] getUserPosts(String username) {
        //declares the empty array
        int[] posts = new int[0];

        //declares the number of posts
        int postCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(postID) FROM posts WHERE username = '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of posts
                postCount = resultSet.getInt(1);

                if (postCount != 0) {
                    //declares the array
                    posts = new int[postCount];

                    //gets the postIDs
                    resultSet = connection.prepareStatement("SELECT postID FROM posts WHERE username = '"
                                                                + username + "';").executeQuery();

                    for (int i = 0; i < postCount; i++) {
                        if (resultSet.next()) {
                            posts[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return posts;
    }

    public static int[] getGroupsOfUser(String username) {
        //declares the empty array
        int[] groups = new int[0];

        //declares the number of groups
        int groupCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(groupID) FROM group_chats WHERE members LIKE '"
                                                        + username + ",%' OR members LIKE '%," + username
                                                        + "' OR members LIKE '%," + username + ",%' OR members LIKE '"
                                                        + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of groups
                groupCount = resultSet.getInt(1);

                if (groupCount != 0) {
                    //declares the array
                    groups = new int[groupCount];

                    //gets the groupIDs
                    resultSet = connection.prepareStatement("SELECT COUNT(groupID) FROM group_chats WHERE members LIKE '"
                                                                + username + ",%' OR members LIKE '%," + username
                                                                + "' OR members LIKE '%," + username
                                                                + ",%' OR members LIKE '" + username + "';").executeQuery();

                    for (int i = 0; i < groupCount; i++) {
                        if (resultSet.next()) {
                            groups[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return groups;
    }

    public static String[] getCommentDetails(int commentID) {
        //declares a string array to store the details
        String[] commentDetails = new String[4];

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT username, postID, comment, date FROM comments WHERE commentID = "
                                                        + commentID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                for (int i = 0; i < commentDetails.length; i++){
                    commentDetails[i] = resultSet.getString(i+1);
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return commentDetails;
    }

    public static String[] getPostDetails(int postID) {
        //declares a string array to store the details
        String[] postDetails = new String[5];

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT username, description, mediaID, date, type FROM posts WHERE postID = "
                                                        + postID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                for (int i = 0; i < 5; i++){
                    postDetails[i] = resultSet.getString(i+1);
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return postDetails;
    }

    public static int[] searchInDirect (String username1, String username2, String pattern){
        //declares an array for the messages found in the direct
        int[] messageIDs = new int[0];

        //declares the number of the results
        int numberOfResults;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(messageID) FROM directmessage WHERE ((sender = '"
                                                        + username1 + "' AND receiver = '" + username2 + "') OR (sender = '"
                                                        + username2 + "' AND receiver = '" + username1
                                                        + "')) AND message LIKE '%" + pattern + "%';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                numberOfResults = resultSet.getInt(1);

                //checks if the resultSet isn't empty
                if (resultSet.getInt(1) != 0) {
                    //declares the array
                    messageIDs = new int[numberOfResults];

                    //adds the IDs to the array
                    resultSet = connection.prepareStatement("SELECT messageID FROM directmessage WHERE ((sender = '"
                                                                + username1 + "' AND receiver = '" + username2
                                                                + "') OR (sender = '" + username2 + "' AND receiver = '"
                                                                + username1 + "')) AND message LIKE '%" + pattern
                                                                + "%';").executeQuery();

                    for (int i = 0; i < numberOfResults; i++) {
                        if (resultSet.next()) {
                            messageIDs[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageIDs;
    }

    public static int[] searchInGroup (String username, int groupID, String pattern){
        //declares an array for the messages found in the group chat
        int[] messageIDs = new int[0];

        //declares the number of the results
        int numberOfResults;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(messageID) FROM groupmessage WHERE groupID = "
                                                        + groupID + " AND (members LIKE '" + username
                                                        + ",%' OR members LIKE '%," + username + "' OR members LIKE '%,"
                                                        + username + ",%'" + "OR members LIKE '" + username
                                                        + "') AND message LIKE '%" + pattern + "%';").executeQuery();
            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                numberOfResults = resultSet.getInt(1);

                //checks if the resultSet isn't empty
                if (resultSet.getInt(1) != 0) {
                    //declares the array
                    messageIDs = new int[numberOfResults];

                    //adds the IDs to the array
                    resultSet = connection.prepareStatement("SELECT messageID FROM groupmessage WHERE groupID = "
                                                                + groupID + " AND (members LIKE '" + username
                                                                + ",%' OR members LIKE '%," + username
                                                                + "' OR members LIKE '%," + username + ",%'"
                                                                + "OR members LIKE '" + username
                                                                + "') AND message LIKE '%" + pattern + "%';").executeQuery();

                    for (int i = 0; i < numberOfResults; i++) {
                        if (resultSet.next()) {
                            messageIDs[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageIDs;
    }

    public static int[] searchDirects (String username, String pattern){
        //declares an array for the messages found in the group chat
        int[] messageIDs = new int[0];

        //declares the number of the results
        int numberOfResults;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(messageID) FROM directmessage WHERE (sender ='"
                                                        + username + "' OR receiver = '" + username
                                                        + "') AND message LIKE '%" + pattern + "%';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                numberOfResults = resultSet.getInt(1);

                //checks if the resultSet isn't empty
                if (resultSet.getInt(1) != 0) {
                    //declares the array
                    messageIDs = new int[numberOfResults];

                    //adds the IDs to the array
                    resultSet = connection.prepareStatement("SELECT messageID FROM directmessage WHERE (sender ='"
                                                                + username + "' OR receiver = '" + username
                                                                + "') AND message LIKE '%" + pattern + "%';").executeQuery();

                    for (int i = 0; i < numberOfResults; i++) {
                        if (resultSet.next()) {
                            messageIDs[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageIDs;
    }

    public static int[] searchGroups (String username, String pattern){
        //declares an array for the messages found in the group chat
        int[] messageIDs = new int[0];

        //declares the number of the results
        int numberOfResults;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(groupmessage.messageID) FROM group_chats "
                                                        + "INNER JOIN groupmessage ON group_chats.groupID = groupmessage.groupID "
                                                        + "WHERE (group_chats.members LIKE '" + username
                                                        + ",%' OR members LIKE '%," + username + "' OR members LIKE '%,"
                                                        + username + ",%'" + "OR members LIKE '" + username
                                                        + "') AND groupmessage.message LIKE '%" + pattern
                                                        + "%';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                numberOfResults = resultSet.getInt(1);

                //checks if the resultSet isn't empty
                if (resultSet.getInt(1) != 0) {
                    //declares the array
                    messageIDs = new int[numberOfResults];

                    //adds the IDs to the array
                    resultSet = connection.prepareStatement("SELECT groupmessage.messageID FROM group_chats "
                                                                + "INNER JOIN groupmessage ON group_chats.groupID = groupmessage.groupID "
                                                                + "WHERE (group_chats.members LIKE '" + username
                                                                + ",%' OR members LIKE '%," + username
                                                                + "' OR members LIKE '%," + username + ",%'"
                                                                + "OR members LIKE '" + username
                                                                + "') AND groupmessage.message LIKE '%" + pattern
                                                                + "%';").executeQuery();

                    for (int i = 0; i < numberOfResults; i++) {
                        if (resultSet.next()) {
                            messageIDs[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageIDs;
    }

    public static String[] searchForUsers (String pattern){
        //declares an array for users found
        String[] users = new String[0];

        //declares the number of the results
        int numberOfResults;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(username) FROM users WHERE username LIKE '%"
                                                        + pattern + "%' OR name LIKE '%" + pattern + "%';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                numberOfResults = resultSet.getInt(1);

                //checks if the resultSet isn't empty
                if (numberOfResults != 0) {
                    //declares the array
                    users = new String[numberOfResults];

                    //adds the IDs to the array
                    resultSet = connection.prepareStatement("SELECT username FROM users WHERE username LIKE '%"
                                                                + pattern + "%' OR name LIKE '%"
                                                                + pattern + "%';").executeQuery();

                    for (int i = 0; i < numberOfResults; i++) {
                        if (resultSet.next()) {
                            users[i] = resultSet.getString(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return users;
    }

    public static int[] getPostComments(int postID) {
        //declares an array for the commentIDs
        int[] comments = new int[0];

        //declares the number of the comments
        int commentCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(commentID) FROM comments WHERE postID = "
                                                        + postID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                commentCount = resultSet.getInt(1);

                if (commentCount != 0) {
                    //declares the array
                    comments = new int[commentCount];

                    //saves the comments
                    resultSet = connection.prepareStatement("SELECT commentID FROM comments WHERE postID = "
                                                                + postID + ";").executeQuery();

                    for (int i = 0; i < commentCount; i++){
                        if (resultSet.next()) {
                            comments[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return comments;
    }

    public static String getGroupJoiner(int groupID) {
        //declares the group joiner
        String joiner = "";

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT joinID FROM group_chats WHERE groupID = "
                                                        + groupID + ";").executeQuery();

            if (resultSet.next()){
                joiner = resultSet.getString(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return joiner;
    }

    public static String getGroupOwner(int groupID) {
        //declares the group owner
        String owner = "";

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT admin FROM group_chats WHERE groupID = "
                                                        + groupID + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                owner = resultSet.getString(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return owner;
    }

    public static int[] getGroupMessageIDsOfGroup(int groupID, int howMany) {
        //declares an array for the messages
        int[] messageIDs = new int[0];

        //declares the number of results
        int resultCount;
        int displayingResultCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(messageID) FROM groupmessage WHERE groupID = "
                                                        + groupID + " ORDER BY messageID DESC LIMIT "
                                                        + howMany + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                resultCount = resultSet.getInt(1);

                //checks if the number of results isn't zero
                if (resultCount != 0) {
                    resultSet = connection.prepareStatement("SELECT messageID FROM groupmessage WHERE groupID = "
                                                                + groupID + " ORDER BY messageID DESC LIMIT "
                                                                + howMany + ";").executeQuery();

                    //declares the array
                    displayingResultCount = Math.min(resultCount, howMany);
                    messageIDs = new int [displayingResultCount];

                    resultSet.next();
                    for (int i = displayingResultCount - 1; i >= 0; i--) {
                        messageIDs[i] = resultSet.getInt(1);
                        resultSet.next();
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageIDs;
    }

    public static int[] getMessageIDsOfUsers(String username1, String username2, int howMany) {
        //declares an array for the messages
        int[] messageIDs = new int[0];

        //declares the number of results
        int resultCount;
        int displayingResultCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(messageID) FROM directmessage WHERE (sender = '"
                                                        + username1 + "' AND receiver = '" + username2
                                                        + "') OR (sender = '" + username2 + "' AND receiver = '"
                                                        + username1 + "') ORDER BY messageID DESC LIMIT "
                                                        + howMany + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                resultCount = resultSet.getInt(1);

                //checks if the number of results isn't zero
                if (resultCount != 0) {
                    resultSet = connection.prepareStatement("SELECT messageID FROM directmessage WHERE (sender = '"
                                                                + username1 + "' AND receiver = '" + username2
                                                                + "') OR (sender = '" + username2 + "' AND receiver = '"
                                                                + username1 + "') ORDER BY messageID DESC LIMIT "
                                                                + howMany + ";").executeQuery();

                    //declares the array
                    displayingResultCount = Math.min(resultCount, howMany);
                    messageIDs = new int [displayingResultCount];

                    resultSet.next();
                    for (int i = displayingResultCount - 1; i >= 0; i--) {
                        messageIDs[i] = resultSet.getInt(1);
                        resultSet.next();
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageIDs;
    }

    public static String[] getGroupMessageDetails(int groupMessageID) {
        //declares a string array to store the details
        String[] groupMessageDetails = new String[6];

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT groupID, sender, message, date, replyMessageID, originalSender "
                                                        + "FROM groupmessage WHERE messageID = " + groupMessageID
                                                        + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                for (int i = 0; i < groupMessageDetails.length; i++){
                    groupMessageDetails[i] = resultSet.getString(i+1);
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return groupMessageDetails;
    }

    public static String[] getMessageDetails(int messageID) {
        //declares a string array to store the details
        String[] messageDetails = new String[5];

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT sender, message, date, replyMessageID, originalSender " +
                                                        "FROM directmessage WHERE messageID = " + messageID
                                                        + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                for (int i = 0; i < messageDetails.length; i++){
                    messageDetails[i] = resultSet.getString(i+1);
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return messageDetails;
    }

    public static int[] getPostFeed(String username) {
        //declares an array for the postIDs
        int[] posts = new int[0];

        //declares the number of the posts
        int postCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(ID) FROM feed WHERE username = '" + username
                                                        + "' AND type = 'post';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                postCount = resultSet.getInt(1);

                if (postCount != 0) {
                    //declares the array
                    posts = new int[postCount];

                    //saves the posts
                    resultSet = connection.prepareStatement("SELECT ID FROM feed WHERE username = '" + username
                                                                + "' AND type = 'post';").executeQuery();

                    for (int i = 0; i < postCount; i++){
                        if (resultSet.next()) {
                            posts[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return posts;
    }

    public static int[] getLikeFeed(String username) {
        //declares an array for the likeIDs
        int[] likes = new int[0];

        //declares the number of the likes
        int likeCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(ID) FROM feed WHERE username = '" + username
                                                        + "' AND type = 'like';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                likeCount = resultSet.getInt(1);

                if (likeCount != 0) {
                    //declares the array
                    likes = new int[likeCount];

                    //saves the posts
                    resultSet = connection.prepareStatement("SELECT ID FROM feed WHERE username = '" + username
                                                                + "' AND type = 'like';").executeQuery();

                    for (int i = 0; i < likeCount; i++){
                        if (resultSet.next()) {
                            likes[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return likes;
    }

    public static int[] getCommentFeed(String username) {
        //declares an array for the commentIDs
        int[] comments = new int[0];

        //declares the number of the posts
        int commentCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(ID) FROM feed WHERE username = '" + username
                                                        + "' AND type = 'comment';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                commentCount = resultSet.getInt(1);

                if (commentCount != 0) {
                    //declares the array
                    comments = new int[commentCount];

                    //saves the posts
                    resultSet = connection.prepareStatement("SELECT ID FROM feed WHERE username = '" + username
                                                                + "' AND type = 'comment';").executeQuery();

                    for (int i = 0; i < commentCount; i++){
                        if (resultSet.next()) {
                            comments[i] = resultSet.getInt(1);
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return comments;
    }

    public static int getNumberOfComments(int postID) {
        //declares the number of comments
        int numberOfComments = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(commentID) FROM comments WHERE postID = " + postID
                                                        + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()) {
                numberOfComments = Integer.parseInt(resultSet.getString(1));
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return numberOfComments;
    }

    public static String getLikerUsername(int likeID) {
        //declares the username
        String username = null;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT username FROM likes WHERE likeID = '" + likeID
                                                        + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                username = resultSet.getString(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return username;
    }

    public static int getPostOfLike(int likeID) {
        //declares the postID
        int postID = 0;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT postID FROM likes WHERE likeID = '" + likeID
                                                        + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                postID = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return postID;
    }

    public static String getGroupName(int groupID) {
        //declares the group name
        String name = null;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT name FROM group_chats WHERE groupID = " + groupID
                                                        + ";").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                name = resultSet.getString(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return name;
    }

    public static String randomUser (String username, String[] usernameDontInclude){
        //declares an array of usernames not to be included
        String [] usernamesNotToBeIncluded = new String[usernameDontInclude.length + 1];
        usernamesNotToBeIncluded[0] = username;
        System.arraycopy(usernameDontInclude,
                0, usernamesNotToBeIncluded, 1, usernameDontInclude.length);

        //declares the sql
        StringBuilder query = new StringBuilder("SELECT username FROM users WHERE NOT (");
        for (int i = 0; i < usernamesNotToBeIncluded.length; i++){
            if (i != usernameDontInclude.length)
                query.append("username = '").append(usernamesNotToBeIncluded[i]).append("' OR ");
            else {
                query.append("username = '").append(usernamesNotToBeIncluded[i]).append("') ORDER BY RAND() LIMIT 1;");
            }
        }

        //declares the username in the resultSet
        String result = null;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement(query.toString()).executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                result = resultSet.getString(1);
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return result;
    }

    public static boolean doesUserHaveChat(String username) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM directs WHERE user1 = '"
                                                        + username + "' OR user2 = '" + username
                                                        + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static boolean doesUserHaveGroups(String username) {
        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT * FROM group_chats WHERE members LIKE '"
                                                        + username + ",%' OR members LIKE '%," + username
                                                        + "' OR members LIKE '%," + username + ",%'"
                                                        + " OR members LIKE '" + username + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                return true;
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return false;
    }

    public static String[] getChats(String username) {
        //declares the empty array
        String[] chats = new String[0];

        //declares the number of chats
        int chatCount;

        Connection connection = Connector.connector.connect();
        ResultSet resultSet;
        try {
            resultSet = connection.prepareStatement("SELECT COUNT(directID) FROM directs WHERE user1 = '"
                                                        + username + "' OR user2 = '" + username
                                                        + "';").executeQuery();

            //checks if the resultSet isn't empty
            if (resultSet.next()){
                //gets the number of chats
                chatCount = resultSet.getInt(1);

                if (chatCount != 0) {
                    //declares the array and gets the usernames
                    chats = new String[chatCount];

                    //gets the likes
                    resultSet = connection.prepareStatement("SELECT user1, user2 FROM directs WHERE user1 = '"
                                                                + username + "' OR user2 = '" + username
                                                                + "';").executeQuery();

                    for (int i = 0; i < chatCount; i++) {
                        if (resultSet.next()){
                            if (username.equals(resultSet.getString(2))){
                                chats[i] = resultSet.getString(1);
                            }
                            else if (username.equals(resultSet.getString(1))){
                                chats[i] = resultSet.getString(2);
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException e) {e.printStackTrace();}
        finally {Connector.connector.disconnect();}
        return chats;
    }
}