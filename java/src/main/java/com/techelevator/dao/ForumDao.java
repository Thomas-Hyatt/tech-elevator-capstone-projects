package com.techelevator.dao;

import com.techelevator.model.AddModeratorDto;
import com.techelevator.model.Forum;
import com.techelevator.model.ForumsUsersDto;
import com.techelevator.model.User;

import java.util.List;

public interface ForumDao {

    Forum createNewForum(Forum newForum, User userCreated);

    ForumsUsersDto joinUserToForum(ForumsUsersDto forumsUsersDto);

    List<ForumsUsersDto> getForumsUsers();

    List<Forum> getAllForums();

    List<Forum> getForumNamesForHomePage();

    Forum getForumById(int id);

    void addUserToForum(int forumId, int userId);

    List<Forum> searchForums(String searchString);

    Forum updateForum(Forum oldForum, Forum newForum);

    Forum getForumByName(String forumName);

    boolean isModerator(int forumId, int userId);

    void addModerator(User userAddingMod, AddModeratorDto addModeratorDto);

//    void deleteForum(int forumId);

}
