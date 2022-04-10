use GoPostTest;
DROP TABLE PostComment;
DROP TABLE SavedPost;
DROP TABLE GoMark;
DROP TABLE GoLike;
DROP TABLE StoryMedia;
DROP TABLE PostMedia;
DROP TABLE ReportedGoUser;
DROP TABLE BlockedGoUser;
DROP TABLE Post;
DROP TABLE Friend;
DROP TABLE Follower;
DROP TABLE Story;
DROP TABLE GoUser;


CREATE TABLE GoUser
(
    GoUserId             BIGINT       NOT NULL auto_increment,
    GoUserName           VARCHAR(30)  NOT NULL,
    GoProfileName        VARCHAR(30)  NOT NULL,
    GoUserEmail          VARCHAR(75)  NOT NULL,
    GoUserPassword       VARCHAR(130) NOT NULL,
    GoUserDescription    VARCHAR(500),
    GoUserIsPrivate      BOOLEAN      NULL DEFAULT false,
    GoUserDateTime       TIMESTAMP    NOT NULL,
    GoUserProfilePicture LONGBLOB,
    CONSTRAINT GoUser_PK PRIMARY KEY (GoUserId)
);
CREATE TABLE Post
(
    PostId          BIGINT       NOT NULL auto_increment,
    GoUserId        BIGINT,
    PostURL         VARCHAR(200),
    PostDateTime    TIMESTAMP    NOT NULL,
    Longitude       DOUBLE,
    Latitude        DOUBLE,
    PostDescription VARCHAR(500) NULL,
    CONSTRAINT PostGoUser_FK FOREIGN KEY (GoUserId)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT Post_PK PRIMARY KEY (PostId)
);
CREATE TABLE Story
(
    StoryId       BIGINT    NOT NULL auto_increment,
    GoUserId      BIGINT,
    StoryDateTime TIMESTAMP NOT NULL,
    StoryURL      VARCHAR(200),
    Longitude     DOUBLE,
    Latitude      DOUBLE,
    CONSTRAINT StoryGoUser_FK FOREIGN KEY (GoUserId)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT Story_PK PRIMARY KEY (StoryId)
);
CREATE TABLE PostComment
(
    CommentId       BIGINT       NOT NULL auto_increment,
    GoUserComment   Varchar(500) NOT NULL,
    PostId          BIGINT,
    CommentDateTime TIMESTAMP    NOT NULL,
    GoUserId        BIGINT,
    CONSTRAINT PostCommentGoUser_FK FOREIGN KEY (GoUserId)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT PostCommentPost_FK FOREIGN KEY (PostId)
        REFERENCES Post (PostId) ON DELETE SET NULL,
    CONSTRAINT Comment_PK PRIMARY KEY (CommentId)
);
CREATE TABLE BlockedGoUser
(
    BlockedGoUserId BIGINT NOT NULL auto_increment,
    Blocker         BIGINT,
    Blocked         BIGINT,
    CONSTRAINT BlockerGoUser_FK FOREIGN KEY (Blocker)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT BlockedGoUser_FK FOREIGN KEY (Blocked)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT BlockedGoUser_PK PRIMARY KEY (BlockedGoUserId)
);
CREATE TABLE ReportedGoUser
(
    ReportedGoUserId BIGINT NOT NULL auto_increment,
    Reported         BIGINT,
    Reporter         BIGINT,
    Reason VARCHAR(500),
    CONSTRAINT ReportedGoUser_FK FOREIGN KEY (Reported)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT ReporterGoUser_FK FOREIGN KEY (Reporter)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT BlockedGoUser_PK PRIMARY KEY (ReportedGoUserId)
);
CREATE TABLE PostMedia
(
    PostMediaId BIGINT   NOT NULL auto_increment,
    Post        LONGBLOB NOT NULL,
    PostId      BIGINT,
    CONSTRAINT PostMediaPost_FK FOREIGN KEY (PostId)
        REFERENCES Post (PostId) ON DELETE SET NULL,
    CONSTRAINT PostMedia_PK PRIMARY KEY (PostMediaId)
);
CREATE TABLE StoryMedia
(
    StoryMediaId BIGINT   NOT NULL auto_increment,
    Story        LONGBLOB NOT NULL,
    StoryId      BIGINT,
    CONSTRAINT StoryMediaStory_FK FOREIGN KEY (StoryId)
        REFERENCES Story (StoryId) ON DELETE SET NULL,
    CONSTRAINT StoryMedia_PK PRIMARY KEY (StoryMediaId)
);
CREATE TABLE GoLike
(
    GoLikeId BIGINT NOT NULL auto_increment,
    PostId   BIGINT,
    GoUserId BIGINT,
    CONSTRAINT GoLikePost_FK FOREIGN KEY (PostId)
        REFERENCES Post (PostId) ON DELETE SET NULL,
    CONSTRAINT GoLikeGoUser_FK FOREIGN KEY (GoUserId)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT GoLike_PK PRIMARY KEY (GoLikeId)
);
CREATE TABLE GoMark
(
    GoMarkId BIGINT NOT NULL auto_increment,
    PostId   BIGINT,
    GoUserId BIGINT,
    CONSTRAINT GoMarkPost_FK FOREIGN KEY (PostId)
        REFERENCES Post (PostId) ON DELETE SET NULL,
    CONSTRAINT GoMarkGoUser_FK FOREIGN KEY (GoUserId)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT GoMark_PK PRIMARY KEY (GoMarkId)
);
CREATE TABLE SavedPost
(
    SavedPostId BIGINT NOT NULL auto_increment,
    GoUserId    BIGINT,
    PostId      BIGINT,
    CONSTRAINT SavedPost_FK FOREIGN KEY (PostId)
        REFERENCES Post (PostId) ON DELETE SET NULL,
    CONSTRAINT SavedPostGoUser_FK FOREIGN KEY (GoUserId)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT SavedPost_PK PRIMARY KEY (SavedPostId)
);
CREATE TABLE Friend
(
    FriendId     BIGINT NOT NULL auto_increment,
    GoUserFriend BIGINT,
    GoUser       BIGINT,
    CONSTRAINT FriendGoUserFriend_FK FOREIGN KEY (GoUserFriend)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT FriendGoUser_FK FOREIGN KEY (GoUser)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT Friend_PK PRIMARY KEY (FriendId)
);
CREATE TABLE Follower
(
    FollowerId     BIGINT NOT NULL auto_increment,
    GoUserFollower BIGINT,
    GoUser         BIGINT,
    CONSTRAINT FollowerGoUserFollower_FK FOREIGN KEY (GoUserFollower)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT FollowerGoUser_FK FOREIGN KEY (GoUser)
        REFERENCES GoUser (GoUserId) ON DELETE SET NULL,
    CONSTRAINT Follower_PK PRIMARY KEY (FollowerId)
);


