package Builder;

import Objects.Like;
import Objects.SaveHandle;

public class LikeBuilder {
    public static Like getLikeFromDatabase(int likeID){
        Like like = new Like();
        like.setLikeID(new SaveHandle(likeID));
        like.setLikerUsername(Database.Loader.getLikerUsername(likeID));
        like.setPost(PostBuilder.getPostFromDatabase(Database.Loader.getPostOfLike(likeID)));
        return like;
    }
}
