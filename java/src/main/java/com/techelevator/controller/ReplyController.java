package com.techelevator.controller;

import com.techelevator.dao.ReplyDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Reply;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class ReplyController {

    private final ReplyDao replyDao;
    private final UserDao userDao;

    public ReplyController(ReplyDao replyDao, UserDao userDao) {
        this.replyDao = replyDao;
        this.userDao = userDao;
    }

    @GetMapping(path = "/users/replies/{userId}")
    public List<Reply> getRepliesFromUser(@PathVariable("userId") int id) {
        return replyDao.listRepliesByUser(id);
    }

    @GetMapping(path = "/posts/replies/{postId}")
    public List<Reply> tempGetRepliesByPost(@PathVariable("postId") int id) {
        return replyDao.tempListRepliesByPost(id);
    }

    @GetMapping(path = "/posts/replies/{postId}/test")
    public List<Reply> getRepliesByPost(@PathVariable("postId") int id) {
        return replyDao.listRepliesByPost(id);
    }

    @RequestMapping(path = "/replies", method = RequestMethod.GET)
    public List<Reply> listAllReplies() {
        return replyDao.listAllReplies();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/replies", method = RequestMethod.POST)
    public Reply reply(@Valid @RequestBody Reply newReply, Principal principal) {
        if (principal != null) {
            try {
                return replyDao.reply(newReply);
            } catch (NullPointerException e) {
                System.out.println(e.getMessage());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return null;
    }


    @PutMapping(path="/replies/{replyId}")
    public void editReply(@PathVariable int replyId, @Valid @RequestBody Reply newReply, Principal principal) {
        if (principal != null && userDao.findByUsername(principal.getName()).getId() == newReply.getUserFrom()) {
            replyDao.editReply(replyId, newReply);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/replies/{replyId}", method = RequestMethod.DELETE)
    public void deleteReply(@PathVariable("replyId") int id) {
        try {
            replyDao.deleteReply(id);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

}
