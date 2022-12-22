package com.techelevator.dao;

import com.techelevator.model.Reply;

import java.util.List;

public interface ReplyDao {

    int getUserIdFromReplyId(int replyId);

    Reply reply(Reply newReply);

    void deleteReply(int id);

    void editReply(int replyId, Reply newReply);

    List<Reply> listAllReplies();

    List<Reply> tempListRepliesByPost(int id);

    List<Reply> listRepliesByPost(int id);

    List<Reply> listRepliesByUser(int id);

    Reply getReplyById(int id);
}
