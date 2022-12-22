package com.techelevator.dao;

import com.techelevator.model.Forum;
import com.techelevator.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    User getUserById(int userId);

    User findByUsername(String username);

    List<User> searchUsers(String searchString);

    int findIdByUsername(String username);

    boolean create(String username, String password, String role);
}
