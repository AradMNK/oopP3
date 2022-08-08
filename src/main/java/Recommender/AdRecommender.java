package Recommender;

import Login.Loginner;
import java.util.*;

public class AdRecommender {
    private static final int NUMBER_OF_RECOMMENDED_ADS = 3, NUMBER_OF_TRIES = 5;

    public static Integer[] recommendAdAlgorithmic(){
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

        return Arrays.copyOfRange(sortedPosts.toArray(new Integer[0]),
                0, Math.min(sortedPosts.size(), NUMBER_OF_RECOMMENDED_ADS));
    }

    public static Integer[] recommendAd(){
        Integer[] algorithmic = recommendAdAlgorithmic();
        ArrayList<Integer> append = new ArrayList<>();
        if (algorithmic.length < NUMBER_OF_RECOMMENDED_ADS){
            Integer ID;
            int i = 0;
            while (append.size() < NUMBER_OF_RECOMMENDED_ADS - algorithmic.length && i < NUMBER_OF_TRIES){
                ID = Database.Loader.getRandomAd(Loginner.loginnedUser.getUsername(), Database.Loader.getLikedAds
                        (Loginner.loginnedUser.getUsername()).toArray(new Integer[0]));
                i++;
                if (ID.equals(0)) continue;
                if (!append.contains(ID)) append.add(ID);
            }
        }
        Integer[] appended = append.toArray(new Integer[0]);
        Integer[] result = new Integer[append.size() + algorithmic.length];
        int i;
        for (i = 0; i < algorithmic.length; i++) result[i] = algorithmic[i];
        for (Integer s : appended) {
            result[i++] = s;
        }
        return result;
    }
}
