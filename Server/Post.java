import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private User author;
    private int likeCount;
    private int saveCount;
    private List<String> comments;
    private boolean commentable = true;
    public boolean printed =false;
    private List<User> likedBy;
    private List<User> savedBy;


    public Post(String content, User author) {
        this.content = content;
        this.author = author;
        this.likeCount = 0;
        this.saveCount = 0;
        this.comments = new ArrayList<>();
        this.likedBy = new ArrayList<>();
        this.savedBy = new ArrayList<>();

    }

    public boolean isCommentable() {
        return commentable;
    }

    public void setCommentable(boolean commentable) {
        this.commentable = commentable;
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getSaveCount() {
        return saveCount;
    }

    public void like(User user) {
        likeCount++;
        likedBy.add(user);
    }
    public List<User> getLikedBy() {
        return likedBy;
    }

    public List<User> getSavedBy() {
        return savedBy;
    }

    public void unlike() {
        if (likeCount > 0) {
            likeCount--;
        }
    }

    public void save(User user) {
        saveCount++;
        this.savedBy.add(user);
    }

    public void unsave() {
        if (saveCount > 0) {
            saveCount--;
        }
    }
    public void addComment(String comment) {
        comments.add(comment);
    }

    public List<String> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return content + " (by " + author.getUsername() + ", likes: " + likeCount + ", saves: " + saveCount + ", comments: " + comments.size() + ")";
    }
}
