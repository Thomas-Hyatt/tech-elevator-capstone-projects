package com.techelevator.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reply {

    private int replyId;
    private int userFrom;
    private int replyToReplyId;
    private int postId;
    private String usernameFrom;
    private String usernameTo;
    private String replyText;
    private String mediaLink;
    private boolean isDeleted;

    private LocalDate dateTime;
    private final List<Reply> subReplies = new ArrayList<>();

    public Reply(int replyId, int userFrom, int replyToReplyId, int postId, String replyText, String mediaLink, LocalDate dateTime) {
        this.replyId = replyId;
        this.userFrom = userFrom;
        this.replyToReplyId = replyToReplyId;
        this.postId = postId;
        this.replyText = replyText;
        this.mediaLink = mediaLink;
        this.dateTime = dateTime;
    }

    public Reply() {}

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String username) {
        this.usernameFrom = username;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String username) {
        this.usernameTo = username;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public int getReplyToReplyId() {
        return replyToReplyId;
    }

    public void setReplyToReplyId(int replyToReplyId) {
        this.replyToReplyId = replyToReplyId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public void addNewSubReply(Reply subReply) {
        subReplies.add(subReply);
    }

    public List<Reply> getSubReplies() {
        return subReplies;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
