package net.htlgkr.gopost.server;

import net.htlgkr.gopost.data.*;
import net.htlgkr.gopost.database.DBObject;
import net.htlgkr.gopost.packet.*;
import net.htlgkr.gopost.util.Command;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection implements Runnable {

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

    public ClientConnection(Socket clientSocket) {
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
                if (e instanceof EOFException || e instanceof SocketException) {
                    return;
                }
            }
        }
    }

    private synchronized void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void handleStoryPacket(StoryPacket storyPacket) {
        Command command = storyPacket.getCommand();
        Story story = storyPacket.getStory();
        switch (command) {
            case UPLOAD_STORY -> {
                System.out.println("uploadStory");
                if (story.getStory() == null || story.getStory().size() <= 0) {
                    sendPacket(new Packet(Command.NO_PICTURES, null));
                    return;
                }
                story.setCreatedDate(LocalDateTime.now());
                String insertStatement = "INSERT INTO Story(GoUserId,StoryDateTime,StoryURL,Longitude,Latitude) VALUES(?,?,?,?,?)";
                double locationLongitude = story.getLocation() == null ? 0 : story.getLocation().getLongitude();
                double locationLatitude = story.getLocation() == null ? 0 : story.getLocation().getLatitude();
                Timestamp createdTimestamp = Timestamp.valueOf(story.getCreatedDate());
                Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                        story.getFromUser().getUserId(),
                        createdTimestamp,
                        story.getUrl(),
                        locationLongitude,
                        locationLatitude);
                insertStatement = "INSERT INTO StoryMedia(Story,StoryId) VALUES(?,?)";
                String selectStoryId = "SELECT StoryId FROM Story WHERE GoUserId = ? AND StoryDateTime = ?";
                long storyId = Server.DB_HANDLER.readFromDB(selectStoryId, story.getFromUser().getUserId(), createdTimestamp, "1;BigInt").get(0).getLong();
                String storyUrl = generateStoryURL(storyId);
                String updateUrlStatement = "UPDATE Story SET StoryURL = ? WHERE GoUserId = ? AND StoryDateTime = ?";
                Server.DB_HANDLER.executeStatementsOnDB(updateUrlStatement, storyUrl, story.getFromUser().getUserId(), createdTimestamp);

                for (int i = 0; i < story.getStory().size(); i++) {
                    Server.DB_HANDLER.executeStatementsOnDB(insertStatement, story.getStory().get(i), storyId);
                }
                sendPacket(new Packet(Command.ANSWER, null));
                System.out.println("uploadStory-Packet sent");
            }
            case DELETE_STORY -> {
                System.out.println("deleteStory");
                String deleteStoryMediaStatement = "DELETE FROM StoryMedia WHERE EXISTS (SELECT 1 FROM Story WHERE Story.StoryId = StoryMedia.StoryId AND Story.StoryURL = ?)";
                Server.DB_HANDLER.executeStatementsOnDB(deleteStoryMediaStatement, story.getUrl());
                String deleteStoryStatement = "DELETE FROM Story WHERE StoryURL = ?";
                Server.DB_HANDLER.executeStatementsOnDB(deleteStoryStatement, story.getUrl());
                sendPacket(new Packet(Command.DELETED_STORY, null));
            }
        }
    }

    private synchronized void handleReportPacket(ReportPacket reportPacket) {
        Command command = reportPacket.getCommand();
        if (Command.ADD_REPORT.equals(command)) {
            System.out.println("addReport");
            User reportedUser = Server.DB_HANDLER.getUserFromName(reportPacket.getUserName());
            String insertStatement = "INSERT INTO ReportedGoUser(Reported,Reporter,Reason) VALUES(?,?,?)";
            Server.DB_HANDLER.executeStatementsOnDB(insertStatement, reportPacket.getSentByUser().getUserId(), reportedUser.getUserId(), reportPacket.getReason());
            sendPacket(new Packet(Command.REPORTED, null));
        }
    }

    private synchronized void handleProfilePacket(ProfilePacket profilePacket) {
        Command command = profilePacket.getCommand();
        if (Command.REQUEST_PROFILE.equals(command)) {
            System.out.println("requestProfile");
            long userId = profilePacket.getProfile().getUserId();
            String selectUserStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserDescription, GoUserIsPrivate, GoUserDateTime, GoUserProfilePicture FROM GoUser WHERE GoUserId = ?";
            List<DBObject> userResult = Server.DB_HANDLER.readFromDB(selectUserStatement, userId, "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;String", "7;Boolean", "8;Timestamp", "9;Blob");
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

            sendPacket(new ProfilePacket(Command.ANSWER, null, requestedProfile));
        }
    }

    private synchronized Story[] getStories(long userId) {
        String selectStoryStatement = "SELECT StoryId, GoUserId, StoryDateTime, StoryURL, Longitude, Latitude FROM Story WHERE GoUserId = ?";
        List<DBObject> storiesResult = Server.DB_HANDLER.readFromDB(selectStoryStatement, userId, "1;BigInt", "2;BigInt", "3;Timestamp", "4;String", "5;Double", "6;Double");
        List<Story> stories = new ArrayList<>();
        for (int i = 0; i < storiesResult.size(); i += 6) {
            stories.add(getStory(storiesResult, i));
        }
        return stories.toArray(new Story[0]);
    }

    private synchronized Story getStory(List<DBObject> postsResult, int startIndex) {
        long storyId = postsResult.get(startIndex).getLong();

        List<byte[]> storyMediaBytes = getStoryMedia(storyId);
        User storyUser = Server.DB_HANDLER.getUserFromId(postsResult.get(startIndex + 1).getLong());
        LocalDateTime storyCreatedDate = postsResult.get(startIndex + 2).getTimestamp().toLocalDateTime();
        String storyUrl = postsResult.get(startIndex + 3).getString();
        GoLocation storyLocation = new GoLocation(postsResult.get(startIndex + 4).getDouble(), postsResult.get(startIndex + 5).getDouble());

        return new Story(storyMediaBytes, storyUser, storyUrl, storyCreatedDate, storyLocation);
    }

    private synchronized List<byte[]> getStoryMedia(long storyId) {
        String selectStoryMediaStatement = "SELECT Story FROM StoryMedia WHERE StoryId = ?";
        List<DBObject> storyMediaResult = Server.DB_HANDLER.readFromDB(selectStoryMediaStatement, storyId, "1;Blob");
        List<byte[]> storyMediaBytes = new ArrayList<>();
        for (DBObject dbObject : storyMediaResult) {
            storyMediaBytes.add(dbObject.getBlob());
        }
        return storyMediaBytes;
    }

    private synchronized User[] getFollowed(long userId) {
        String selectedFollowedStatement = "SELECT GoUser FROM Follower WHERE GoUserFollower = ?";
        List<DBObject> followedResult = Server.DB_HANDLER.readFromDB(selectedFollowedStatement, userId, "1;BigInt");
        List<User> followed = new ArrayList<>();
        for (DBObject dbObject : followedResult) {
            long followedUserId = dbObject.getLong();
            followed.add(Server.DB_HANDLER.getUserFromId(followedUserId));
        }
        return followed.toArray(new User[0]);
    }

    private synchronized User[] getFollowers(long userId) {
        String selectedFollowersStatement = "SELECT GoUserFollower FROM Follower WHERE GoUser = ?";
        List<DBObject> followerResult = Server.DB_HANDLER.readFromDB(selectedFollowersStatement, userId, "1;BigInt");
        List<User> follower = new ArrayList<>();
        for (DBObject dbObject : followerResult) {
            long followerUserId = dbObject.getLong();
            follower.add(Server.DB_HANDLER.getUserFromId(followerUserId));
        }
        return follower.toArray(new User[0]);
    }

    private synchronized User[] getFriends(long userId) {
        String selectedFriendsStatement = "SELECT GoUserFriend FROM Friend WHERE GoUser = ?";
        List<DBObject> friendsResult = Server.DB_HANDLER.readFromDB(selectedFriendsStatement, userId, "1;BigInt");
        List<User> friends = new ArrayList<>();
        for (DBObject dbObject : friendsResult) {
            long friendUserId = dbObject.getLong();
            friends.add(Server.DB_HANDLER.getUserFromId(friendUserId));
        }
        return friends.toArray(new User[0]);
    }

    private synchronized Post[] getSavedPosts(long userId) {
        String selectSavedPostsStatement = "SELECT PostId FROM SavedPost WHERE GoUserId = ?";
        List<DBObject> savedPostsResult = Server.DB_HANDLER.readFromDB(selectSavedPostsStatement, userId, "1;BigInt");
        List<Post> savedPosts = new ArrayList<>();
        for (int i = 0; i < savedPostsResult.size(); i++) {
            long postId = savedPostsResult.get(i).getLong();
            String selectPostsStatement = "SELECT PostId, GoUserId, PostURL, PostDateTime, Longitude, Latitude, PostDescription FROM Post WHERE PostId = ?";
            List<DBObject> postsResult = Server.DB_HANDLER.readFromDB(selectPostsStatement, postId, "1;BigInt", "2;BigInt", "3;String", "4;Timestamp", "5;Double", "6;Double", "7;String");
            savedPosts.add(getPost(postsResult, 0));
        }
        return savedPosts.toArray(new Post[0]);
    }

    private synchronized Post getPost(List<DBObject> postsResult, int startIndex) {
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

    private synchronized Post[] getPosts(long userId) {
        String selectPostsStatement = "SELECT PostId, GoUserId, PostURL, PostDateTime, Longitude, Latitude, PostDescription FROM Post WHERE GoUserId = ?";
        List<DBObject> postsResult = Server.DB_HANDLER.readFromDB(selectPostsStatement, userId, "1;BigInt", "2;BigInt", "3;String", "4;Timestamp", "5;Double", "6;Double", "7;String");
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < postsResult.size(); i += 7) {
            posts.add(getPost(postsResult, i));
        }
        return posts.toArray(new Post[0]);
    }

    private synchronized List<byte[]> getPostMediaBytes(long postId) {
        String selectPostMediaStatement = "SELECT Post FROM PostMedia WHERE PostId = ?";
        List<DBObject> postMediaResult = Server.DB_HANDLER.readFromDB(selectPostMediaStatement, postId, "1;Blob");
        List<byte[]> postMediaBytes = new ArrayList<>();
        for (DBObject dbObject : postMediaResult) {
            postMediaBytes.add(dbObject.getBlob());
        }
        return postMediaBytes;
    }

    private synchronized User[] getLikes(long postId) {
        String selectLikesStatement = "SELECT GoUserId FROM GoLike WHERE PostId = ?";
        List<DBObject> likesResult = Server.DB_HANDLER.readFromDB(selectLikesStatement, postId, "1;BigInt");
        return likesResult.stream().map(l -> Server.DB_HANDLER.getUserFromId(l.getLong())).toArray(User[]::new);
    }

    private synchronized Comment[] getComments(long postId) {
        String selectCommentsStatement = "SELECT GoUserId, GoUserComment, CommentDateTime FROM PostComment WHERE PostId = ?";
        List<DBObject> commentsResult = Server.DB_HANDLER.readFromDB(selectCommentsStatement, postId, "1;String", "2;Timestamp", "3;BigInt");
        List<Comment> comments = new ArrayList<>();
        for (int j = 0; j < commentsResult.size(); j += 3) {
            User commentUser = Server.DB_HANDLER.getUserFromId(commentsResult.get(j).getLong());
            String commentContent = commentsResult.get(j + 1).getString();
            LocalDateTime commentCreatedDate = commentsResult.get(j + 2).getTimestamp().toLocalDateTime();
            Comment nextComment = new Comment(commentUser, commentContent, commentCreatedDate);
            comments.add(nextComment);
        }
        return comments.toArray(new Comment[0]);
    }

    private synchronized User[] getMarks(long postId) {
        String selectMarksStatement = "SELECT GoUserId FROM GoMark WHERE PostId = ?";
        List<DBObject> marksResult = Server.DB_HANDLER.readFromDB(selectMarksStatement,
                postId, "1;BigInt");
        return marksResult.stream().map(l -> Server.DB_HANDLER.getUserFromId(l.getLong())).toArray(User[]::new);
    }

    private synchronized void handlePostPacket(PostPacket postPacket) {
        Command command = postPacket.getCommand();
        Post post = postPacket.getPost();
        switch (command) {
            case UPLOAD_POST-> {
                System.out.println("uploadPost");
                if (post.getPictures() == null || post.getPictures().size() <= 0) {
                    sendPacket(new Packet(Command.NO_PICTURES, null));
                    return;
                }
                post.setCreatedDate(LocalDateTime.now());
                String insertStatement = "INSERT INTO Post(GoUserId,PostURL,PostDateTime,Longitude,Latitude,PostDescription) VALUES(?,?,?,?,?,?)";
                double locationLongitude = post.getLocation() == null ? 0 : post.getLocation().getLongitude();
                double locationLatitude = post.getLocation() == null ? 0 : post.getLocation().getLatitude();
                Timestamp createdTimestamp = Timestamp.valueOf(post.getCreatedDate());
                Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                        post.getFromUser().getUserId(),
                        post.getUrl(),
                        createdTimestamp,
                        locationLongitude,
                        locationLatitude,
                        post.getDescription());
                insertStatement = "INSERT INTO PostMedia(Post,PostId) VALUES(?,?)";

                String selectPostId = "SELECT PostId FROM Post WHERE GoUserId = ? AND PostDateTime = ?";
                System.out.println("Timestamp: " + createdTimestamp);
                long postId = Server.DB_HANDLER.readFromDB(selectPostId, post.getFromUser().getUserId(), createdTimestamp, "1;BigInt").get(0).getLong();
                String postURL = generatePostURL(postId);
                String updateUrlStatement = "UPDATE Post SET PostURL = ? WHERE GoUserId = ? AND PostDateTime = ?";
                Server.DB_HANDLER.executeStatementsOnDB(updateUrlStatement, postURL, post.getFromUser().getUserId(), createdTimestamp);

                for (int i = 0; i < post.getPictures().size(); i++) {
                    Server.DB_HANDLER.executeStatementsOnDB(insertStatement, post.getPictures().get(i), postId);
                }
                System.out.println("requestPost4");
                sendPacket(new Packet(Command.ANSWER, null));
                System.out.println("uploadPost-Packet sent");
            }
            case DELETE_POST -> {
                System.out.println("deletePost");
                String deletePostMediaStatement = "DELETE FROM PostMedia WHERE EXISTS (SELECT 1 FROM Post WHERE Post.PostId = PostMedia.PostId AND Post.PostURL = ?)";
                Server.DB_HANDLER.executeStatementsOnDB(deletePostMediaStatement, post.getUrl());
                String deletePostStatement = "DELETE FROM Post WHERE PostURL = ?";
                Server.DB_HANDLER.executeStatementsOnDB(deletePostStatement, post.getUrl());
                sendPacket(new Packet(Command.DELETED_POST, null));
            }
        }
    }

    private synchronized String generatePostURL(long postId){
        return Server.TEMPLATE_URL + "post/id=" + postId;
    }

    private synchronized String generateStoryURL(long storyId){
        return Server.TEMPLATE_URL + "story/id=" + storyId;
    }

    private synchronized void handleLoginPacket(LoginPacket loginPacket) {
        Command command = loginPacket.getCommand();
        switch (command) {
            case FIRST_TIME_LOGIN:
                System.out.println("FirstTimeLogin");
                if (isUserNameAlreadyExisting(loginPacket.getUserName())) {
                    sendPacket(new Packet(Command.USER_ALREADY_EXISTS, null));
                    return;
                }
                String insertStatement = "INSERT INTO GoUser(GoUserName,GoProfileName,GoUserEmail,GoUserPassword,GoUserIsPrivate,GoUserDateTime) VALUES(?,?,?,?,?,?)";
                Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                        loginPacket.getUserName(),
                        loginPacket.getProfileName(),
                        loginPacket.getEmail(),
                        loginPacket.getPassword(),
                        false,
                        Timestamp.valueOf(LocalDateTime.now()));
            case CHECK_PASSWORD:
                System.out.println("checkIfCorrectPassword");
                String selectStatement = "SELECT GoUserId, GoUserProfilePicture FROM GoUser WHERE GoUserName = ? AND GoUserPassword = ?";
                List<DBObject> result = Server.DB_HANDLER.readFromDB(selectStatement, loginPacket.getUserName(), loginPacket.getPassword(), "1;BigInt", "2;Blob");
                setUserId(result.get(0).getLong());
                Server.addClient(this);

                User user = new User(userId, loginPacket.getUserName(), loginPacket.getProfileName(), loginPacket.getEmail(), loginPacket.getPassword(), result.get(1).getBlob());
                sendPacket(new Packet(Command.ANSWER, user));
        }
    }

    private synchronized boolean isUserNameAlreadyExisting(String userName) {
        String selectStatement = "SELECT GoUserName FROM GoUser";
        List<DBObject> result = Server.DB_HANDLER.readFromDB(selectStatement, "1;String");
        for (DBObject dbObject : result) {
            if (dbObject.getString().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    private synchronized void handleBlockPacket(UserPacket userPacket) {
        Command command = userPacket.getCommand();
        if (Command.ADD_BLOCK.equals(command)) {
            System.out.println("addBlock");
            User blockedUser = Server.DB_HANDLER.getUserFromName(userPacket.getUserName());
            String insertStatement = "INSERT INTO BlockedGoUser(Blocker,Blocked) VALUES(?,?)";
            Server.DB_HANDLER.executeStatementsOnDB(insertStatement, userPacket.getSentByUser().getUserId(), blockedUser.getUserId());
            sendPacket(new Packet(Command.BLOCKED, null));
        }
    }

    private synchronized boolean sendPacket(Packet packet) {
        try {
            writer.writeObject(packet);
            writer.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
