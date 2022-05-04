package net.htlgkr.gopost.database;

import net.htlgkr.gopost.data.*;
import net.htlgkr.gopost.server.Server;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataQuery {
    public static synchronized Story[] getStories(long userId) {
        String selectStoryStatement = "SELECT StoryId, GoUserId, StoryDateTime, StoryURL, Longitude, Latitude FROM Story WHERE GoUserId = ?";
        List<DBObject> storiesResult = Server.DB_HANDLER.readFromDB(selectStoryStatement, userId, "1;BigInt", "2;BigInt", "3;Timestamp", "4;String", "5;Double", "6;Double");
        List<Story> stories = new ArrayList<>();
        for (int i = 0; i < storiesResult.size(); i += 6) {
            stories.add(getStory(storiesResult, i));
        }
        return stories.toArray(new Story[0]);
    }

    public static User getUserFromId(long userId) {
        String selectUserStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserProfilePicture FROM GoUser WHERE GoUserId = ?";
        List<DBObject> result = Server.DB_HANDLER.readFromDB(selectUserStatement, userId, "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;Blob");
        return new User(result.get(0).getLong(), result.get(1).getString(), result.get(2).getString(), result.get(3).getString(), result.get(4).getString(), result.get(5).getBlob());
    }

    public static User getUserFromName(String userName) {
        String selectUserStatement = "SELECT GoUserId, GoUserName, GoProfileName, GoUserEmail, GoUserPassword, GoUserProfilePicture FROM GoUser WHERE GoUserName = ?";
        List<DBObject> result = Server.DB_HANDLER.readFromDB(selectUserStatement, userName, "1;BigInt", "2;String", "3;String", "4;String", "5;String", "6;Blob");
        return new User(result.get(0).getLong(), result.get(1).getString(), result.get(2).getString(), result.get(3).getString(), result.get(4).getString(), result.get(5).getBlob());
    }

    public static synchronized Story getStory(List<DBObject> postsResult, int startIndex) {
        long storyId = postsResult.get(startIndex).getLong();

        List<byte[]> storyMediaBytes = getStoryMedia(storyId);
        User storyUser = getUserFromId(postsResult.get(startIndex + 1).getLong());
        LocalDateTime storyCreatedDate = postsResult.get(startIndex + 2).getTimestamp().toLocalDateTime();
        String storyUrl = postsResult.get(startIndex + 3).getString();
        GoLocation storyLocation = new GoLocation(postsResult.get(startIndex + 4).getDouble(), postsResult.get(startIndex + 5).getDouble());

        return new Story(storyMediaBytes, storyUser, storyUrl, storyCreatedDate, storyLocation);
    }

    public static synchronized List<byte[]> getStoryMedia(long storyId) {
        String selectStoryMediaStatement = "SELECT Story FROM StoryMedia WHERE StoryId = ?";
        List<DBObject> storyMediaResult = Server.DB_HANDLER.readFromDB(selectStoryMediaStatement, storyId, "1;Blob");
        List<byte[]> storyMediaBytes = new ArrayList<>();
        for (DBObject dbObject : storyMediaResult) {
            storyMediaBytes.add(dbObject.getBlob());
        }
        return storyMediaBytes;
    }

    public static synchronized User[] getFollowed(long userId) {
        String selectedFollowedStatement = "SELECT GoUser FROM Follower WHERE GoUserFollower = ?";
        List<DBObject> followedResult = Server.DB_HANDLER.readFromDB(selectedFollowedStatement, userId, "1;BigInt");
        List<User> followed = new ArrayList<>();
        for (DBObject dbObject : followedResult) {
            long followedUserId = dbObject.getLong();
            followed.add(getUserFromId(followedUserId));
        }
        return followed.toArray(new User[0]);
    }

    public static synchronized User[] getFollowers(long userId) {
        String selectedFollowersStatement = "SELECT GoUserFollower FROM Follower WHERE GoUser = ?";
        List<DBObject> followerResult = Server.DB_HANDLER.readFromDB(selectedFollowersStatement, userId, "1;BigInt");
        List<User> follower = new ArrayList<>();
        for (DBObject dbObject : followerResult) {
            long followerUserId = dbObject.getLong();
            follower.add(getUserFromId(followerUserId));
        }
        return follower.toArray(new User[0]);
    }

    public static synchronized User[] getFriends(long userId) {
        String selectedFriendsStatement = "SELECT GoUserFriend FROM Friend WHERE GoUser = ?";
        List<DBObject> friendsResult = Server.DB_HANDLER.readFromDB(selectedFriendsStatement, userId, "1;BigInt");
        List<User> friends = new ArrayList<>();
        for (DBObject dbObject : friendsResult) {
            long friendUserId = dbObject.getLong();
            friends.add(getUserFromId(friendUserId));
        }
        return friends.toArray(new User[0]);
    }

    public static synchronized Post[] getSavedPosts(long userId) {
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

    public static synchronized Post getPost(List<DBObject> postsResult, int startIndex) {
        long postId = postsResult.get(startIndex).getLong();

        List<byte[]> postMediaBytes = getPostMediaBytes(postId);
        User[] likes = getLikes(postId);
        Comment[] comments = getComments(postId);
        User[] marks = getMarks(postId);

        User postUser = getUserFromId(postsResult.get(startIndex + 1).getLong());
        String postUrl = postsResult.get(startIndex + 2).getString();
        LocalDateTime postCreatedDate = postsResult.get(startIndex + 3).getTimestamp().toLocalDateTime();
        GoLocation postLocation = new GoLocation(postsResult.get(startIndex + 4).getDouble(), postsResult.get(startIndex + 5).getDouble());
        String postDescription = postsResult.get(startIndex + 6).getString();

        return new Post(postMediaBytes, postUser, postUrl, postCreatedDate, postLocation, postDescription, likes, comments, marks);
    }

    public static synchronized Post[] getPosts(long userId) {
        String selectPostsStatement = "SELECT PostId, GoUserId, PostURL, PostDateTime, Longitude, Latitude, PostDescription FROM Post WHERE GoUserId = ?";
        List<DBObject> postsResult = Server.DB_HANDLER.readFromDB(selectPostsStatement, userId, "1;BigInt", "2;BigInt", "3;String", "4;Timestamp", "5;Double", "6;Double", "7;String");
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < postsResult.size(); i += 7) {
            posts.add(getPost(postsResult, i));
        }
        return posts.toArray(new Post[0]);
    }

    public static synchronized List<byte[]> getPostMediaBytes(long postId) {
        String selectPostMediaStatement = "SELECT Post FROM PostMedia WHERE PostId = ?";
        List<DBObject> postMediaResult = Server.DB_HANDLER.readFromDB(selectPostMediaStatement, postId, "1;Blob");
        List<byte[]> postMediaBytes = new ArrayList<>();
        for (DBObject dbObject : postMediaResult) {
            postMediaBytes.add(dbObject.getBlob());
        }
        return postMediaBytes;
    }

    public static synchronized User[] getLikes(long postId) {
        String selectLikesStatement = "SELECT GoUserId FROM GoLike WHERE PostId = ?";
        List<DBObject> likesResult = Server.DB_HANDLER.readFromDB(selectLikesStatement, postId, "1;BigInt");
        return likesResult.stream().map(l -> getUserFromId(l.getLong())).toArray(User[]::new);
    }

    public static synchronized Comment[] getComments(long postId) {
        String selectCommentsStatement = "SELECT GoUserId, GoUserComment, CommentDateTime FROM PostComment WHERE PostId = ?";
        List<DBObject> commentsResult = Server.DB_HANDLER.readFromDB(selectCommentsStatement, postId, "1;String", "2;Timestamp", "3;BigInt");
        List<Comment> comments = new ArrayList<>();
        for (int j = 0; j < commentsResult.size(); j += 3) {
            User commentUser = getUserFromId(commentsResult.get(j).getLong());
            String commentContent = commentsResult.get(j + 1).getString();
            LocalDateTime commentCreatedDate = commentsResult.get(j + 2).getTimestamp().toLocalDateTime();
            Comment nextComment = new Comment(commentUser, commentContent, commentCreatedDate);
            comments.add(nextComment);
        }
        return comments.toArray(new Comment[0]);
    }

    public static synchronized User[] getMarks(long postId) {
        String selectMarksStatement = "SELECT GoUserId FROM GoMark WHERE PostId = ?";
        List<DBObject> marksResult = Server.DB_HANDLER.readFromDB(selectMarksStatement,
                postId, "1;BigInt");
        return marksResult.stream().map(l -> getUserFromId(l.getLong())).toArray(User[]::new);
    }
}
