import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

/*
 * The authorization header is set for axios when you login but what happens when you come back or
 * the page is refreshed. When that happens you need to check for the token in local storage and if it
 * exists you should set the header so that it will be attached to each request
 */
const currentToken = localStorage.getItem("token");
const currentUser = JSON.parse(localStorage.getItem("user"));

if (currentToken != null) {
  axios.defaults.headers.common["Authorization"] = `Bearer ${currentToken}`;
}

export default new Vuex.Store({
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
    }),
  ],
  state: {
    token: currentToken || "",
    user: currentUser || {},

    // The post that the user is currently viewing
    activePostId: 0,
    // The replies of the post that the user is currently viewing
    activeReplies: [],
    activeForumName: "",
    activeForums: [],
    forumUsers: [],
    searchedForums: [],
    posts: [],
    forums: [],
    searchTerm: "",
    searchedPosts: [],
    sortByMostRecent: true,
    userUpVotes: [],
    userDownVotes: [],
    postVotes: [],
    allUserVotes: [],
  },
  mutations: {
    // Authentication Mutations
    SET_AUTH_TOKEN(state, token) {
      state.token = token;
      localStorage.setItem("token", token);
      axios.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    },
    SET_USER(state, user) {
      state.user = user;
      localStorage.setItem("user", JSON.stringify(user));
    },
    LOGOUT(state) {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      state.token = "";
      state.user = {};
      axios.defaults.headers.common = {};
    },

    // Forum Mutations
    SET_FORUMS(state, data) {
      state.forums = data;
    },
    SET_ACTIVE_FORUM(state, forumName) {
      state.activeForumName = forumName;
    },
    SET_ACTIVE_FORUMS(state, data) {
      state.activeForums = data;
    },
    SET_FORUM_USERS(state, data) {
      state.forumUsers = data;
    },
    SAVE_FORUM(state, forum) {
      state.forums.push(forum);
    },
    ADD_FORUM_USER(state, forumUser) {
      state.forumUsers.push(forumUser);
    },
    REMOVE_FORUM_USER(state, forumUser) {
      let index = state.forumUsers.indexOf(forumUser.userId);
      state.forumUsers.splice(index, 1);
    },
    SET_SEARCHED_FORUMS(state, data) {
      state.searchedForums = data;
    },
    PROMOTE_USER_TO_MODERATOR(state, forumUser) {
      let index = state.forumUsers.findIndex((element) => {
        return (
          element.forumId === forumUser.forumId &&
          element.userId === forumUser.userId
        );
      });
      state.forumUsers[index]["moderator"] = true;
    },

    // Post Mutations
    SET_POSTS(state, data) {
      state.posts = data;
    },
    SAVE_POST(state, post) {
      state.posts.push(post);
    },
    SET_ACTIVE_POST(state, postId) {
      state.activePostId = parseInt(postId);
    },
    SET_SEARCH_TERM(state, searchTerm) {
      state.searchTerm = searchTerm;
    },
    SET_SEARCHED_POSTS(state, posts) {
      state.searchedPosts = posts;
    },
    UP_VOTE(state, vote) {
      state.posts = vote;
    },
    DOWN_VOTE(state, vote) {
      state.posts = vote;
    },
    SWITCH_VOTE(state, vote) {
      let index = state.postVotes.findIndex((element) => {
        return element.postId === vote.postId && element.userId === vote.userId;
      });
      state.postVotes[index].upvote = vote.upvote;
    },
    DELETE_VOTE(state, vote) {
      let index = state.postVotes.findIndex((element) => {
        return element.postId === vote.postId && element.userId === vote.userId;
      });
      state.postVotes.splice(index, 1);
    },
    ADD_VOTE(state, vote) {
      state.postVotes.push(vote);
    },
    SET_POST_VOTES(state, data) {
      state.postVotes = data;
    },

    SET_ALL_USER_VOTES(state, data) {
      state.allUserVotes = data;
    },

    DELETE_POST(state, postToDelete) {
      state.posts = state.posts.filter((post) => {
        return post.postId != postToDelete.postId;
      });
    },
    TOGGLE_SORTED_POSTS(state) {
      state.sortByMostRecent = !state.sortByMostRecent;
    },

    SET_SORTED_POSTS_MOST_RECENT(state) {
      state.sortByMostRecent = false;
    },

    ADD_UPVOTED_POSTS(state, data) {
      if (!state.userUpVotes.includes(data)) {
        state.userUpVotes.push(data);
      }
    },
    ADD_DOWNVOTED_POSTS(state, data) {
      if (!state.userDownVotes.includes(data)) {
        state.userDownVotes.push(data);
      }
    },

    ADD_VOTE_COUNT_FOR_POST(state, data) {
      state.posts.forEach((post) => {
        if (post.postId === data) {
          post.votes++;
        }
      });
    },

    SUBTRACT_VOTE_COUNT_FOR_POST(state, data) {
      state.posts.forEach((post) => {
        if (post.postId === data) {
          post.votes--;
        }
      });
    },

    REMOVE_UPVOTED_POST(state, data) {
      state.userUpVotes.splice(state.userUpVotes.indexOf(data), 1);
    },

    REMOVE_DOWNVOTED_POST(state, data) {
      state.userDownVotes.splice(state.userDownVotes.indexOf(data), 1);
    },

    CLEAR_VOTE_ARRAYS(state) {
      state.userUpVotes = [];
      state.userDownVotes = [];
    },

    // Reply Mutations
    SET_ACTIVE_REPLIES(state, data) {
      state.activeReplies = data;
    },
    SAVE_REPLY(state, reply) {
      state.activeReplies.push(reply);
    },
    SET_ACTIVE_NESTED_REPLIES(state, data) {
      state.activeNestedReplies = data;
    },
    DELETE_REPLY(state, replyToDelete) {
      state.activeReplies.forEach((reply) => {
        if (reply.replyId == replyToDelete.replyId) {
          reply.deleted = true;
        }
      });
    },
  },
});
