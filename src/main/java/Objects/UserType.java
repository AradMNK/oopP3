package Objects;

public enum UserType {
    NORMAL("normal"), BUSINESS("business");

    private final String label;

    UserType(String s){label = s;}

    public static UserType[] getValues() { return values();}

    @Override
    public String toString(){
        return label;
    }
}
