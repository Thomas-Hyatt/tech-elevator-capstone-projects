package com.techelevator.model;

public class ForumsUsersDto {

    private int forumId;
    private String forumName;
    private int userId;
    private String username;
    private boolean isModerator;

    public ForumsUsersDto(int forumId, int userId, boolean isModerator) {
        this.forumId = forumId;
        this.userId = userId;
        this.isModerator = isModerator;
    }

    public ForumsUsersDto() {}

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }
}
