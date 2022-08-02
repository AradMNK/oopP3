package TextController;

import Login.LoginState;
import Login.Loginner;

public class UserEditor {
    final static String end = "/end";

    public static void edit(){
        if (Loginner.loginState == LoginState.SIGN_OUT){
            TextController.println("Please login first before trying to edit you account.");
            return;
        }

        TextController.println("Enter tag=value:");
        String next = TextController.getLine();
        while (!next.equals(end)){
            TextController.println("Enter tag=value to edit your account or " + end + " to end:");
            int index = next.indexOf("=");
            String tag = next.substring(0, index), value = next.substring(index + 1);
            setUserByTag(Loginner.loginnedUser.getUsername(), tag, value);

            next = TextController.getLine();
        }
    }

    private static void setUserByTag(String username, String tag, String value) {
        switch (tag){
            case "bio" -> {Database.Changer.setUserBio(username, value); Loginner.loginnedUser.setBio(value);}
            case "sub" -> {Database.Changer.setUserSubtitle(username, value); Loginner.loginnedUser.setSubtitle(value);}
            case "name" -> {Database.Changer.setUserName(username, value); Loginner.loginnedUser.setName(value);}
        }
    }
}
