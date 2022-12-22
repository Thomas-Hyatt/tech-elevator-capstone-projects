import axios from "axios";
axios.defaults.baseURL = "http://localhost:9000";

export default {
  getPosts() {
    return axios.get("/posts/");
  },

  getPost(forumId, postId) {
    return axios.get(`/${forumId}/posts/${postId}`);
  },

  addPost(post) {
    return axios.post(`/forums/posts/`, post);
  },

  searchForPosts(searchTerm) {
    return axios.get(`/posts/search/${searchTerm}`);
  },

  getVotesByPost(postId) {
    return axios.get(`/posts/${postId}/votes`);
  },

  getAllPostVotes() {
    return axios.get("/posts/votes");
  },

  deletePost(postId) {
    return axios.delete(`/posts/${postId}`);
  },

  upvotePost(vote) {
    return axios.post(`/posts/upvote`, vote);
  },

  downvotePost(vote) {
    return axios.post(`/posts/downvote`, vote);
  },

  getVotesByUser(userId) {
    return axios.get(`/posts/votes/${userId}`);
  },

  hasUserVotedOnPost(postId, userId) {
    return axios.get(`/posts/${postId}/votes/${userId}`);
  },

  getAllVotesByUser(userId) {
    return axios.get(`/posts/votes/${userId}`);
  },

  updateVote(vote) {
    return axios.put(`/posts/vote`, vote);
  },

  deleteVote(postId, userId) {
    return axios.delete(`/posts/votes/${postId}/${userId}`);
  },
};
