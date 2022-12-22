package com.techelevator.dao;

import com.techelevator.model.Forum;
import com.techelevator.model.Post;

import java.util.List;

public interface PostDao {

    List<Post> getPostsForHomePage();

    List<Post> getPostsByForum(String forumName);

    List<Post> searchPosts(String searchString);

    Post createNewPost(Post newPost);

    void editPost(int postId, Post updatedPost);

    void deletePost(int id);

}
