package TextController;

import Login.LoginState;
import Login.Loginner;
import Objects.*;

public class FeedController {
    private static final int overlineCount = 15, ellipsisCount = 20;
    private static final String ellipsis = "...";

    public static void show(){
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login first to see your feed.");
            return;
        }

        User user = Loginner.loginnedUser;

        showMenu(user);
    }

    public static void showMenu(User user){
        boolean hasPost = Database.Loader.userHasPostFeed(user.getUsername()),
                hasComment = Database.Loader.userHasCommentFeed(user.getUsername()),
                hasLike = Database.Loader.userHasLikeFeed(user.getUsername());

        if (!hasPost && !hasComment && !hasLike) {
            TextController.println("Your feed is empty, for now.");
            return;
        }

        TextController.println("Please choose:");
        if (hasPost)  TextController.println("1. New posts from followings");
        if (hasComment) TextController.println("2. New comments on your posts");
        if (hasLike) TextController.println("3. New likes on your posts");

        int choice;

        try{choice = TextController.scanner.nextInt();}
        catch (Exception e){TextController.println("Please enter a number."); showMenu(user); return;}

        if (choice == 1 && hasPost) showPosts(user);
        else if (choice == 2 && hasComment) showComments(user);
        else if (choice == 3 && hasLike) showLikes(user);
        else TextController.println("Please enter a valid number.");
    }

    private static void showLikes(User user) {
        for (Like like: user.getFeed().getLikes()){
            TextController.println("[" + like.getPost().getPostID().getHandle()
                    + "] (" + ellipsis(like.getPost().getDescription())
                    + ") was liked by: @[" + like.getLikerUsername() + "]");
            Database.Changer.removeLikeFromFeed(user.getUsername(), like.getPost().getPostID().getHandle());
        }
    }

    private static String ellipsis(String description) {
        return (description.length() > ellipsisCount + ellipsis.length()) ?
                description.substring(0, ellipsisCount) + ellipsis : description;
    }

    private static void showComments(User user) {
        for (Comment comment : user.getFeed().getComments()){
            TextController.println("Replying to:");
            writePost(comment.getPost());
            TextController.println("-".repeat(overlineCount));
            writeComment(comment);
            TextController.println("");
            Database.Changer.removeCommentFromFeed(user.getUsername(), comment.getCommentID().getHandle());
        }
    }

    private static void showPosts(User user) {
        for (Post post : user.getFeed().getPosts()) {
            Database.Changer.removePostFromFeed(user.getUsername(), post.getPostID().getHandle());
            writePost(post);
            TextController.println("‾".repeat(overlineCount));
        }
    }

    private static void writePost(Post post){
        if (post.getPoster().getUserType() == UserType.BUSINESS) TextController.print("** AD **");
        TextController.println("(" + post.getDatePosted() + ") "
                + post.getPoster().getName() + "[@" + post.getPoster().getUsername() + "]:" );
        TextController.println(post.getDescription());
        TextController.println("postid: " + post.getPostID() + "\"");
    }

    private static void writeComment(Comment comment) {
        TextController.println("(" + comment.getDate() + ") "
                + comment.getCommenter().getName() + "[@" + comment.getCommenter().getUsername() + "]:" );
        TextController.println(comment.getMsg());
    }
}
