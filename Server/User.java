import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String username;
    private String email;
    private String phoneNumber;
    private String birthDate;
    private String password;
    private String bio;
    private List<Post> likedPosts;
    private List<Post> savedPosts;
    public List<User> following;
    public List<User> followers;
    public List<Post> userPosts;
    public List<String> notifications;
    public  List<String> searchHistory;

    public User(String name, String username, String email, String phoneNumber, String birthDate, String password, String bio) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.password = password;
        this.bio = bio;
        this.likedPosts = new ArrayList<>();
        this.savedPosts = new ArrayList<>();
        this.following = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.userPosts = new ArrayList<>();
        this.notifications = new ArrayList<>();
        this.searchHistory = new ArrayList<>();
        notifications.add("Notifications: ");
        searchHistory.add("Search History: ");
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public List<Post> getSavedPosts() {
        return savedPosts;
    }

    public List<User> getFollowing() {
        return following;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public List<Post> getUserPosts() {
        return userPosts;
    }


    public void addPost(Post post) {
        userPosts.add(post);
    }



    public void likePost(Post post, User user) {
        if (!likedPosts.contains(post)) {
            likedPosts.add(post);
            post.like(user);
        }
    }

    public void unlikePost(Post post) {
        if (likedPosts.contains(post)) {
            likedPosts.remove(post);
            post.unlike();
        }
    }

    public void savePost(Post post, User user) {
        if (!savedPosts.contains(post)) {
            savedPosts.add(post);
            post.save(user);
        }
    }

    public void deletePost(Post post) {
        userPosts.remove(post);
    }



    public boolean isFollowing(User user) {
        return following.contains(user);
    }

    public boolean isFollowing2(String username) {
        for (User followedUser : following) {
            if (followedUser.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" + "name='" + name + '\'' + ", username='" + username + '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", birthDate='" + birthDate + '\'' + ", bio='" + bio + '\'' + '}';
    }


}

