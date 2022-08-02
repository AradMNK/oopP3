package Builder;

import Objects.Feed;
import Objects.Post;
import Objects.User;
import javafx.scene.control.DatePicker;

import java.sql.DatabaseMetaData;

public class FeedBuilder {
    public static Feed getFeedFromDatabase(User user){
        Feed feed = new Feed();
        int[] feedPostIDs = Database.Loader.getPostFeed(user.getUsername());
        int[] feedLikeIDs = Database.Loader.getLikeFeed(user.getUsername());
        int[] feedCommentIDs = Database.Loader.getCommentFeed(user.getUsername());

        return new Feed();
    }
}
