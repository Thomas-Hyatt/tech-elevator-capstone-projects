package com.techelevator.dao;

import com.techelevator.model.Reply;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcReplyDao implements ReplyDao {

    private final JdbcTemplate jdbcTemplate;
    UserDao userDao;
    User userTo;
    User userFrom;

    public JdbcReplyDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.userDao = new JdbcUserDao(jdbcTemplate);
    }

    @Override
    public Reply getReplyById(int id) {
        String sql = "select reply_id, user_from_id, reply_to_id, post_id from replies where reply_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        Reply reply = new Reply();

        while (rowSet.next()) {
            reply = mapRowToReply(rowSet);
        }

        return reply;
    }

    @Override
    public List<Reply> listAllReplies() {
        String sql = "select * from replies;";
        List<Reply> allReplies = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next()) {
            allReplies.add(mapRowToReply(rowSet));
        }

        return allReplies;
    }

    @Override
    public List<Reply> tempListRepliesByPost(int id) {
        String sql = "select * from replies where post_id = ?;";
        List<Reply> allReplies = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);

        while (rowSet.next()) {
            allReplies.add(mapRowToReply(rowSet));
        }

        return allReplies;
    }

    @Override
    public List<Reply> listRepliesByPost(int postId) {
        String sql = "select * from replies where post_id = ? AND reply_to_id IS NULL;";
        List<Reply> originalReplies = new ArrayList<>();
        List<Reply> subReplies = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, postId);

        while (rowSet.next()) {
            originalReplies.add(mapRowToReply(rowSet));
        }
        sql = "SELECT * FROM replies WHERE post_id = ? AND reply_to_id IS NOT NULL;";
        rowSet = jdbcTemplate.queryForRowSet(sql, postId);
        while(rowSet.next()) {
            subReplies.add(mapRowToReply(rowSet));
        }

        return createNestedReplies(originalReplies, subReplies);
    }



    @Override
    public List<Reply> listRepliesByUser(int userId) {
        String sql = "select * from replies where user_from_id = ?;";
        List<Reply> allReplies = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

        while (rowSet.next()) {
            allReplies.add(mapRowToReply(rowSet));
        }

        return allReplies;
    }

    @Override
    public int getUserIdFromReplyId(int replyId) {
        String sql = "select user_from_id from replies where reply_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, replyId);
        int userId = 0;

        if (rowSet.next()) {
            userId = rowSet.getInt("user_from_id");
        }

        return userId;
    }

    @Override
    public Reply reply(Reply newReply) {
        String sql = "";
        Integer newReplyId = null;
        try {
            if (newReply.getReplyToReplyId() == 0) { // if reply_to_id = null
                sql = "insert into replies (user_from_id, post_id, text, media_link) " +
                        "values (?, ?, ?, ?) returning reply_id;";
                newReplyId = jdbcTemplate.queryForObject(sql, Integer.class, newReply.getUserFrom(),
                        newReply.getPostId(), newReply.getReplyText(), newReply.getMediaLink());
            } else if (newReply.getMediaLink() == null) { // if media_link = null
                sql = "insert into replies (user_from_id, reply_to_id, post_id, text) values (?, ?, ?, ?) returning reply_id;";
                newReplyId = jdbcTemplate.queryForObject(sql, Integer.class, newReply.getUserFrom(),
                        newReply.getReplyToReplyId(), newReply.getPostId(), newReply.getReplyText());
            } else if (newReply.getReplyText() == null) { // if text = null
                sql = "insert into replies (user_from_id, reply_to_id, post_id, media_link) " +
                        "values (?, ?, ?, ?) returning reply_id;";
                newReplyId = jdbcTemplate.queryForObject(sql, Integer.class, newReply.getUserFrom(),
                        newReply.getReplyToReplyId(), newReply.getPostId(), newReply.getMediaLink());
            } else if (newReply.getPostId() == 0) { // if post_id = null
                return null;
            } else if (newReply.getMediaLink() == null && newReply.getReplyText() == null) { // if media_link && text = null
                return null;
            } else { // if all values != null
                sql = "insert into replies (user_from_id, reply_to_id, post_id, text, media_link) " +
                        "values (?, ?, ?, ?, ?) returning reply_id;";
                newReplyId = jdbcTemplate.queryForObject(sql, Integer.class, newReply.getUserFrom(),
                        newReply.getReplyToReplyId(), newReply.getPostId(), newReply.getReplyText(), newReply.getMediaLink());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (newReplyId != null) {
            newReply.setReplyId(newReplyId);
        } else {
            return null;
        }

        return newReply;
    }

    @Override
    public void deleteReply(int id) {
        String sql = "delete from reply_votes where reply_id = ?;";
        jdbcTemplate.update(sql, id);
        sql = "UPDATE replies SET is_deleted = true WHERE reply_id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void editReply(int replyId, Reply newReply) {
        if (replyId == newReply.getReplyId())
        try {
            String sql = "UPDATE replies SET text = ?, media_link = ? WHERE reply_id = ?;";
            jdbcTemplate.update(sql, newReply.getReplyText(), newReply.getMediaLink(), replyId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private List<Reply> createNestedReplies(List<Reply> originalReplies, List<Reply> subReplies) {
        int subReplyIndex = 0;
        int count = 0;
        while(subReplies.size() > 0 && count < subReplies.size() * 100) {
            Reply subReply = subReplies.get(subReplyIndex);
            boolean aggregationResult = aggregate(subReply, originalReplies);
            if (aggregationResult) {
                subReplies.remove(subReplyIndex);
            }
            if (!aggregationResult && (subReplyIndex + 1) < subReplies.size()) {
                subReplyIndex++;
            }
            if (subReplyIndex == subReplies.size() - 1) {
                subReplyIndex = 0;
            }
            count++;
        }
        return originalReplies;
    }

    private boolean aggregate(Reply replyObjectToAdd, List<Reply> repliesList) {
        boolean replyAdded = false;
        for(Reply reply : repliesList) {
            if (reply.getReplyId() == replyObjectToAdd.getReplyToReplyId()) {
                reply.addNewSubReply(replyObjectToAdd);
                replyAdded = true;
            } else if (!replyObjectToAdd.getSubReplies().isEmpty()){
                if(aggregate(replyObjectToAdd, repliesList)) {
                    replyAdded = true;
                }
            }
        }
        return replyAdded;
    }

    private Reply mapRowToReply(SqlRowSet rowSet) {
        Reply reply = new Reply();

        reply.setReplyId(rowSet.getInt("reply_id"));
        reply.setUserFrom(rowSet.getInt("user_from_id"));
        reply.setUsernameFrom(userDao.getUserById(reply.getUserFrom()).getUsername());
        reply.setReplyToReplyId(rowSet.getInt("reply_to_id"));
        if (reply.getReplyToReplyId() != 0) {
            reply.setUsernameTo(userDao.getUserById(getUserIdFromReplyId(reply.getReplyToReplyId())).getUsername());
        }
        reply.setPostId(rowSet.getInt("post_id"));
        reply.setReplyText(rowSet.getString("text"));
        reply.setMediaLink(rowSet.getString("media_link"));
        reply.setDeleted(rowSet.getBoolean("is_deleted"));
        if (rowSet.getDate("date_time") != null) {
            reply.setDateTime(rowSet.getDate("date_time").toLocalDate());
        }
        return reply;
    }

}
