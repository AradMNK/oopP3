package TextController;

import Login.LoginState;
import Login.Loginner;
import Objects.User;

public class CommentController {
    static void newComment(String postIDasString){
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login first to comment.");
            return;
        }

        int postID;
        try {postID = Integer.parseInt(postIDasString);} catch (NumberFormatException e){e.printStackTrace(); return;}

        User user = Loginner.loginnedUser;
        if (!Database.Loader.postIdExists(postID)){
            TextController.println("The post with ID \"" + postID + "\" does not exist.");
            return;
        }

        String message = message();
        if (message.equals("")) {
            TextController.println("Cannot create an empty post.");
            return;
        }

        user.comment(postID, message);

        TextController.println("Commented! Preview:");
        TextController.println(message);
    }

    private static String message(){
        TextController.println("Write your post and type /" + CommandType.COMMENT + " at the start of a new line to post.");

        StringBuilder stringBuilder = new StringBuilder();
        String line = TextController.getLine();

        while (true){
            stringBuilder.append(line);
            line = TextController.getLine();
            if (line.equals("/" + CommandType.COMMENT)) break;
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
