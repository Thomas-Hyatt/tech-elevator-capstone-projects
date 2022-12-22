package com.techelevator.dao;

import com.techelevator.model.Forum;
import com.techelevator.model.Post;
import com.techelevator.model.VotesForPostDto;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPostDao implements PostDao {

    private final JdbcTemplate jdbcTemplate;
    private final ForumDao forumDao;
    private final UserDao userDao;
    private final PostVoteDao postVoteDao;

    public JdbcPostDao(JdbcTemplate jdbcTemplate, ForumDao forumDao, UserDao userDao, PostVoteDao postVoteDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.forumDao = forumDao;
        this.userDao = userDao;
        this.postVoteDao = postVoteDao;
    }


    @Override
    public List<Post> getPostsForHomePage() {
        List<Post> getPosts = new ArrayList<>();

        String sql = "SELECT * FROM posts ORDER BY date_time DESC;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while(rowSet.next()) {
            getPosts.add(mapRowToPost(rowSet));
        }

        return getPosts;
    }

    @Override
    public List<Post> getPostsByForum(String forumName) {
        List<Post> getPosts = new ArrayList<>();

        String sql = "SELECT post_id, user_id, forum_id, title, text, media_link, date_time FROM posts JOIN forums USING (forum_id) WHERE forums.name = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, forumName);
        while(rowSet.next()) {
            getPosts.add(mapRowToPost(rowSet));
        }
        return getPosts;
    }

    @Override
    public Post createNewPost(Post newPost) {
        String sql = "";
        Integer postId = null;
        try {
            if (newPost.getText() == null && newPost.getMediaLink() == null) {
                sql = "INSERT INTO posts (user_id, forum_id, title) VALUES (?, ?, ?) RETURNING post_id";
                postId = jdbcTemplate.queryForObject(sql, Integer.class, newPost.getUserId(), newPost.getForumId(),
                        newPost.getTitle());
            } else if (newPost.getText() == null) {
                sql = "INSERT INTO posts (user_id, forum_id, title, media_link) VALUES (?, ?, ?, ?) RETURNING post_id;";
                postId = jdbcTemplate.queryForObject(sql, Integer.class, newPost.getUserId(), newPost.getForumId(),
                        newPost.getTitle(), newPost.getMediaLink());
            } else if (newPost.getMediaLink() == null) {
                sql = "INSERT INTO posts (user_id, forum_id, title, text) VALUES (?, ?, ?, ?) RETURNING post_id;";
                postId = jdbcTemplate.queryForObject(sql, Integer.class, newPost.getUserId(), newPost.getForumId(),
                        newPost.getTitle(), newPost.getText());
            } else {
                sql = "INSERT INTO posts (user_id, forum_id, title, text, media_link) VALUES (?, ?, ?, ?, ?) RETURNING post_id;";
                postId = jdbcTemplate.queryForObject(sql, Integer.class, newPost.getUserId(), newPost.getForumId()
                        , newPost.getTitle(), newPost.getText(), newPost.getMediaLink());
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (postId != null) {
            newPost.setPostId(postId);
        } else {
            return null;
        }
        return newPost;
    }

    // this method also functions as an addImageToPost() method without the redundancy of 2 edit methods
    @Override
    public void editPost(int postId, Post updatedPost) {
        if (postId == updatedPost.getPostId()) {
            String sql = "UPDATE posts SET title = ?, text = ?, media_link = ? WHERE post_id = ?;";
            jdbcTemplate.update(sql, updatedPost.getTitle(), updatedPost.getText(), updatedPost.getMediaLink(), postId);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // is this returning void since it's not actually returning a new updated post?
        // yes update in sql doesn't return anything
    }

    @Override
    public void deletePost(int id) {
        String sql = "DELETE FROM replies WHERE post_id = ?;";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM post_votes WHERE post_id = ?;";
        jdbcTemplate.update(sql, id);
        sql = "DELETE FROM posts WHERE post_id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Post> searchPosts(String searchString) {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE title ILIKE ? OR text ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, "%" + searchString + "%", "%" + searchString + "%");
        while(rowSet.next()) {
            posts.add(mapRowToPost(rowSet));
        }
        return posts;
    }

    private Post mapRowToPost(SqlRowSet rowSet) {
        Post post = new Post();
        post.setPostId(rowSet.getInt("post_id"));
        post.setUserId(rowSet.getInt("user_id"));
        post.setUsername(userDao.getUserById(post.getUserId()).getUsername());
        post.setForumId(rowSet.getInt("forum_id"));
        post.setForumName(forumDao.getForumById(post.getForumId()).getName());
        post.setTitle(rowSet.getString("title"));
        post.setText(rowSet.getString("text"));
        post.setMediaLink(rowSet.getString("media_link"));
        if (rowSet.getDate("date_time") != null) {
            post.setDateTime(rowSet.getDate("date_time").toLocalDate());
            post.setTimeInMillis(rowSet.getDate("date_time").getTime());
        }
        VotesForPostDto dto = postVoteDao.getPostVotesByPost(post.getPostId());
        post.setUpvotes(dto.getUpvotes());
        post.setDownvotes(dto.getDownvotes());
        post.setVotes(dto.getVotes());
        return post;
    }
}
