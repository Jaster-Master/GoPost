package net.htlgkr.gopost.server;

import net.htlgkr.gopost.data.*;
import net.htlgkr.gopost.database.DBObject;
import net.htlgkr.gopost.packet.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection implements Runnable {

    private final Server server;
    private final Socket clientSocket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ClientConnection(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            this.writer = new ObjectOutputStream(clientSocket.getOutputStream());
            this.reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!clientSocket.isClosed()) {
            try {
                Object readObject = reader.readObject();
                System.out.println("Object received");
                if (!(readObject instanceof Packet packet)) continue;
                System.out.println("Packet received: " + packet.getCommand());
                if (readObject instanceof UserPacket userPacket) {
                    handleBlockPacket(userPacket);
                } else if (readObject instanceof LoginPacket loginPacket) {
                    handleLoginPacket(loginPacket);
                } else if (readObject instanceof PostPacket postPacket) {
                    handlePostPacket(postPacket);
                } else if (readObject instanceof ProfilePacket profilePacket) {
                    handleProfilePacket(profilePacket);
                } else if (readObject instanceof ReportPacket reportPacket) {
                    handleReportPacket(reportPacket);
                } else if (readObject instanceof StoryPacket storyPacket) {
                    handleStoryPacket(storyPacket);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                if (e instanceof EOFException) {
                    return;
                }
            }
        }
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleStoryPacket(StoryPacket storyPacket) {
        String command = storyPacket.getCommand();
        Story story = storyPacket.getStory();
        switch (command) {
            case "uploadStory" -> {
                System.out.println("uploadStory");
                String insertStatement = "INSERT INTO Story(GoUserId,StoryDateTime,StoryURL,Longitude,Latitude) VALUES(?,?,?,?,?)";
                Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                        story.getFromUser().getUserId(),
                        story.getCreatedDate(),
                        story.getUrl(),
                        story.getLocation().getLongitude(),
                        story.getLocation().getLatitude());
                insertStatement = "INSERT INTO StoryMedia(Story,StoryId) VALUES(?,?)";
                for (int i = 0; i < story.getStory().size(); i++) {
                    Server.DB_HANDLER.executeStatementsOnDB(insertStatement, story.getStory().get(i), Server.DB_HANDLER.readFromDB(
                            "SELECT StoryId FROM Story WHERE GoUserId = ? AND StoryDateTime = ? FETCH FIRST ROW ONLY",
                            story.getFromUser().getUserId(), story.getCreatedDate()).get(0).getLong());
                }
            }
            case "requestStory" -> {
                Long storyId = Server.DB_HANDLER.readFromDB(
                        "SELECT StoryId FROM Story WHERE GoUserId = ? AND StoryDateTime = ? FETCH FIRST ROW ONLY",
                        story.getFromUser().getUserId(), story.getCreatedDate()).get(0).getLong();
                List<DBObject> storyMedias = Server.DB_HANDLER.readFromDB(
                        "SELECT Story FROM StoryMedia WHERE StoryId = ?",
                        storyId);
                Story newStory = new Story();
                for (DBObject storyMedia : storyMedias) {
                    byte[] storyBytes = storyMedia.getBlob();
                    newStory.addToStory(storyBytes);
                }
                sendPacket(new StoryPacket("answer", story.getFromUser(), newStory));
            }
        }
    }

    private void handleReportPacket(ReportPacket reportPacket) {
        String command = reportPacket.getCommand();
        if ("addReport".equals(command)) {
            System.out.println("addReport");
            User reportedUser = Server.DB_HANDLER.getUserFromName(reportPacket.getUserName());
            String insertStatement = "INSERT INTO ReportedGoUser(Reported,Reporter,Reason) VALUES(?,?,?)";
            Server.DB_HANDLER.executeStatementsOnDB(insertStatement, reportPacket.getSentByUser().getUserId(), reportedUser.getUserId(), reportPacket.getReason());
        }
    }

    private void handleProfilePacket(ProfilePacket profilePacket) {
        String command = profilePacket.getCommand();
        if ("requestProfile".equals(command)) {
            System.out.println("requestProfile");
            long userId = profilePacket.getProfile().getUserId();
            String selectUserStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserDescription, GoUserIsPrivate, GoUserDateTime, GoUserProfilePicture FROM GoUser WHERE GoUserId = ?";
            List<DBObject> userResult = Server.DB_HANDLER.readFromDB(selectUserStatement, userId, "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;String", "7;Boolean", "8;Timestamp", "9:Blob");
            Post[] posts = getPosts(userId);
            Post[] savedPosts = getSavedPosts(userId);
            User[] friends = getFriends(userId);
            User[] followers = getFollowers(userId);
            User[] followed = getFollowed(userId);
            Story[] stories = getStories(userId);

            String userName = userResult.get(1).getString();
            String profileName = userResult.get(2).getString();
            String email = userResult.get(3).getString();
            String password = userResult.get(4).getString();
            String description = userResult.get(5).getString();
            boolean isPrivate = userResult.get(6).getBoolean();
            LocalDateTime createdDate = userResult.get(7).getTimestamp().toLocalDateTime();
            byte[] profilePicture = userResult.get(8).getBlob();

            Profile requestedProfile = new Profile(userId, userName, profileName, email, password, description, isPrivate,
                    createdDate, profilePicture, posts, stories, savedPosts, friends, followers, followed);

            sendPacket(new ProfilePacket("answer", null, requestedProfile));
        }
    }

    private Story[] getStories(long userId) {
        String selectStoryStatement = "SELECT StoryId, GoUserId, StoryDateTime, StoryURL, Longitude, Latitude FROM Story WHERE GoUserId = ?";
        List<DBObject> storiesResult = Server.DB_HANDLER.readFromDB(selectStoryStatement, userId, "1;BigInt", "2;BigInt", "3;Timestamp", "4;String", "5;Double", "6;Double");
        List<Story> stories = new ArrayList<>();
        for (int i = 0; i < storiesResult.size(); i += 6) {
            stories.add(getStory(storiesResult, i));
        }
        return (Story[]) stories.toArray();
    }

    private Story getStory(List<DBObject> postsResult, int startIndex) {
        long storyId = postsResult.get(startIndex).getLong();

        List<byte[]> storyMediaBytes = getStoryMedia(storyId);
        User storyUser = Server.DB_HANDLER.getUserFromId(postsResult.get(startIndex + 1).getLong());
        LocalDateTime storyCreatedDate = postsResult.get(startIndex + 2).getTimestamp().toLocalDateTime();
        String storyUrl = postsResult.get(startIndex + 3).getString();
        GoLocation storyLocation = new GoLocation(postsResult.get(startIndex + 4).getDouble(), postsResult.get(startIndex + 5).getDouble());

        Story story = new Story(storyMediaBytes, storyUrl, storyUser, storyCreatedDate, storyLocation);
        return story;
    }

    private List<byte[]> getStoryMedia(long storyId) {
        String selectStoryMediaStatement = "SELECT Story FROM StoryMedia WHERE StoryId = ?";
        List<DBObject> storyMediaResult = Server.DB_HANDLER.readFromDB(selectStoryMediaStatement, storyId, "1;Blob");
        List<byte[]> storyMediaBytes = new ArrayList<>();
        for (DBObject dbObject : storyMediaResult) {
            storyMediaBytes.add(dbObject.getBlob());
        }
        return storyMediaBytes;
    }

    private User[] getFollowed(long userId) {
        String selectedFollowedStatement = "SELECT GoUser FROM Follower WHERE GoUserFollower = ?";
        List<DBObject> followedResult = Server.DB_HANDLER.readFromDB(selectedFollowedStatement, userId, "1;BigInt");
        List<User> followed = new ArrayList<>();
        for (DBObject dbObject : followedResult) {
            long followedUserId = dbObject.getLong();
            followed.add(Server.DB_HANDLER.getUserFromId(followedUserId));
        }
        return (User[]) followed.toArray();
    }

    private User[] getFollowers(long userId) {
        String selectedFollowersStatement = "SELECT GoUserFollower FROM Follower WHERE GoUser = ?";
        List<DBObject> followerResult = Server.DB_HANDLER.readFromDB(selectedFollowersStatement, userId, "1;BigInt");
        List<User> follower = new ArrayList<>();
        for (DBObject dbObject : followerResult) {
            long followerUserId = dbObject.getLong();
            follower.add(Server.DB_HANDLER.getUserFromId(followerUserId));
        }
        return (User[]) follower.toArray();
    }

    private User[] getFriends(long userId) {
        String selectedFriendsStatement = "SELECT GoUserFriend FROM Friend WHERE GoUser = ?";
        List<DBObject> friendsResult = Server.DB_HANDLER.readFromDB(selectedFriendsStatement, userId, "1;BigInt");
        List<User> friends = new ArrayList<>();
        for (DBObject dbObject : friendsResult) {
            long friendUserId = dbObject.getLong();
            friends.add(Server.DB_HANDLER.getUserFromId(friendUserId));
        }
        return (User[]) friends.toArray();
    }

    private Post[] getSavedPosts(long userId) {
        String selectSavedPostsStatement = "SELECT PostId FROM SavedPost WHERE GoUserId = ?";
        List<DBObject> savedPostsResult = Server.DB_HANDLER.readFromDB(selectSavedPostsStatement, userId, "1;BigInt");
        List<Post> savedPosts = new ArrayList<>();
        for (int i = 0; i < savedPostsResult.size(); i++) {
            long postId = savedPostsResult.get(i).getLong();
            String selectPostsStatement = "SELECT PostId, GoUserId, PostURL, PostDateTime, Longitude, Latitude, PostDescription FROM Post WHERE PostId = ?";
            List<DBObject> postsResult = Server.DB_HANDLER.readFromDB(selectPostsStatement, postId, "1;BigInt", "2;BigInt", "3;String", "4;Timestamp", "5;Double", "6;Double", "7;String");
            savedPosts.add(getPost(postsResult, 0));
        }
        return (Post[]) savedPosts.toArray();
    }

    private Post getPost(List<DBObject> postsResult, int startIndex) {
        long postId = postsResult.get(startIndex).getLong();

        List<byte[]> postMediaBytes = getPostMediaBytes(postId);
        User[] likes = getLikes(postId);
        Comment[] comments = getComments(postId);
        User[] marks = getMarks(postId);

        User postUser = Server.DB_HANDLER.getUserFromId(postsResult.get(startIndex + 1).getLong());
        String postUrl = postsResult.get(startIndex + 2).getString();
        LocalDateTime postCreatedDate = postsResult.get(startIndex + 3).getTimestamp().toLocalDateTime();
        GoLocation postLocation = new GoLocation(postsResult.get(startIndex + 4).getDouble(), postsResult.get(startIndex + 5).getDouble());
        String postDescription = postsResult.get(startIndex + 6).getString();

        return new Post(postMediaBytes, postUser, postUrl, postCreatedDate, postLocation, postDescription, likes, comments, marks);
    }

    private Post[] getPosts(long userId) {
        String selectPostsStatement = "SELECT PostId, GoUserId, PostURL, PostDateTime, Longitude, Latitude, PostDescription FROM Post WHERE GoUserId = ?";
        List<DBObject> postsResult = Server.DB_HANDLER.readFromDB(selectPostsStatement, userId, "1;BigInt", "2;BigInt", "3;String", "4;Timestamp", "5;Double", "6;Double", "7;String");
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < postsResult.size(); i += 7) {
            posts.add(getPost(postsResult, i));
        }
        return (Post[]) posts.toArray();
    }

    private List<byte[]> getPostMediaBytes(long postId) {
        String selectPostMediaStatement = "SELECT Post FROM PostMedia WHERE PostId = ?";
        List<DBObject> postMediaResult = Server.DB_HANDLER.readFromDB(selectPostMediaStatement, postId, "1;Blob");
        List<byte[]> postMediaBytes = new ArrayList<>();
        for (DBObject dbObject : postMediaResult) {
            postMediaBytes.add(dbObject.getBlob());
        }
        return postMediaBytes;
    }

    private User[] getLikes(long postId) {
        String selectLikesStatement = "SELECT GoUserId FROM GoLike WHERE PostId = ?";
        List<DBObject> likesResult = Server.DB_HANDLER.readFromDB(selectLikesStatement, postId, "1;BigInt");
        return (User[]) likesResult.stream().map(l -> Server.DB_HANDLER.getUserFromId(l.getLong())).toArray();
    }

    private Comment[] getComments(long postId) {
        String selectCommentsStatement = "SELECT GoUserId, GoUserComment, CommentDateTime,  FROM PostComment WHERE PostId = ?";
        List<DBObject> commentsResult = Server.DB_HANDLER.readFromDB(selectCommentsStatement, postId, "1;String", "2;Timestamp", "3;BigInt");
        List<Comment> comments = new ArrayList<>();
        for (int j = 0; j < commentsResult.size(); j += 3) {
            User commentUser = Server.DB_HANDLER.getUserFromId(commentsResult.get(j).getLong());
            String commentContent = commentsResult.get(j + 1).getString();
            LocalDateTime commentCreatedDate = commentsResult.get(j + 2).getTimestamp().toLocalDateTime();
            Comment nextComment = new Comment(commentUser, commentContent, commentCreatedDate);
            comments.add(nextComment);
        }
        return (Comment[]) comments.toArray();
    }

    private User[] getMarks(long postId) {
        String selectMarksStatement = "SELECT GoUserId FROM GoMark WHERE PostId = ?";
        List<DBObject> marksResult = Server.DB_HANDLER.readFromDB(selectMarksStatement,
                postId, "1;BigInt");
        return (User[]) marksResult.stream().map(l -> Server.DB_HANDLER.getUserFromId(l.getLong())).toArray();
    }

    private void handlePostPacket(PostPacket postPacket) {
        String command = postPacket.getCommand();
        Post post = postPacket.getPost();
        switch (command) {
            case "uploadPost" -> {
                System.out.println("uploadPost");
                String insertStatement = "INSERT INTO Post(GoUserId,StoryURL,StoryDateTime,Longitude,Latitude,PostDescription) VALUES(?,?,?,?,?,?)";
                Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                        post.getFromUser().getUserId(),
                        post.getUrl(),
                        post.getCreatedDate(),
                        post.getLocation().getLongitude(),
                        post.getLocation().getLatitude(),
                        post.getDescription());
                insertStatement = "INSERT INTO PostMedia(Post,PostId) VALUES(?,?)";
                for (int i = 0; i < post.getPictures().size(); i++) {
                    Server.DB_HANDLER.executeStatementsOnDB(insertStatement, post.getPictures().get(i), Server.DB_HANDLER.readFromDB(
                            "SELECT PostId FROM Post WHERE GoUserId = ? AND PostDateTime = ? FETCH FIRST ROW ONLY",
                            post.getFromUser().getUserId(), post.getCreatedDate()).get(0).getLong());
                }
            }
            case "requestPost" -> {
                Long postId = Server.DB_HANDLER.readFromDB(
                        "SELECT PostId FROM Post WHERE GoUserId = ? AND PostDateTime = ? FETCH FIRST ROW ONLY",
                        post.getFromUser().getUserId(), post.getCreatedDate()).get(0).getLong();
                List<DBObject> postMedias = Server.DB_HANDLER.readFromDB(
                        "SELECT Post FROM PostMedia WHERE PostId = ?",
                        postId);
                Post newPost = new Post();
                for (DBObject storyMedia : postMedias) {
                    byte[] storyBytes = storyMedia.getBlob();
                    newPost.addToPost(storyBytes);
                }
                sendPacket(new PostPacket("answer", post.getFromUser(), newPost));
            }
        }
    }

    private void handleLoginPacket(LoginPacket loginPacket) {
        String command = loginPacket.getCommand();
        switch (command) {
            case "firstTimeLogin":
                System.out.println("FirstTimeLogin");
                if (checkIfUserNameAlreadyExists(loginPacket.getUserName())) {
                    sendPacket(new Packet("userAlreadyExists", null));
                }
                String insertStatement = "INSERT INTO GoUser(GoUserName,GoProfileName,GoUserEmail,GoUserPassword,GoUserIsPrivate,GoUserDateTime) VALUES(?,?,?,?,?,?)";
                Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                        loginPacket.getUserName(),
                        loginPacket.getProfileName(),
                        loginPacket.getEmail(),
                        loginPacket.getPassword(),
                        false,
                        Timestamp.valueOf(LocalDateTime.now()));
            case "checkIfCorrectPassword":
                System.out.println("checkIfCorrectPassword");
                String selectStatement = "SELECT GoUserId, GoUserProfilePicture FROM GoUser WHERE GoUserName = ? AND GoUserPassword = ?";
                List<DBObject> result = Server.DB_HANDLER.readFromDB(selectStatement, loginPacket.getUserName(), loginPacket.getPassword(), "1;BigInt", "2;Blob");
                setUserId(result.get(0).getLong());
                server.addClient(this);

                User user = new User(userId, loginPacket.getUserName(), loginPacket.getProfileName(), loginPacket.getEmail(), loginPacket.getPassword(), result.get(1).getBlob());
                sendPacket(new Packet("answer", user));
        }
    }

    private boolean checkIfUserNameAlreadyExists(String userName) {
        String selectStatement = "SELECT GoUserName FROM GoUser";
        List<DBObject> result = Server.DB_HANDLER.readFromDB(selectStatement, "1;String");
        for (DBObject dbObject : result) {
            if (dbObject.getString().equals(userName)) {
                return false;
            }
        }
        return true;
    }

    private void handleBlockPacket(UserPacket userPacket) {
        String command = userPacket.getCommand();
        if ("addBlock".equals(command)) {
            System.out.println("addBlock");
            User blockedUser = Server.DB_HANDLER.getUserFromName(userPacket.getUserName());
            String insertStatement = "INSERT INTO BlockedGoUser(Blocker,Blocked) VALUES(?,?)";
            Server.DB_HANDLER.executeStatementsOnDB(insertStatement, userPacket.getSentByUser().getUserId(), blockedUser.getUserId());
        }
    }

    private boolean sendPacket(Packet packet) {
        try {
            writer.writeObject(packet);
            writer.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
