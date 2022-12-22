package com.techelevator.model;

import org.apache.tomcat.jni.Local;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Forum {

    private int forumId;
    private String name;
    private String description;
    private LocalDate dateCreated;

    public Forum(int forumId, String name, String description, LocalDate dateCreated) {
        this.forumId = forumId;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public Forum() {}

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
}
