package com.techelevator.controller;


import com.techelevator.dao.UserDao;
import com.techelevator.model.Forum;
import com.techelevator.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("api/users")
public class UsersController {

    private final UserDao userDao;

    public UsersController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @GetMapping(path = "/search/{searchString}")
    public List<User> searchForums(@PathVariable String searchString) {
        return userDao.searchUsers(searchString);
    }



}
