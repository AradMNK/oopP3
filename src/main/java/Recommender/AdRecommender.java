package Recommender;

import Login.Loginner;
import java.util.*;

public class AdRecommender {
    private static final int NUMBER_OF_RECOMMENDED_ADS = 3;

    public static Integer[] recommendAd(){
        final HashSet<Integer> likedPostsOfFollowers = new HashSet<>();

        for (String followerUsername: Loginner.loginnedUser.getFollowings())
            likedPostsOfFollowers.addAll(Database.Loader.getLikedAds(followerUsername));

        likedPostsOfFollowers.removeIf(Database.Loader.getLikedAds(Loginner.loginnedUser.getUsername())::contains);

        final List<Integer> postsCopy = likedPostsOfFollowers.stream().toList();
        ArrayList<Integer> sortedPosts = new ArrayList<>(postsCopy);
        final Integer[] likeNum = new Integer[sortedPosts.size()];
        for (int i = 0; i < sortedPosts.size(); i++) {
            likeNum[i] = Database.Loader.getNumberOfLikes(sortedPosts.get(i));
        }
        sortedPosts.sort(Comparator.comparing(integer -> likeNum[postsCopy.indexOf(integer)]));

        return (Integer[]) Arrays.copyOfRange(sortedPosts.toArray(),
                0, Math.min(sortedPosts.size(), NUMBER_OF_RECOMMENDED_ADS));
    }
}
