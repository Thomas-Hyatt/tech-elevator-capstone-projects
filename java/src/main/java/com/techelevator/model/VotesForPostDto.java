package com.techelevator.model;

public class VotesForPostDto {

    private int postId;
    private int upvotes;
    private int downvotes;
    private int votes;

    public VotesForPostDto(int postId, int upvotes, int downvotes, int votes) {
        this.postId = postId;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
        this.votes = votes;
    }

    public VotesForPostDto() {}

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }
}
