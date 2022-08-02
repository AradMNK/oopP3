package Objects;

public class BusinessUser extends User {
    @Override
    public UserType getUserType(){return UserType.BUSINESS;}

    public void addViewToAccount() {
        for (Post post: getPosts()) addViewToAccount(post);
    }

    public void addViewToAccount(Post post) {
        Database.Changer.addViewForUser(post.getPostID().getHandle(), this.getUsername());
    }
}