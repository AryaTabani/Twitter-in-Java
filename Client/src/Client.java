import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    private static User currentUser;

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345)) {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (currentUser == null) {
                    System.out.println("1. Sign Up");
                    System.out.println("2. Log In");
                    System.out.println("3. Exit");
                    int choice = Integer.parseInt(scanner.nextLine());
                    if (choice == 1) {
                        signUp(scanner);
                    } else if (choice == 2) {
                        logIn(scanner);
                    }else if (choice == 3) {
                        out.writeObject("Exit");
                        System.exit(0);
                    }
                } else {
                    System.out.println("1. View Posts");
                    System.out.println("2. Like a Post");
                    System.out.println("3. Save a Post");
                    System.out.println("4. View Liked Posts");
                    System.out.println("5. View Saved Posts");
                    System.out.println("6. Share a Post");
                    System.out.println("7. Delete a Post");
                    System.out.println("8. Follow a User");
                    System.out.println("9. View Profile");
                    System.out.println("10. Search Users");
                    System.out.println("11. Add Comment to Post");
                    System.out.println("12. View Likes of a Post");
                    System.out.println("13. View Saves of a Post");
                    System.out.println("14. View Comments of a Post");
                    System.out.println("15. View Search History");
                    System.out.println("16. View Notifications");
                    System.out.println("17. Log Out");
                    int choice = Integer.parseInt(scanner.nextLine());
                    switch (choice) {
                        case 1:
                            viewPosts();
                            break;
                        case 2:
                            likePost(scanner);
                            break;
                        case 3:
                            savePost(scanner);
                            break;
                        case 4:
                            viewLikedPosts();
                            break;
                        case 5:
                            viewSavedPosts();
                            break;
                        case 6:
                            sharePost(scanner);
                            break;
                        case 7:
                            deletePost(scanner);
                            break;
                        case 8:
                            followUser(scanner);
                            break;
                        case 9:
                            viewProfile(scanner, currentUser);
                            break;
                        case 10:
                            searchUsers(scanner);
                            break;
                        case 11:
                            addCommentToPost(scanner);
                            break;
                        case 12:
                            viewLikesOfPost(scanner);
                            break;
                        case 13:
                            viewSavesOfPost(scanner);
                            break;
                        case 14:
                            viewCommentsOfPost(scanner);
                            break;
                        case 15:
                            viewSearchHistory();
                            break;
                        case 16:
                            viewnotifs();
                            break;
                        case 17:
                            currentUser = null;
                            out.writeObject("logout");
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException| ClassCastException e) {
            e.printStackTrace();
        }
    }

    private static void signUp(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Birth Date: ");
        String birthDate = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Bio: ");
        String bio = scanner.nextLine();
        User user = new User(name, username, email, phoneNumber, birthDate, password, bio);
        out.writeObject("signup");
        out.writeObject(user);
        System.out.println(in.readObject());
    }

    private static void logIn(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        out.writeObject("login");
        out.writeObject(email);
        out.writeObject(password);
        User user = (User) in.readObject();
        if (user != null) {
            currentUser = user;
            System.out.println("Logged in successfully.");
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static void viewPosts() throws IOException, ClassNotFoundException, ClassCastException {
        out.writeObject("viewPosts");
        out.writeObject(currentUser);
        List<User> following = (List<User>) in.readObject();
        List<Post> posts = (List<Post>) in.readObject();
        System.out.println("Posts from users you follow:");
        for (Post post : posts) {
            for(User ck : following) {
                if (ck.getUsername().equals(post.getAuthor().getUsername())) {
                    System.out.println(post);
                    post.printed = true;
                    for (String comment : post.getComments()) {
                        System.out.println(" - " + comment);
                    }
                }
            }
        }
        System.out.println("Random posts:");
        for (Post post : posts) {
            if (!post.printed) {
                System.out.println(post);
                for (String comment : post.getComments()) {
                    System.out.println(" - " + comment);
                }
            }
            post.printed=false;
        }
    }

    private static void likePost(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the content of the post to like: ");
        String content = scanner.nextLine();
        out.writeObject("likePost");
        out.writeObject(content);
        out.writeObject(currentUser);
        System.out.println(in.readObject());
    }

    private static void savePost(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the content of the post to save: ");
        String content = scanner.nextLine();
        out.writeObject("savePost");
        out.writeObject(content);
        out.writeObject(currentUser);
        System.out.println(in.readObject());
    }

    private static void viewLikedPosts() throws IOException, ClassNotFoundException, ClassCastException {
        out.writeObject("viewLikedPosts");
        out.writeObject(currentUser);
        List<Post> LikedPosts = (List<Post>) in.readObject();
        if (LikedPosts.isEmpty()) {
            System.out.println("There is no posts");
        } else {
            System.out.println("Liked Posts:");
            for (Post post : LikedPosts) {
                System.out.println(post);
            }
        }
    }

    private static void viewSavedPosts() throws IOException, ClassNotFoundException, ClassCastException {
        out.writeObject("viewSavedPosts");
        out.writeObject(currentUser);
        List<Post> SavedPosts = (List<Post>) in.readObject();
        if (SavedPosts.isEmpty()) {
            System.out.println("There is no posts");
            return;
        } else {
            System.out.println("Saved Posts:");
            for (Post post : SavedPosts) {
                System.out.println(post);
            }
            return;
        }
    }

    private static void sharePost(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the content of the post to share: ");
        String content = scanner.nextLine();
        System.out.print("Can take comments:(Yes/No) ");
        String comment = scanner.nextLine();
        if (comment.equals("Yes")) {
            Post post = new Post(content, currentUser);
            post.setCommentable(true);
            out.writeObject("sharePost");
            out.writeObject(post);
            out.writeObject(currentUser);
            System.out.println(in.readObject());
        } else if (comment.equals("No")) {
            Post post = new Post(content, currentUser);
            post.setCommentable(false);
            out.writeObject("sharePost");
            out.writeObject(post);
            out.writeObject(currentUser);
            System.out.println(in.readObject());
        } else {
            Post post = new Post(content, currentUser);
            post.setCommentable(false);
            out.writeObject("sharePost");
            out.writeObject(post);
            out.writeObject(currentUser);
            System.out.println(in.readObject());
        }
    }

    private static void deletePost(Scanner scanner) throws IOException, ClassNotFoundException , ClassCastException{
        System.out.print("Enter the content of the post to delete: ");
        String content = scanner.nextLine();
        out.writeObject("deletePost");
        out.writeObject(content);
        out.writeObject(currentUser);
        System.out.println(in.readObject());
    }

    private static void followUser(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the username of the user to follow: ");
        String username = scanner.nextLine();
        out.writeObject("followUser");
        out.writeObject(username);
        out.writeObject(currentUser);
        System.out.println(in.readObject());
    }

    private static void viewProfile(Scanner scanner, User user) throws IOException, ClassNotFoundException, ClassCastException {
        out.writeObject("viewProfile");
        out.writeObject(user);
        System.out.println(user);
        System.out.println("Followers: " + in.readObject());
        System.out.println("Following: " + in.readObject());
        System.out.println("1. View Followers");
        System.out.println("2. View Following");
        System.out.println("3. View Posts");
        if (user != currentUser) {
            System.out.println("4. Follow/Unfollow");
        }
        System.out.println("5. Back");
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                viewFollowers(scanner, user);
                break;
            case 2:
                viewFollowing(scanner, user);
                break;
            case 3:
                viewUserPosts(scanner, user);
                break;
            case 4:
                out.writeObject("follow/unfollow");
                out.writeObject(user.getUsername());
                out.writeObject(currentUser);
                System.out.println(in.readObject());
                break;
        }
    }

    private static void viewFollowers(Scanner scanner, User user) throws IOException, ClassNotFoundException, ClassCastException {
        out.writeObject("viewFollowers");
        out.writeObject(user);
        List<User> result = (List<User>) in.readObject();
        for (User follower : result) {
            System.out.println(follower.getUsername());
        }
    }

    private static void viewFollowing(Scanner scanner, User user) throws IOException, ClassNotFoundException, ClassCastException {
        out.writeObject("viewFollowing");
        out.writeObject(user);
        List<User> result = (List<User>) in.readObject();
        for (User following : result) {
            System.out.println(following.getUsername());
        }
    }

    private static void viewUserPosts(Scanner scanner, User user) throws IOException, ClassNotFoundException , ClassCastException{
        out.writeObject("viewOwnPosts");
        out.writeObject(user);
        List<Post> userposts = (List<Post>) in.readObject();
        System.out.println("Posts:");
        for (Post post : userposts) {
            System.out.println(post.getContent());
            for (String comment : post.getComments()) {
                System.out.println(" - " + comment);
            }
        }
        if (user.equals(currentUser)) {
            System.out.print("Enter the content of the post to delete or 'back' to go back: ");
            String input = scanner.nextLine();
            if (!input.equals("back")) {
                out.writeObject("deletePost");
                out.writeObject(input);
                out.writeObject(currentUser);
                System.out.println(in.readObject());
            }
        }
    }

    private static void searchUsers(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the name or username to search: ");
        String query = scanner.nextLine();
        out.writeObject("searchUsers");
        out.writeObject(query);
        out.writeObject(currentUser);
        List<User> result = (List<User>) in.readObject();
        System.out.println("Search results:");
        for (User user : result) {
            System.out.println(user.getUsername());
        }
        System.out.print("Enter the username of the user to view profile or 'back' to go back: ");
        String input = scanner.nextLine();
        if (!input.equals("back")) {
            for (User user : result) {
                if (user.getUsername().equals(input)) {
                    viewProfile(scanner, user);
                    return;
                }
            }
            System.out.println("User not found.");
        }
    }

    private static void viewSearchHistory() throws IOException, ClassNotFoundException, ClassCastException {
        out.writeObject("searchHis");
        out.writeObject(currentUser);
        List<String> searchHistory = (List<String>) in.readObject();
        System.out.println("Search history:");
        for (String user : searchHistory) {
            System.out.println(user);
        }
    }

    private static void addCommentToPost(Scanner scanner) throws IOException, ClassNotFoundException , ClassCastException{
        System.out.print("Enter the content of the post to comment on: ");
        String content = scanner.nextLine();
        System.out.print("Enter your comment: ");
        String comment = scanner.nextLine();
        out.writeObject("addComment");
        out.writeObject(content);
        out.writeObject(comment);
        out.writeObject(currentUser);
        System.out.println(in.readObject());
    }

    private static void viewLikesOfPost(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the content of the post to view likes: ");
        String content = scanner.nextLine();
        out.writeObject("viewLikes");
        out.writeObject(content);
        out.writeObject(currentUser);
        List<User> following = (List<User>) in.readObject();
        List<String> followingusers = new ArrayList<>();
        for(User shit : following) {
            followingusers.add(shit.getUsername());
        }
        List<User> likedBy = (List<User>) in.readObject();
        System.out.println("Liked by:");
        if (likedBy != null) {
            for (User user : likedBy) {

                System.out.println(user.getUsername() + (followingusers.contains(user.getUsername()) ? " (Following)" : " (Not Following)"));
            }
        }
        else{
            System.out.println("No body has liked it.");
        }
    }

    private static void viewSavesOfPost(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the content of the post to view saves: ");
        String content = scanner.nextLine();
        out.writeObject("viewSaves");
        out.writeObject(content);
        out.writeObject(currentUser);
        List<User> following = (List<User>) in.readObject();
        List<String> followingusers = new ArrayList<>();
        for(User shit : following) {
            followingusers.add(shit.getUsername());
        }
        List<User> savedBy = (List<User>) in.readObject();
        System.out.println("Saved by:");
        if (savedBy != null) {
            for (User user : savedBy) {
                System.out.println(user.getUsername() + (followingusers.contains(user.getUsername()) ? " (Following)" : " (Not Following)"));
            }
        }
    }

    private static void viewCommentsOfPost(Scanner scanner) throws IOException, ClassNotFoundException, ClassCastException {
        System.out.print("Enter the content of the post to view comments: ");
        String content = scanner.nextLine();
        out.writeObject("viewComments");
        out.writeObject(content);
        List<String> comments = (List<String>) in.readObject();
        System.out.println("Comments:");
        if (comments != null) {
            for (String comment : comments) {
                System.out.println(comment);
            }
        }
    }
    private static void viewnotifs() throws IOException, ClassNotFoundException {
        out.writeObject("notif");
        out.writeObject(currentUser.getUsername());
        List<String> notifs = (List<String>) in.readObject();
        if(!notifs.isEmpty()){
            System.out.println("Notifications: ");
            for(String text : notifs){
                System.out.println(text);
            }
        }
    }
}
