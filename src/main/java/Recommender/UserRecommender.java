package Recommender;

import Builder.UserBuilder;
import Login.Loginner;
import Objects.User;
import java.util.*;

public class UserRecommender {
    private static final int NUMBER_OF_RECOMMENDED_USERS = 3;

    public static String[] recommendUser(){
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

        return (String[]) Arrays.copyOfRange(sortedList.toArray(),
                0, Math.min(sortedList.size(), NUMBER_OF_RECOMMENDED_USERS));
    }
}
