package TextController;

import Login.LoginState;
import Login.Loginner;
import Objects.UserType;

public class StatsController {
    public static void show(String[] args) {
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login first to see your stats.");
            return;
        }

        if (Loginner.loginnedUser.getUserType() == UserType.NORMAL){
            TextController.println("You need to have a business account to use this command.");
            return;
        }

        if (args.length > 0){
            int postID;
            try {postID = Integer.parseInt(args[0]);} catch (NumberFormatException e){e.printStackTrace(); return;}
            showForPostID(postID);
            return;
        }

        TextController.println("Today's views on your account: "
                + Database.Loader.getTotalViews(Loginner.loginnedUser.getUsername()));
        TextController.println("Today's likes on your account: "
                + Database.Loader.getTotalLikes(Loginner.loginnedUser.getUsername()));
    }

    private static void showForPostID(int postID) {
        if (!Database.Loader.postIdExists(postID)){
            TextController.println("PostID \"" + postID + "\" does not exist.");
            return;
        }

        if (!Database.Loader.getPostPoster(postID).equals(Loginner.loginnedUser.getUsername())){
            TextController.println("That post is not your post!");
            return;
        }

        TextController.println("Today's likes on post \"" + postID + "\"were: " + Database.Loader.getNumberOfLikeStats(postID));
        TextController.println("Today's views on post \"" + postID + "\"were: " + Database.Loader.getViews(postID));
    }
}
