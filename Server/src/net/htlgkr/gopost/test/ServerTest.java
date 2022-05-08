package net.htlgkr.gopost.test;

import net.htlgkr.gopost.data.*;
import net.htlgkr.gopost.notification.GoNotification;
import net.htlgkr.gopost.packet.*;
import net.htlgkr.gopost.server.Server;
import net.htlgkr.gopost.util.Command;
import net.htlgkr.gopost.util.Encrypt;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServerTest {

    private static final String IP_ADDRESS = "80.243.162.117";
    private static final int PORT = 16663;

    private static ObjectOutputStream writer;
    private static ObjectInputStream reader;

    private static final User testUser = new User(1, "jaster", "JasterMaxwell", "jaster@gmail.com", Encrypt.SHA512("Amongus128"), null);

    @BeforeClass
    public static void connect() {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(IP_ADDRESS, PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Packet sendPacket(Packet packet) {
        try {
            writer.writeObject(packet);
            writer.flush();
            return (Packet) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    @Test
    public void loginTest() {
        System.out.println("LoginTest");
        Packet packet = sendPacket(new LoginPacket(Command.FIRST_TIME_LOGIN, testUser, testUser.getProfileName(), testUser.getUserName(), testUser.getEmail(), testUser.getPassword()));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void handleCommentPacket() {

    }

    @Test
    public void handleLikePacket() {

    }

    @Test
    public void loginAlreadyExistsTest() {
        System.out.println("LoginAlreadyExistsTest");
        Packet packet = sendPacket(new LoginPacket(Command.FIRST_TIME_LOGIN, testUser, testUser.getProfileName(), testUser.getUserName(), testUser.getEmail(), testUser.getPassword()));
        if (packet == null || !packet.getCommand().equals(Command.USER_ALREADY_EXISTS)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void requestProfileTest() {
        System.out.println("RequestProfileTest");
        Profile profile = new Profile();
        profile.setUserId(testUser.getUserId());
        Packet packet = sendPacket(new ProfilePacket(Command.REQUEST_PROFILE, testUser, profile));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void uploadPostTest() {
        System.out.println("UploadPostTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Post post = new Post(bytes, testUser, null, null, null, null, null, null, null);
        Packet packet = sendPacket(new PostPacket(Command.UPLOAD_POST, testUser, post));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void getPostTest() {
        System.out.println("GetPostTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Post post = new Post(bytes, testUser, null, null, null, null, null, null, null);
        sendPacket(new PostPacket(Command.UPLOAD_POST, testUser, post));

        Post requestPost = new Post();
        requestPost.setUrl(Server.TEMPLATE_URL + "post/id=" + 0);
        PostPacket postPacket = new PostPacket(Command.GET_POST, testUser, requestPost);
        PostPacket requestedPost = (PostPacket) sendPacket(postPacket);
        if (requestedPost == null || !requestedPost.getCommand().equals(Command.ANSWER)) {
            Assert.fail();
        }
        System.out.println(requestedPost.getPost().getUrl());
    }

    @Test
    public void deletePostTest() {
        System.out.println("DeletePostTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Post post = new Post(bytes, testUser, Server.TEMPLATE_URL + "post/id=2", null, null, null, null, null, null);
        Packet packet = sendPacket(new PostPacket(Command.UPLOAD_POST, testUser, post));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
        packet = sendPacket(new PostPacket(Command.DELETE_POST, testUser, post));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void uploadPostNoPicturesTest() {
        System.out.println("UploadPostNoPicturesTest");
        List<byte[]> bytes = new ArrayList<>();
        Post post = new Post(bytes, testUser, null, null, null, null, null, null, null);
        Packet packet = sendPacket(new PostPacket(Command.UPLOAD_POST, testUser, post));
        if (packet == null || !packet.getCommand().equals(Command.NO_PICTURES)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void uploadStoryTest() {
        System.out.println("UploadStoryTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Story story = new Story(bytes, testUser, null, null, null);
        Packet packet = sendPacket(new StoryPacket(Command.UPLOAD_STORY, testUser, story));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void getStoryTest() {
        System.out.println("GetStoryTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Story story = new Story(bytes, testUser, null, null, null);
        sendPacket(new StoryPacket(Command.UPLOAD_STORY, testUser, story));

        Story requestStory = new Story();
        requestStory.setUrl(Server.TEMPLATE_URL + "story/id=" + 0);
        StoryPacket storyPacket = new StoryPacket(Command.GET_POST, testUser, requestStory);
        StoryPacket requestedStory = (StoryPacket) sendPacket(storyPacket);
        if (requestedStory == null || !requestedStory.getCommand().equals(Command.ANSWER)) {
            Assert.fail();
        }
        System.out.println(requestedStory.getStory().getUrl());
    }

    @Test
    public void deleteStoryTest() {
        System.out.println("deleteStoryTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Story story = new Story(bytes, testUser, Server.TEMPLATE_URL + "story/id=2", null, null);
        Packet packet = sendPacket(new StoryPacket(Command.UPLOAD_STORY, testUser, story));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
        packet = sendPacket(new StoryPacket(Command.DELETE_STORY, testUser, story));
        if (packet == null) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void uploadStoryNoPicturesTest() {
        System.out.println("UploadStoryNoPicturesTest");
        List<byte[]> bytes = new ArrayList<>();
        Story story = new Story(bytes, testUser, null, null, null);
        Packet packet = sendPacket(new StoryPacket(Command.UPLOAD_STORY, testUser, story));
        if (packet == null || !packet.getCommand().equals(Command.NO_PICTURES)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void requestProfilePostsCountTest() {
        System.out.println("RequestProfilePostsCountTest");
        Profile profile = new Profile();
        profile.setUserId(testUser.getUserId());
        Packet packet = sendPacket(new ProfilePacket(Command.REQUEST_PROFILE, testUser, profile));
        if (packet == null) {
            Assert.fail();
        }
        if (packet instanceof ProfilePacket profilePacket) {
            System.out.println(packet.getCommand());
            System.out.println(profilePacket.getProfile().getPosts().length);
        }
    }

    @Test
    public void requestProfileStoriesCountTest() {
        System.out.println("RequestProfileStoriesCountTest");
        Profile profile = new Profile();
        profile.setUserId(testUser.getUserId());
        Packet packet = sendPacket(new ProfilePacket(Command.REQUEST_PROFILE, testUser, profile));
        if (packet == null) {
            Assert.fail();
        }
        if (packet instanceof ProfilePacket profilePacket) {
            System.out.println(packet.getCommand());
            System.out.println(profilePacket.getProfile().getStories().length);
        }
    }

    @Test
    public void reportUserTest() {
        System.out.println("ReportUserTest");
        Packet packet = sendPacket(new ReportPacket(Command.ADD_REPORT, testUser, "jaster", "bullying"));
        if (packet == null || !packet.getCommand().equals(Command.REPORTED)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void blockUserTest() {
        System.out.println("BlockUserTest");
        Packet packet = sendPacket(new UserPacket(Command.ADD_BLOCK, testUser, "jaster"));
        if (packet == null || !packet.getCommand().equals(Command.BLOCKED)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void sendFollowerNotificationTest() {
        System.out.println("SendNotificationTest");
        long testUserId = 0;
        String title = "GoPost Follower";
        String body = "Somebody followed you on GoPost!";
        String icon = Server.GOPOST_ICON;
        boolean result = GoNotification.sendNotification(testUserId, title, body, icon);
        Assert.assertTrue(result);
    }

    @Test
    public void likePostTest() {
        System.out.println("LikePostTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Post post = new Post(bytes, testUser, null, null, null, null, null, null, null);
        sendPacket(new PostPacket(Command.UPLOAD_POST, testUser, post));

        ProfilePacket profilePacket = new ProfilePacket(Command.REQUEST_PROFILE, testUser, null);
        ProfilePacket profile = (ProfilePacket) sendPacket(profilePacket);
        if (profile == null) {
            Assert.fail();
        }

        String postUrl = profile.getProfile().getPosts()[0].getUrl();
        LikePacket likePacket = new LikePacket(Command.LIKE_POST, testUser, postUrl);
        Packet packet = sendPacket(likePacket);
        if (packet == null || !packet.getCommand().equals(Command.LIKED_POST)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void commentPostTest() {
        System.out.println("CommentPostTest");
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(new byte[0]);
        Post post = new Post(bytes, testUser, null, null, null, null, null, null, null);
        sendPacket(new PostPacket(Command.UPLOAD_POST, testUser, post));

        ProfilePacket profilePacket = new ProfilePacket(Command.REQUEST_PROFILE, testUser, null);
        ProfilePacket profile = (ProfilePacket) sendPacket(profilePacket);
        if (profile == null) {
            Assert.fail();
        }

        String postUrl = profile.getProfile().getPosts()[0].getUrl();
        Comment createdComment = new Comment(testUser, "amogus", LocalDateTime.now());
        CommentPacket commentPacket = new CommentPacket(Command.COMMENT_POST, testUser, postUrl, createdComment);
        Packet packet = sendPacket(commentPacket);
        if (packet == null || !packet.getCommand().equals(Command.COMMENTED_POST)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }

    @Test
    public void followUserTest() {
        System.out.println("FollowUserTest");
        String userName1 = "jaster";
        String userName2 = "goblin";
        sendPacket(new LoginPacket(Command.FIRST_TIME_LOGIN, testUser, testUser.getProfileName(), userName1, testUser.getEmail(), testUser.getPassword()));
        sendPacket(new LoginPacket(Command.FIRST_TIME_LOGIN, testUser, testUser.getProfileName(), userName2, testUser.getEmail(), testUser.getPassword()));

        UserPacket userPacket = new UserPacket(Command.FOLLOW, testUser, userName2);
        Packet packet = sendPacket(userPacket);
        if (packet == null || !packet.getCommand().equals(Command.FOLLOWED)) {
            Assert.fail();
        }
        System.out.println(packet.getCommand());
    }
}
