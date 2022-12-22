package com.techelevator.model;

import java.time.LocalDate;

public class PostVote {

    private int userId;
    private int postId;
    private boolean isUpvote;
    private LocalDate timeDate;

    public PostVote(int userId, int postId, boolean isUpvote, LocalDate timeDate) {
        this.userId = userId;
        this.postId = postId;
        this.isUpvote = isUpvote;
        this.timeDate = timeDate;
    }

    public PostVote() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public boolean isUpvote() {
        return isUpvote;
    }

    public void setUpvote(boolean upvote) {
        isUpvote = upvote;
    }

    public LocalDate getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(LocalDate timeDate) {
        this.timeDate = timeDate;
    }
}
