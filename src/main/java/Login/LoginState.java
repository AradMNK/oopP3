package Login;

public enum LoginState {
    SIGN_IN, SIGN_OUT;

    public static LoginState getLoginState(String username){
        if (Database.Loader.usernameExists(username)) return SIGN_IN;
        return SIGN_OUT;
    }
}