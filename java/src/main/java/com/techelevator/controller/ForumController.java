package com.techelevator.controller;

import com.techelevator.dao.ForumDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.AddModeratorDto;
import com.techelevator.model.Forum;
import com.techelevator.model.ForumsUsersDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
public class ForumController {

    private final ForumDao forumDao;
    private final UserDao userDao;

    public ForumController(ForumDao forumDao, UserDao userDao) {
        this.forumDao = forumDao;
        this.userDao = userDao;
    }

    @PostMapping(path = "/forums/")
    @ResponseStatus(HttpStatus.CREATED)
    public Forum createNewForum(@Valid @RequestBody Forum forum, Principal principal) {
        if (principal != null) {
            return forumDao.createNewForum(forum, userDao.findByUsername(principal.getName()));
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping(path="/forums/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ForumsUsersDto joinUserToForum(@Valid @RequestBody ForumsUsersDto forumsUsersDto) {
        return forumDao.joinUserToForum(forumsUsersDto);
    }

    @GetMapping(path = "/forums")
    public List<Forum> getAllForums() {
        return forumDao.getAllForums();
    }



    @GetMapping(path = "/forums/users/")
    public List<ForumsUsersDto> forumsUsersDtos() {
        return forumDao.getForumsUsers();
    }

    @GetMapping(path = "/forums/home")
    public List<Forum> getForumNamesForHomePage() {
        return forumDao.getForumNamesForHomePage();
    }

    @GetMapping(path = "/forums/{forumName}")
    public Forum getForumByName(@PathVariable String forumName) {
        return forumDao.getForumByName(forumName);
    }

    @GetMapping(path = "/forums/search/{searchString}")
    public List<Forum> searchForums(@PathVariable String searchString) {
        return forumDao.searchForums(searchString);
    }

    @PutMapping(path = "/forums/{forumName}")
    public Forum updateForum(@PathVariable String forumName, @RequestBody Forum forum, Principal principal) {
        if (principal != null && forumDao.isModerator(forum.getForumId(), userDao.findIdByUsername(principal.getName()))) {
            return forumDao.updateForum(forumDao.getForumByName(forumName), forum);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/forums/{forumId}/users/{userId}/isModerator")
    public boolean isModerator(@PathVariable int forumId, @PathVariable int userId) {
        return forumDao.isModerator(forumId, userId);
    }

    @PutMapping(path = "/forums/addModerator")
    public void addModerator(@RequestBody AddModeratorDto addModeratorDto, Principal principal) {
        if (principal != null) {
            forumDao.addModerator(userDao.findByUsername(principal.getName()), addModeratorDto);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    //DOESN'T WORK
//    @DeleteMapping(path = "/forums/{forumId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteForum(@PathVariable int forumId) {
//        forumDao.deleteForum(forumId);
//    }

}
