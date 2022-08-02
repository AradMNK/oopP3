package Login;

import TextController.TextController;

public enum SecurityQuestion {
    CITY(1, "In what city were you born?"),
    PET(2, "What is the name of your favorite pet?"),
    HIGH_SCHOOL(3, "What high school did you attend?"),
    FOOD(4, "What was your favorite food as a child?"),
    YEAR(5, "What year was your father born?"),
    SIBLING(6, "What is your oldest sibling named?");

    public final int saverID;
    public final String question;

    SecurityQuestion(int saverID, String question){
        this.saverID = saverID;
        this.question = question;
    }

    public static int numOfQuestions() {return values().length;}
    public static void write() {for (SecurityQuestion q: values()) TextController.println(q.saverID + ". " + q.question);}

    public static SecurityQuestion getSecurityQuestionByNumber(int secQuestionNum) {
        for (SecurityQuestion q : values()) if (secQuestionNum == q.saverID) return q;
        throw new SecurityQuestionException();
    }

    public static SecurityQuestion[] getValues() {return values();}

    @Override
    public String toString(){return question;}
}
