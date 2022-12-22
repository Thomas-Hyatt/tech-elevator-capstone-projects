package com.techelevator.model;

public class AddModeratorDto {

    private int forumId;
    private int userId;
    private boolean isModerator;

    public AddModeratorDto(int forumId, int userId, boolean isModerator) {
        this.forumId = forumId;
        this.userId = userId;
        this.isModerator = isModerator;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }
}
