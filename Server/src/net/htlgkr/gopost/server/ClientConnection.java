package net.htlgkr.gopost.server;

import net.htlgkr.gopost.data.*;
import net.htlgkr.gopost.database.DBObject;
import net.htlgkr.gopost.database.DataQuery;
import net.htlgkr.gopost.notification.GoNotification;
import net.htlgkr.gopost.packet.*;
import net.htlgkr.gopost.util.Command;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ClientConnection extends WebSocketServer {

    public ClientConnection(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Client connected: " + webSocket.getRemoteSocketAddress().getAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println("WebSocket closed");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(s);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        super.onMessage(conn, message);
        Object readObject;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.array());
            ObjectInputStream reader = new ObjectInputStream(byteArrayInputStream);
            readObject = reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Object received: " + readObject);
        if (!(readObject instanceof Packet packet)) return;
        handlePacket(packet, conn);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server Running");
    }

    private synchronized void handlePacket(Packet packet, WebSocket client) {
        System.out.println("Packet received: " + packet.getCommand());
        if (packet instanceof UserPacket userPacket) {
            handleUserPacket(userPacket, client);
        } else if (packet instanceof LoginPacket loginPacket) {
            handleLoginPacket(loginPacket, client);
        } else if (packet instanceof PostPacket postPacket) {
            handlePostPacket(postPacket, client);
        } else if (packet instanceof ProfilePacket profilePacket) {
            handleProfilePacket(profilePacket, client);
        } else if (packet instanceof ReportPacket reportPacket) {
            handleReportPacket(reportPacket, client);
        } else if (packet instanceof StoryPacket storyPacket) {
            handleStoryPacket(storyPacket, client);
        } else if (packet instanceof LikePacket likePacket) {
            handleLikePacket(likePacket, client);
        } else if (packet instanceof CommentPacket commentPacket) {
            handleCommentPacket(commentPacket, client);
        }
    }

    private synchronized void handleCommentPacket(CommentPacket commentPacket, WebSocket client) {
        Comment comment = commentPacket.getComment();
        User sentUser = commentPacket.getSentByUser();
        String postUrl = commentPacket.getCommentedPostURL();
        String selectPostId = "SELECT PostId FROM Post WHERE PostURL = ?";
        long postId = Server.DB_HANDLER.readFromDB(selectPostId, postUrl, "1;BigInt").get(0).getLong();
        String insertStatement = "INSERT INTO GoLike(GoUserComment,PostId,CommentDateTime,GoUserId) VALUES(?,?,?,?)";
        Timestamp createdTimestamp = Timestamp.valueOf(comment.getCreatedDate());
        Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                comment.getMessage(),
                postId,
                createdTimestamp,
                sentUser.getUserId());
        sendPacket(new Packet(Command.COMMENTED_POST, null), client);

        String selectCommentedUserId = "SELECT UserId FROM Post WHERE PostURL = ?";
        long commentedUserId = Server.DB_HANDLER.readFromDB(selectCommentedUserId, postUrl, "1;BigInt").get(0).getLong();

        sendCommentNotification(sentUser, DataQuery.getUserFromId(commentedUserId));
    }

    private synchronized void handleLikePacket(LikePacket likePacket, WebSocket client) {
        User sentUser = likePacket.getSentByUser();
        String postUrl = likePacket.getLikedPostUrl();
        String selectPostId = "SELECT PostId FROM Post WHERE PostURL = ?";
        long postId = Server.DB_HANDLER.readFromDB(selectPostId, postUrl, "1;BigInt").get(0).getLong();
        String insertStatement = "INSERT INTO GoLike(PostId,GoUserId) VALUES(?,?)";
        Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                postId,
                sentUser.getUserId());

        String selectLikedUserId = "SELECT UserId FROM Post WHERE PostURL = ?";
        long likedUserId = Server.DB_HANDLER.readFromDB(selectLikedUserId, postUrl, "1;BigInt").get(0).getLong();

        sendPacket(new Packet(Command.LIKED_POST, null), client);
        sendLikeNotification(sentUser, DataQuery.getUserFromId(likedUserId));
    }

    private synchronized void handleStoryPacket(StoryPacket storyPacket, WebSocket client) {
        Command command = storyPacket.getCommand();
        Story story = storyPacket.getStory();
        switch (command) {
            case UPLOAD_STORY -> {
                System.out.println(Command.UPLOAD_STORY);
                if (story.getStory() == null || story.getStory().size() <= 0) {
                    sendPacket(new Packet(Command.NO_PICTURES, null), client);
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
                sendPacket(new Packet(Command.ANSWER, null), client);
                System.out.println("uploadStory-Packet sent");
            }
            case DELETE_STORY -> {
                System.out.println(Command.DELETE_STORY);
                String deleteStoryMediaStatement = "DELETE FROM StoryMedia WHERE EXISTS (SELECT 1 FROM Story WHERE Story.StoryId = StoryMedia.StoryId AND Story.StoryURL = ?)";
                Server.DB_HANDLER.executeStatementsOnDB(deleteStoryMediaStatement, story.getUrl());
                String deleteStoryStatement = "DELETE FROM Story WHERE StoryURL = ?";
                Server.DB_HANDLER.executeStatementsOnDB(deleteStoryStatement, story.getUrl());
                sendPacket(new Packet(Command.DELETED_STORY, null), client);
            }
            case GET_STORY -> {
                System.out.println(Command.GET_STORY);
                String getStoryStatement = "SELECT StoryId, GoUserId, StoryDateTime, StoryURL, Longitude, Latitude FROM Story WHERE GoUserId = ?";
                List<DBObject> storyResult = Server.DB_HANDLER.readFromDB(getStoryStatement, story.getUrl(), "1;String");

                Story requestedStory = DataQuery.getStory(storyResult, 0);
                sendPacket(new StoryPacket(Command.ANSWER, null, requestedStory), client);
            }
        }
    }

    private synchronized void handleReportPacket(ReportPacket reportPacket, WebSocket client) {
        Command command = reportPacket.getCommand();
        if (Command.ADD_REPORT.equals(command)) {
            System.out.println(Command.ADD_REPORT);
            User reportedUser = DataQuery.getUserFromName(reportPacket.getUserName());
            String insertStatement = "INSERT INTO ReportedGoUser(Reported,Reporter,Reason) VALUES(?,?,?)";
            Server.DB_HANDLER.executeStatementsOnDB(insertStatement, reportPacket.getSentByUser().getUserId(), reportedUser.getUserId(), reportPacket.getReason());
            sendPacket(new Packet(Command.REPORTED, null), client);
        }
    }

    private synchronized void handleProfilePacket(ProfilePacket profilePacket, WebSocket client) {
        Command command = profilePacket.getCommand();
        if (Command.REQUEST_PROFILE.equals(command)) {
            System.out.println(Command.REQUEST_PROFILE);
            long userId = profilePacket.getProfile().getUserId();
            String selectUserStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserDescription, GoUserIsPrivate, GoUserDateTime, GoUserProfilePicture FROM GoUser WHERE GoUserId = ?";
            List<DBObject> userResult = Server.DB_HANDLER.readFromDB(selectUserStatement, userId, "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;String", "7;Boolean", "8;Timestamp", "9;Blob");
            Post[] posts = DataQuery.getPosts(userId);
            Post[] savedPosts = DataQuery.getSavedPosts(userId);
            User[] friends = DataQuery.getFriends(userId);
            User[] followers = DataQuery.getFollowers(userId);
            User[] followed = DataQuery.getFollowed(userId);
            Story[] stories = DataQuery.getStories(userId);

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

            sendPacket(new ProfilePacket(Command.ANSWER, null, requestedProfile), client);
        }
    }

    private synchronized void handlePostPacket(PostPacket postPacket, WebSocket client) {
        Command command = postPacket.getCommand();
        Post post = postPacket.getPost();
        switch (command) {
            case UPLOAD_POST -> {
                System.out.println(Command.UPLOAD_POST);
                if (post.getPictures() == null || post.getPictures().size() <= 0) {
                    sendPacket(new Packet(Command.NO_PICTURES, null), client);
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
                sendPacket(new Packet(Command.ANSWER, null), client);
                System.out.println("uploadPost-Packet sent");
            }
            case DELETE_POST -> {
                System.out.println(Command.DELETE_POST);
                String deletePostMediaStatement = "DELETE FROM PostMedia WHERE EXISTS (SELECT 1 FROM Post WHERE Post.PostId = PostMedia.PostId AND Post.PostURL = ?)";
                Server.DB_HANDLER.executeStatementsOnDB(deletePostMediaStatement, post.getUrl());
                String deletePostStatement = "DELETE FROM Post WHERE PostURL = ?";
                Server.DB_HANDLER.executeStatementsOnDB(deletePostStatement, post.getUrl());
                sendPacket(new Packet(Command.DELETED_POST, null), client);
            }
            case GET_POST -> {
                System.out.println(Command.GET_POST);
                System.out.println("PostURL: " + post.getUrl());
                String getPostStatement = "SELECT PostId, GoUserId, PostURL, PostDateTime, Longitude, Latitude, PostDescription FROM Post WHERE PostURL = ?";
                List<DBObject> postResult = Server.DB_HANDLER.readFromDB(getPostStatement, post.getUrl(), "1;BigInt", "2;BigInt", "3;String", "4;Timestamp", "5;Double", "6;Double", "7;String");
                System.out.println(postResult.size());
                Post requestedPost = DataQuery.getPost(postResult, 0);
                System.out.println("Post: " + requestedPost);
                sendPacket(new PostPacket(Command.ANSWER, null, requestedPost), client);
            }
        }
    }

    private synchronized String generatePostURL(long postId) {
        return Server.TEMPLATE_URL + "post/id=" + postId;
    }

    private synchronized String generateStoryURL(long storyId) {
        return Server.TEMPLATE_URL + "story/id=" + storyId;
    }

    private synchronized void handleLoginPacket(LoginPacket loginPacket, WebSocket client) {
        Command command = loginPacket.getCommand();
        switch (command) {
            case FIRST_TIME_LOGIN:
                System.out.println(Command.FIRST_TIME_LOGIN);
                if (DataQuery.isUserNameAlreadyExisting(loginPacket.getUserName())) {
                    sendPacket(new Packet(Command.USER_ALREADY_EXISTS, null), client);
                    return;
                }
                if (DataQuery.isEmailAlreadyExisting(loginPacket.getEmail())) {
                    sendPacket(new Packet(Command.EMAIL_ALREADY_EXISTS, null), client);
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
                System.out.println("Inserted new user");
            case LOGIN:
                System.out.println(Command.LOGIN);
                String selectStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserProfilePicture FROM GoUser WHERE GoUserName = ? AND GoUserPassword = ?";
                List<DBObject> result = Server.DB_HANDLER.readFromDB(selectStatement, loginPacket.getUserName(), loginPacket.getPassword(), "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;Blob");
                System.out.println(result.size());
                if (result.isEmpty()) {
                    sendPacket(new Packet(Command.USER_DOESNT_EXIST, null), client);
                    return;
                }

                User user = new User(result.get(0).getLong(), result.get(1).getString(), result.get(2).getString(), result.get(3).getString(), result.get(4).getString(), result.get(5).getBlob());
                sendPacket(new Packet(Command.ANSWER, user), client);
        }
    }

    private synchronized void handleUserPacket(UserPacket userPacket, WebSocket client) {
        Command command = userPacket.getCommand();
        if (Command.ADD_BLOCK.equals(command)) {
            System.out.println(Command.ADD_BLOCK);
            User blockedUser = DataQuery.getUserFromName(userPacket.getUserName());
            String insertStatement = "INSERT INTO BlockedGoUser(Blocker,Blocked) VALUES(?,?)";
            Server.DB_HANDLER.executeStatementsOnDB(insertStatement, userPacket.getSentByUser().getUserId(), blockedUser.getUserId());
            sendPacket(new Packet(Command.BLOCKED, null), client);
        } else if (Command.FOLLOW.equals(command)) {
            System.out.println(Command.FOLLOW);
            User followedUser = DataQuery.getUserFromName(userPacket.getUserName());
            User followerUser = userPacket.getSentByUser();
            String insertStatement = "INSERT INTO Follower(GoUserFollower,GoUser) VALUES(?,?)";
            Server.DB_HANDLER.executeStatementsOnDB(insertStatement, followerUser.getUserId(), followedUser.getUserId());
            sendPacket(new Packet(Command.FOLLOWED, null), client);
            sendFollowNotification(followerUser, followedUser);
        }
    }

    private synchronized void sendFollowNotification(User followerUser, User followedUser) {
        String title = "GoPost";
        String body = followerUser.getProfileName() + " just followed you on GoPost!";
        String icon = Server.GOPOST_ICON;
        GoNotification.sendNotification(followedUser.getUserId(), title, body, icon);
    }

    private synchronized void sendLikeNotification(User likeUser, User likedUser) {
        String title = "GoPost";
        String body = likeUser.getProfileName() + " just liked your Post on GoPost!";
        String icon = Server.GOPOST_ICON;
        GoNotification.sendNotification(likedUser.getUserId(), title, body, icon);
    }

    private synchronized void sendCommentNotification(User commentUser, User commentedUser) {
        String title = "GoPost";
        String body = commentUser.getProfileName() + " just commented your Post on GoPost!";
        String icon = Server.GOPOST_ICON;
        GoNotification.sendNotification(commentedUser.getUserId(), title, body, icon);
    }

    private synchronized boolean sendPacket(Packet packet, WebSocket client) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream writer = new ObjectOutputStream(byteArrayOutputStream);
            writer.writeObject(packet);
            writer.flush();

            client.send(byteArrayOutputStream.toByteArray());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
