package Recommender;

import Builder.UserBuilder;
import Login.Loginner;
import Objects.User;
import java.util.*;

public class UserRecommender {
    private static final int NUMBER_OF_RECOMMENDED_USERS = 3, NUMBER_OF_TRIES = 5;

    public static User[] recommendAlgorithmic(){
        HashSet<User> followerOfFollowers = new HashSet<>();

        for (String followerUsername: Loginner.loginnedUser.getFollowings()){
            User user = UserBuilder.getUserFromDatabaseWithFollowings(followerUsername);
            for (String followerUsername2 : user.getFollowings())
                followerOfFollowers.add(UserBuilder.getUserFromDatabaseWithFollowings(followerUsername2));
        }

        followerOfFollowers.removeIf(user -> Loginner.loginnedUser.getFollowings().contains(user.getUsername()));

        User[] candidateUsernames = new User[followerOfFollowers.size()];
        Integer[] scores = new Integer[followerOfFollowers.size()];
        int i = 0;

        for (User candidate : followerOfFollowers) {
            int score = 0;
            for (String followedByUser : Loginner.loginnedUser.getFollowers())
                if (candidate.getFollowers().contains(followedByUser)) score++;
            candidateUsernames[i] = candidate;
            scores[i++] = score;
        }

        final List<User> stringListCopy = Arrays.asList(candidateUsernames);
        ArrayList<User> sortedList = new ArrayList<>(stringListCopy);
        sortedList.sort(Comparator.comparing(user -> scores[stringListCopy.indexOf(user)]));

        return Arrays.copyOfRange(sortedList.toArray(new User[0]),
                0, Math.min(sortedList.size(), NUMBER_OF_RECOMMENDED_USERS));
    }

    public static User[] recommendUser(){
        User[] algorithmic = recommendAlgorithmic();
        ArrayList<String> append = new ArrayList<>();
        if (algorithmic.length < NUMBER_OF_RECOMMENDED_USERS){
            String username;
            int i = 0;
            while (append.size() < NUMBER_OF_RECOMMENDED_USERS - algorithmic.length && i < NUMBER_OF_TRIES){
                username = Database.Loader.randomUser(Loginner.loginnedUser.getUsername(),
                        Loginner.loginnedUser.getFollowings().toArray(new String[0]));
                if (!append.contains(username)) append.add(username);
                i++;
            }
        }
        String[] appended = append.toArray(new String[0]);
        User[] result = new User[append.size() + algorithmic.length];
        int i;
        for (i = 0; i < algorithmic.length; i++) result[i] = algorithmic[i];
        for (String s : appended) {
            result[i++] = UserBuilder.getUserFromDatabase(s);
        }
        return result;
    }
}
