import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static final String USERS_FILE = "users.dat";
    private static final String POSTS_FILE = "posts.dat";
    public static Map<String, User> users = new HashMap<>();
    public static List<Post> posts = new ArrayList<>();
    public static List<User> searchHistory = new ArrayList<>();


    public static void main(String[] args) {
        //clearFiles();
        loadDataFromFile();
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDataFromFile() {
        try {
            FileInputStream usersFileInputStream = new FileInputStream(USERS_FILE);
            ObjectInputStream usersObjectInputStream = new ObjectInputStream(usersFileInputStream);
            users = (Map<String, User>) usersObjectInputStream.readObject();
            usersObjectInputStream.close();
            usersFileInputStream.close();

            FileInputStream postsFileInputStream = new FileInputStream(POSTS_FILE);
            ObjectInputStream postsObjectInputStream = new ObjectInputStream(postsFileInputStream);
            posts = (List<Post>) postsObjectInputStream.readObject();
            postsObjectInputStream.close();
            postsFileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
            users = new HashMap<>();
            posts = new ArrayList<>();
        }
    }

    private static void saveDataToFile() {
        try {
            FileOutputStream usersFileOutputStream = new FileOutputStream(USERS_FILE);
            ObjectOutputStream usersObjectOutputStream = new ObjectOutputStream(usersFileOutputStream);
            usersObjectOutputStream.writeObject(users);
            usersObjectOutputStream.close();
            usersFileOutputStream.close();

            FileOutputStream postsFileOutputStream = new FileOutputStream(POSTS_FILE);
            ObjectOutputStream postsObjectOutputStream = new ObjectOutputStream(postsFileOutputStream);
            postsObjectOutputStream.writeObject(posts);
            postsObjectOutputStream.close();
            postsFileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearFiles() {
        try {
            FileOutputStream usersFileOutputStream = new FileOutputStream(USERS_FILE);
            usersFileOutputStream.close();
            FileOutputStream postsFileOutputStream = new FileOutputStream(POSTS_FILE);
            postsFileOutputStream.close();
            System.out.println("Files have been cleared.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class ClientHandler extends Thread {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                String action;
                while ((action = (String) in.readObject()) != null) {
                    switch (action) {
                        case "signup":
                            handleSignUp();
                            break;
                        case "login":
                            handleLogIn();
                            break;
                        case "viewPosts":
                            handleViewPosts();
                            break;
                        case "viewLikedPosts":
                            handleViewLikedPosts();
                            break;
                        case "viewSavedPosts":
                            handleViewSavedPosts();
                            break;
                        case "viewOwnPosts":
                            handleViewOwnPosts();
                            break;
                        case "likePost":
                            handleLikePost();
                            break;
                        case "savePost":
                            handleSavePost();
                            break;
                        case "sharePost":
                            handleSharePost();
                            break;
                        case "deletePost":
                            handleDeletePost();
                            break;
                        case "followUser":
                            handleFollowUser();
                            break;
                        case "viewProfile":
                            handleViewProfile();
                            break;
                        case "searchUsers":
                            handleSearchUsers();
                            break;
                        case "addComment":
                            handleAddComment();
                            break;
                        case "viewLikes":
                            handleViewLikes();
                            break;
                        case "viewSaves":
                            handleViewSaves();
                            break;
                        case "viewComments":
                            handleViewComments();
                            break;
                        case "follow/unfollow":
                            Handler();
                            break;
                        case "viewFollowers":
                            viewFollowers();
                            break;
                        case "viewFollowing":
                            viewFollowing();
                            break;
                        case "searchHis":
                            Serachhis();
                            break;
                        case "notif":
                            viewNotifications();
                            break;
                        case "logout":
                            break;
                        case "Exit":
                            System.exit(0);
                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleSignUp() throws IOException, ClassNotFoundException {
            User user = (User) in.readObject();
            users.put(user.getEmail(), user);
            out.reset();
            saveDataToFile();
            out.writeObject("Account created successfully.");
            out.reset();
        }

        private void viewNotifications() throws IOException, ClassNotFoundException {
            String username = (String) in.readObject();
            User user = null;
            for (User user1 : users.values()) {
                if (user1.getUsername().equals(username)) {
                    user = user1;
                }
            }
            List<String> nots = new ArrayList<>();
            for (String not : user.notifications) {
                nots.add(not);
            }
            out.writeObject(nots);
            out.reset();

        }

        private void handleLogIn() throws IOException, ClassNotFoundException {

            String email = (String) in.readObject();
            String password = (String) in.readObject();
            User user = users.get(email);
            if (user != null && user.getPassword().equals(password)) {
                out.writeObject(user);
                out.reset();
            } else {
                out.writeObject(null);
                out.reset();
            }
        }

        private void handleViewPosts() throws IOException, ClassNotFoundException {
            User user = (User) in.readObject();
            List<User> result2 = new ArrayList<>();
            for (User user1 : users.values()) {
                for (User checker : user1.followers) {
                    if (checker.getUsername().equals(user.getUsername())) {
                        result2.add(user1);
                    }
                }
            }
            out.writeObject(result2);
            out.reset();
            out.writeObject(posts);
            out.reset();
        }

        private void handleViewLikedPosts() throws IOException, ClassNotFoundException {

            User user = (User) in.readObject();
            List<Post> result = new ArrayList<>();
            for (Post post : posts) {
                for (User user1 : post.getLikedBy()) {
                    if (user1.getUsername().equals(user.getUsername())) {
                        result.add(post);
                    }
                }
            }
//            for (Post post : user.getLikedPosts()) {
//                result.add(post);
//            }
            out.writeObject(result);
            out.reset();
        }

        private void handleViewSavedPosts() throws IOException, ClassNotFoundException {
            User user = (User) in.readObject();
            List<Post> result = new ArrayList<>();
            for (Post post : posts) {
                for (User user1 : post.getSavedBy()) {
                    if (user1.getUsername().equals(user.getUsername())) {
                        result.add(post);
                    }
                }
            }
//            for (Post post : user.getSavedPosts()) {
//                result.add(post);
//            }
            out.writeObject(result);
            out.reset();
        }

        private void handleViewOwnPosts() throws IOException, ClassNotFoundException {

            User user = (User) in.readObject();
            List<Post> result = new ArrayList<>();
            for (Post post : user.getUserPosts()) {
                result.add(post);
            }
            out.writeObject(result);
            out.reset();
        }


        private void handleSavePost() throws IOException, ClassNotFoundException {

            String content = (String) in.readObject();
            User user = (User) in.readObject();
            for (Post post : posts) {
                if (post.getContent().equals(content)) {
                    user.savePost(post, user);
                    out.writeObject("Post saved.");
                    out.reset();
                    saveDataToFile();
                    return;
                }
            }
            out.writeObject("Post not found.");
            out.reset();
        }

        private void handleSharePost() throws IOException, ClassNotFoundException {

            Post post = (Post) in.readObject();
            User user = (User) in.readObject();
            posts.add(post);
            user.addPost(post);
            out.writeObject("Post shared.");
            out.reset();
            saveDataToFile();
        }

        private void handleDeletePost() throws IOException, ClassNotFoundException {

            String content = (String) in.readObject();
            User user = (User) in.readObject();
            for (Post post : posts) {
                if (post.getContent().equals(content) && post.getAuthor().getUsername().equals(user.getUsername())) {
                    posts.remove(post);
                    user.deletePost(post);
                    for (User u : users.values()) {
                        u.getSavedPosts().remove(post);
                    }
                    out.writeObject("Post deleted.");
                    out.reset();
                    saveDataToFile();
                    return;
                }
            }
            out.writeObject("Post not found.");
            out.reset();
        }

        private void handleFollowUser() throws IOException, ClassNotFoundException {
            String username = (String) in.readObject();
            User user = (User) in.readObject();
            List<User> result2 = new ArrayList<>();
            for (User user1 : users.values()) {
                for (User checker : user1.followers) {
                    if (checker.getUsername().equals(user.getUsername())) {
                        result2.add(user1);
                    }
                }
            }
            for (User u : users.values()) {
                if (u.getUsername().equals(username)) {
                    if (!result2.contains(u)) {
                        user.following.add(u);
                        u.followers.add(user);
                        u.notifications.add(user.getUsername() + " started following you!");
                        out.writeObject("You are now following " + u.getUsername());
                        out.reset();
                        saveDataToFile();
                        return;
                    } else {
                        out.writeObject("You are already following this user");
                        return;
                    }
                }
            }
            out.writeObject("User not found.");
            out.reset();
        }

        private void Handler() throws IOException, ClassNotFoundException {

            String username = (String) in.readObject();
            User user = (User) in.readObject();
            List<User> result2 = new ArrayList<>();
            for (User user1 : users.values()) {
                for (User checker : user1.followers) {
                    if (checker.getUsername().equals(user.getUsername())) {
                        result2.add(user1);
                    }
                }
            }
            for (User u : users.values()) {
                if (u.getUsername().equals(username)) {
                    if (result2.contains(u)) {
                        user.following.remove(u);
                        u.followers.remove(user);
                        out.writeObject("You have unfollowed " + u.getUsername());
                        out.reset();
                        saveDataToFile();
                    } else {
                        user.following.add(u);
                        u.followers.add(user);
                        u.notifications.add(user.getUsername() + " started following you!");
                        out.writeObject("You are now following " + u.getUsername());
                        out.reset();
                        saveDataToFile();
                    }
                    return;
                }
            }
            out.writeObject("User not found.");
            out.reset();

        }

        private void handleViewProfile() throws IOException, ClassNotFoundException {

            User user = (User) in.readObject();
            out.writeObject(user.getFollowers().size());
            out.reset();
            List<User> result10 = new ArrayList<>();
            for (User user1 : users.values()) {
                for (User checker : user1.followers) {
                    if (checker.getUsername().equals(user.getUsername())) {
                        result10.add(user1);
                    }
                }
            }
            out.writeObject(result10.size());
            out.reset();
        }

        private void handleSearchUsers() throws IOException, ClassNotFoundException {
            String query = (String) in.readObject();
            User user1 = (User) in.readObject();
            List<User> result = new ArrayList<>();
            for(User use:users.values()){
                if(use.getUsername().equals(user1.getUsername())){

                }
            }
            for (User user : users.values()) {
                if (user.getUsername().contains(query) || user.getName().contains(query)) {
                    result.add(user);
                    for(User use:users.values()){
                        if(use.getUsername().equals(user1.getUsername())){
                            use.searchHistory.add(user.getUsername());
                        }
                    }

                }
            }
            out.writeObject(result);
            out.reset();
        }

        private void Serachhis() throws IOException, ClassNotFoundException {
            User user = (User) in.readObject();
            List<String> result10 = new ArrayList<>();
            for (User use : users.values()) {
                if (use.getUsername().equals(user.getUsername())) {
                    for (String name : use.searchHistory) {
                        result10.add(name);
                    }
                }

            }
            out.writeObject(result10);
            out.reset();
        }

        private void handleLikePost() throws IOException, ClassNotFoundException {
            String content = (String) in.readObject();
            User user = (User) in.readObject();
            for (Post post : posts) {
                if (post.getContent().equals(content)) {
                    user.likePost(post, user);
                    String username = post.getAuthor().getUsername();
                    for (User use : users.values()) {
                        if (use.getUsername().equals(username)) {
                            use.notifications.add(user.getName() + " liked your post!");
                            break;
                        }
                    }
                    out.writeObject("Post liked.");
                    out.reset();
                    saveDataToFile();
                    return;
                }
            }
            out.writeObject("Post not found.");
            out.reset();
        }

        private void handleAddComment() throws IOException, ClassNotFoundException {

            String content = (String) in.readObject();
            String comment = (String) in.readObject();
            User user = (User) in.readObject();
            for (Post post : posts) {
                if (post.getContent().equals(content)) {
                    if (post.isCommentable()) {
                        post.addComment(comment);
                        String username = post.getAuthor().getUsername();
                        for (User use : users.values()) {
                            if (use.getUsername().equals(username)) {
                                use.notifications.add((username) + " added comment to your post! " + "post => " + post.getContent());
                                break;
                            }

                        }
                        out.writeObject("Comment added.");
                        out.reset();
                        saveDataToFile();
                        return;
                    } else {
                        out.writeObject("You cant add comment to this post");
                        out.reset();
                        return;
                    }
                }
            }
            out.writeObject("Post not found.");
            out.reset();
        }


        private void handleViewLikes() throws IOException, ClassNotFoundException {
            String content = (String) in.readObject();
            User user = (User) in.readObject();
            List<User> result2 = new ArrayList<>();
            for (User user1 : users.values()) {
                for (User checker : user1.followers) {
                    if (checker.getUsername().equals(user.getUsername())) {
                        result2.add(user1);
                    }
                }
            }
            out.writeObject(result2);
            out.reset();
            for (Post post : posts) {
                if (post.getContent().equals(content)) {
                    out.writeObject(post.getLikedBy());
                    out.reset();
                    return;
                }
            }
            out.writeObject(null);
            out.reset();
        }

        private void handleViewSaves() throws IOException, ClassNotFoundException {

            String content = (String) in.readObject();
            User user = (User) in.readObject();
            List<User> result2 = new ArrayList<>();
            for (User user1 : users.values()) {
                for (User checker : user1.followers) {
                    if (checker.getUsername().equals(user.getUsername())) {
                        result2.add(user1);
                    }
                }
            }
            out.writeObject(result2);
            out.reset();
            for (Post post : posts) {
                if (post.getContent().equals(content)) {
                    out.writeObject(post.getSavedBy());
                    out.reset();
                    return;
                }
            }
            out.writeObject(null);
            out.reset();
        }

        private void viewFollowers() throws IOException, ClassNotFoundException {

            User user = (User) in.readObject();
            List<User> result = new ArrayList<>();
            for (User user2 : user.getFollowers()) {
                result.add(user2);
            }
            out.writeObject(result);
            out.reset();
        }

        private void viewFollowing() throws IOException, ClassNotFoundException {

            User user = (User) in.readObject();
            List<User> result2 = new ArrayList<>();
            for (User user1 : users.values()) {
                for (User checker : user1.followers) {
                    if (checker.getUsername().equals(user.getUsername())) {
                        result2.add(user1);
                    }
                }
            }
            out.writeObject(result2);
            out.reset();
        }


        private void handleViewComments() throws IOException, ClassNotFoundException {

            String content = (String) in.readObject();
            for (Post post : posts) {
                if (post.getContent().equals(content)) {
                    out.writeObject(post.getComments());
                    out.reset();
                    return;
                }
            }
            out.writeObject(null);
            out.reset();
        }


    }
}
