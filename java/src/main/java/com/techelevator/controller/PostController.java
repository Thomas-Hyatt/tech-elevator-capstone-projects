package com.techelevator.controller;

import com.techelevator.dao.PostDao;
import com.techelevator.dao.ReplyDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.DadJokeDto;
import com.techelevator.model.Post;
import com.techelevator.model.Reply;
import com.techelevator.service.DadJokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
public class PostController {

    @Autowired
    private final PostDao postDao;
    private final UserDao userDao;
    private final ReplyDao replyDao;
    private final String DAD_JOKE_BOT_NAME = "Annoying_Dad_Joke_Bot";
    private final int DIRECT_REPLY_REPLY_TO_REPLY_ID = 0;
    private final String DAD_JOKE_REPLY_MEDIA_LINK = "";


    public PostController(PostDao postDao, UserDao userDao, ReplyDao replyDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.replyDao = replyDao;
    }

    @GetMapping(path = "/posts")
    public List<Post> getPostsForHomePage() {
        return postDao.getPostsForHomePage();
    }

    @PostMapping(path = "forums/posts/")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createNewPost(@RequestBody Post newPost, Principal principal) {

        if (principal != null) {
            newPost.setUserId(userDao.findIdByUsername(principal.getName()));
            Post createdPost = postDao.createNewPost(newPost);
            if (createdPost == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            } else {
                DadJokeService dadJokeService = new DadJokeService();
                String dadJoke = dadJokeService.getDadJoke();

                Reply dadJokeReply = new Reply();
                dadJokeReply.setUserFrom(userDao.findIdByUsername(DAD_JOKE_BOT_NAME));
                dadJokeReply.setReplyToReplyId(DIRECT_REPLY_REPLY_TO_REPLY_ID);
                dadJokeReply.setPostId(createdPost.getPostId());
                dadJokeReply.setReplyText(dadJoke);
                dadJokeReply.setMediaLink(DAD_JOKE_REPLY_MEDIA_LINK);

                replyDao.reply(dadJokeReply);
            }
            return createdPost;
        } else {
            return null;
        }
    }

    @PutMapping(path = "/posts/{postId}")
    public void editPost(@PathVariable int postId, @RequestBody Post post, Principal principal) {
        if (principal != null && userDao.findIdByUsername(principal.getName()) == post.getUserId()) {
            postDao.editPost(postId, post);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(path = "/posts/search/{searchString}")
    public List<Post> searchPosts(@PathVariable String searchString) {
        return postDao.searchPosts(searchString);
    }

    @GetMapping(path = "forums/{forumName}/posts")
    public List<Post> getPostsByForum(@PathVariable String forumName) {
        return postDao.getPostsByForum(forumName);
    }

    @DeleteMapping(path = "posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostById(@PathVariable int id) {
        postDao.deletePost(id);
    }

}
