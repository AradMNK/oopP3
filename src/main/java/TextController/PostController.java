package TextController;

import Login.LoginState;
import Login.Loginner;
import Objects.User;

public class PostController {
    static void newPost(){
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login first to post.");
            return;
        }

        User user = Loginner.loginnedUser;

        String message = message();
        if (message.equals("")) {
            TextController.println("Cannot create an empty post.");
            return;
        }

        user.post(message);

        TextController.println("Posted! Preview:");
        TextController.println(message);
    }

    private static String message(){
        TextController.println("Write your post and type /" + CommandType.POST + " at the start of a new line to post.");

        StringBuilder stringBuilder = new StringBuilder();
        String line = TextController.getLine();

        while (true){
            stringBuilder.append(line);
            line = TextController.getLine();
            if (line.equals("/" + CommandType.POST)) break;
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
