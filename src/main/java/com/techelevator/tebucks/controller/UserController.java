package com.techelevator.tebucks.controller;


import com.techelevator.tebucks.dao.*;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    // Get a list of users excluding the currently logged-in user
    @GetMapping(path = "")
    public List<User> listUsers(Principal loggedInUser) {

        // First, get all users in the database
        List<User> otherUsers = userDao.findAll();

        int loggedInUserId = userDao.findIdByUsername(loggedInUser.getName());

        // Find and remove the user in the list that is the currently logged-in user
        otherUsers.removeIf(user -> user.getId() == loggedInUserId);

        return otherUsers;
    }

}
