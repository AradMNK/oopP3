package Builder;

import Objects.Feed;
import Objects.User;

public class FeedBuilder {
    public static Feed getFeedFromDatabase(User user){
        Feed feed = new Feed();
        int[] feedPostIDs = Database.Loader.getPostFeed(user.getUsername());
        int[] feedLikeIDs = Database.Loader.getLikeFeed(user.getUsername());
        int[] feedCommentIDs = Database.Loader.getCommentFeed(user.getUsername());

        for (int feedPostID: feedPostIDs) feed.getPosts().add(PostBuilder.getPostFromDatabase(feedPostID));
        for (int feedCommentID: feedCommentIDs) feed.getComments().add(CommentBuilder.getCommentFromDatabase(feedCommentID));
        for (int feedLikeID: feedLikeIDs) feed.getLikes().add(LikeBuilder.getLikeFromDatabase(feedLikeID));

        return feed;
    }
}
