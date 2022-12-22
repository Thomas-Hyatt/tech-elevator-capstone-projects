package com.techelevator.dao;

import com.techelevator.model.PostVote;
import com.techelevator.model.VotesForPostDto;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPostVoteDao implements PostVoteDao {

    JdbcTemplate jdbcTemplate;

    public JdbcPostVoteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void upVotePost(PostVote postVote) {
        String sql = "INSERT INTO post_votes (user_id, post_id, is_upvote) VALUES (?, ?, true)";
        jdbcTemplate.update(sql, postVote.getUserId(), postVote.getPostId());
    }

    @Override
    public void downVotePost(PostVote postVote) {
        String sql = "INSERT INTO post_votes (user_id, post_id, is_upvote) VALUES (?, ?, false)";
        jdbcTemplate.update(sql, postVote.getUserId(), postVote.getPostId());
    }

    @Override
    public List<PostVote> getAllPostVotes() {
        List<PostVote> allPostVotes = new ArrayList<>();

        String sql = "SELECT * FROM post_votes;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while(rowSet.next()) {
            allPostVotes.add(mapRowSetToPostVote(rowSet));
        }
        return allPostVotes;
    }

    @Override
    public List<PostVote> getPostVotesByUser(int userId) {
        List<PostVote> postVotes = new ArrayList<>();

        String sql = "SELECT * FROM post_votes WHERE user_id = ?";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        while(rowSet.next()) {
            postVotes.add(mapRowSetToPostVote(rowSet));
        }
        return postVotes;
    }

    @Override
    public VotesForPostDto getPostVotesByPost(int postId) {

        String sql = "SELECT post_id, COUNT(*) FILTER(WHERE is_upvote) AS upvotes, COUNT(*) FILTER(WHERE NOT is_upvote) " +
                "AS downvotes, (COUNT(*) FILTER(WHERE is_upvote) - COUNT(*) FILTER(WHERE NOT is_upvote)) AS votes " +
                "FROM post_votes WHERE post_id = ? GROUP BY post_id";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, postId);

        if(rowSet.next()) {
            return mapRowSetToVotesForPostDto(rowSet);
        }
        return new VotesForPostDto(postId, 0, 0, 0);
    }

    @Override
    public List<PostVote> getUpVotesByUser(int userId) {
        List<PostVote> postVotes = new ArrayList<>();

        String sql = "SELECT * FROM post_votes WHERE user_id = ? AND is_upvote = true";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        while(rowSet.next()) {
            postVotes.add(mapRowSetToPostVote(rowSet));
        }
        return postVotes;
    }

    @Override
    public List<PostVote> getDownVotesByUser(int userId) {
        List<PostVote> postVotes = new ArrayList<>();

        String sql = "SELECT * FROM post_votes WHERE user_id = ? AND is_upvote = false";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        while(rowSet.next()) {
            postVotes.add(mapRowSetToPostVote(rowSet));
        }
        return postVotes;
    }

    @Override
    public List<PostVote> getUpVotesByPost(int postId) {
        List<PostVote> postVotes = new ArrayList<>();

        String sql = "SELECT * FROM post_votes WHERE post_id = ? AND is_upvote = true";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, postId);

        while(rowSet.next()) {
            postVotes.add(mapRowSetToPostVote(rowSet));
        }
        return postVotes;
    }

    @Override
    public List<PostVote> getDownVotesByPost(int postId) {
        List<PostVote> postVotes = new ArrayList<>();

        String sql = "SELECT * FROM post_votes WHERE post_id = ? AND is_upvote = false";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, postId);

        while(rowSet.next()) {
            postVotes.add(mapRowSetToPostVote(rowSet));
        }
        return postVotes;
    }

    @Override
    public boolean hasUserVotedOnPost(int postId, int userId) {
        String sql = "SELECT * FROM post_votes WHERE post_id = ? AND user_id = ?;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, postId, userId);
        return rowSet.next();
    }

    @Override
    public void updatePostVote(PostVote postVote) {
        String sql = "UPDATE post_votes SET is_upvote = NOT is_upvote WHERE post_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, postVote.getPostId(), postVote.getUserId());
    }

    @Override
    public void deletePostVote(int postId, int userId) {
        String sql = "DELETE FROM post_votes WHERE post_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, postId, userId);
    }

    private PostVote mapRowSetToPostVote(SqlRowSet rowSet) {
        PostVote postVote = new PostVote();
        postVote.setPostId(rowSet.getInt("post_id"));
        postVote.setUserId(rowSet.getInt("user_id"));
        postVote.setUpvote(rowSet.getBoolean("is_upvote"));
        if (rowSet.getDate("date_time") != null) {
            postVote.setTimeDate(rowSet.getDate("date_time").toLocalDate());
        }
        return postVote;
    }

    private VotesForPostDto mapRowSetToVotesForPostDto(SqlRowSet rowSet) {
        VotesForPostDto votesForPostDto = new VotesForPostDto();
        votesForPostDto.setPostId(rowSet.getInt("post_id"));
        votesForPostDto.setUpvotes(rowSet.getInt("upvotes"));
        votesForPostDto.setDownvotes(rowSet.getInt("downvotes"));
        votesForPostDto.setVotes(rowSet.getInt("votes"));
        return votesForPostDto;
    }

}
