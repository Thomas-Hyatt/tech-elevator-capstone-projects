import axios from "axios";
axios.defaults.baseURL = "http://localhost:9000";

export default {
  getRepliesByPost(postId) {
    return axios.get(`/posts/replies/${postId}`);
  },
  addReply(reply) {
    return axios.post("/replies/", reply);
  },
  deleteReply(replyId) {
    return axios.delete(`/replies/${replyId}`);
  }
};