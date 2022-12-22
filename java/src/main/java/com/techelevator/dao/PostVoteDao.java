package com.techelevator.dao;

import com.techelevator.model.PostVote;
import com.techelevator.model.VotesForPostDto;

import java.util.List;

public interface PostVoteDao {

    void upVotePost(PostVote postVote);

    void downVotePost(PostVote postVote);

    List<PostVote> getAllPostVotes();

    List<PostVote> getPostVotesByUser(int userId);

    VotesForPostDto getPostVotesByPost(int postId);

    List<PostVote> getUpVotesByUser(int userId);

    List<PostVote> getDownVotesByUser(int userId);

    List<PostVote> getUpVotesByPost(int postId);

    List<PostVote> getDownVotesByPost(int postId);

    boolean hasUserVotedOnPost(int postId, int userId);

    void updatePostVote(PostVote postVote);

    void deletePostVote(int postId, int userId);
}
