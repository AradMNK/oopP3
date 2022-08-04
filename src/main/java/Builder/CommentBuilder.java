package Builder;

import Objects.Comment;
import Objects.Post;
import Objects.SaveHandle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentBuilder {
    public static Comment getCommentFromDatabase(int commentID){
        Comment comment = new Comment();
        String[] details = Database.Loader.getCommentDetails(commentID); //username, postID, comment, date

        comment.setCommentID(new SaveHandle(commentID));
        comment.setCommenter(UserBuilder.getUserFromDatabase(details[0]));
        comment.setPost(PostBuilder.getPostFromDatabase(Integer.parseInt(details[1])));
        comment.setMsg(details[2]);
        comment.setDate(LocalDateTime.parse(details[3].substring(0, details[3].length() - 2),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return comment;
    }

    public static Comment getCommentFromDatabase(int commentID, Post post){
        Comment comment = new Comment();
        String[] details = Database.Loader.getCommentDetails(commentID); //username, postID, comment, date

        comment.setCommentID(new SaveHandle(commentID));
        comment.setCommenter(UserBuilder.getUserFromDatabase(details[0]));
        comment.setPost(post);
        comment.setMsg(details[2]);
        comment.setDate(LocalDateTime.parse(details[3].substring(0, details[3].length() - 2),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return comment;
    }
}
