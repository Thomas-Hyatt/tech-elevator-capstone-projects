<template class="posts">
  <div id="post-container">
    <div id="postBox">
      <div class="inline head-banner">
        <!-- Link to user profile -->
        <h2 id="user">
          <router-link
            class="highlighted"
            :to="{ name: 'user-posts', params: { username: post.username } }"
          >
            <font-awesome-icon :icon="['fas', 'circle-user']" size="lg" />
            {{ post.username }}
          </router-link>
        </h2>
        <h2>in</h2>
        <h3>{{ post.datetime }}</h3>
        <h4>
          <router-link
            class="highlighted forum-name"
            :to="{ name: 'forum-view', params: { forumName: post.forumName } }"
            >[{{ post.forumName }}]</router-link
          >
        </h4>
        <h3>&nbsp;&nbsp;{{ post.dateTime }}</h3>
      </div>
      <div class="posts">
        <router-link
          style="color: navy"
          :to="{
            name: 'post-details',
            params: { forumName: post.forumName, postId: post.postId },
          }"
        >
          <h1 class="post-title">{{ post.title }}</h1>
          <img id="post-image" :src="post.mediaLink" v-show="post.mediaLink" />
          <h3 class="text">{{ post.text }}</h3>
        </router-link>
        <div class="inline">
          <!-- Up-vote & down-vote buttons -->
          <div class="votes">
            <!-- <div class="up-vote">
              <button
                @mouseover="isUpActive = true"
                @mouseleave="isUpActive = false"
                @click="
                  upClick = !upClick;
                  downClick = false;
                  upVote();
                "
                class="ui button toggle"
              >
                <i v-if="toggleUp(upClick) == false && isUpActive == false">
                  <font-awesome-icon
                    :icon="['far', 'circle-up']"
                    size="lg"
                    class="up-color"
                  />
                </i>
                <span v-else>
                  <font-awesome-icon
                    :icon="['fas', 'circle-up']"
                    size="lg"
                    class="up-color"
                  />
                </span></button
              >{{ getUpVotes }}
            </div>
            <div class="down-vote">
              <button
                @mouseover="isDownActive = true"
                @mouseleave="isDownActive = false"
                @click="
                  downClick = !downClick;
                  upClick = false;
                  downVote();
                "
                class="ui button toggle"
              >
                <i
                  v-if="toggleDown(downClick) == false && isDownActive == false"
                >
                  <font-awesome-icon
                    :icon="['far', 'circle-down']"
                    size="lg"
                    class="down-color"
                  />
                </i>
                <span v-else>
                  <font-awesome-icon
                    :icon="['fas', 'circle-down']"
                    size="lg"
                    class="down-color"
                  />
                </span></button
              >{{ getDownVotes }}
            </div> -->

            <!-- Delete post button -->
            <VoteTest :post="post" id="voteTest" />
            <div id="votes-and-delete">
              <button
                class="delete-btn"
                v-if="
                  this.post.userId == this.$store.state.user.id ||
                  userIsModerator()
                "
                @click="doDelete()"
              >
                <font-awesome-icon
                  class="delete-color"
                  :icon="['fas', 'trash-can']"
                  size="lg"
                />
              </button>
              <confirm-dialogue ref="confirmDialogue"></confirm-dialogue>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import postService from "../services/PostService.js";
import ConfirmDialogue from "../components/ConfirmDialogue.vue";
import VoteTest from "./VoteTest.vue";
// import Votes from './Votes.vue'

export default {
  components: { ConfirmDialogue, VoteTest },
  name: "post",
  props: {
    post: Object,
    votes: Object,
  },
  data() {
    return {
      isUpActive: false,
      isDownActive: false,
      upClick: false,
      downClick: false,
      vote: {
        postId: this.post.postId,
        userId: this.$store.state.user.id,
      },
      upVotes: 0,
      downVotes: 0,
      userUpVoted: false,
      userDownVoted: false,
      userInitiallyUpvoted: false,
      userInitiallyDownvoted: false,
      userId: this.$store.state.user.id,
    };
  },
  methods: {
    toggleUp(clicked) {
      if (this.upClick == true) {
        clicked = true;
        return clicked;
      } else {
        clicked = false;
        return clicked;
      }
    },
    toggleDown(clicked) {
      if (this.downClick == true) {
        clicked = true;
        return clicked;
      } else {
        clicked = false;
        return clicked;
      }
    },
    userIsModerator() {
      if (this.$store.state.user.username == 'admin') {
        return true;
      }
      let array = this.$store.state.forumUsers.filter((element) => {
        return element.forumId == this.post.forumId;
      });

      let index = array.findIndex((element) => {
        return element.userId == this.$store.state.user.id;
      });
      if (index > 0) {
        return array[index]["moderator"];
      } else return false;
    },
    upVote() {
      if (this.userId === undefined) {
        this.$router.push("/login");
      }
      if (this.$store.state.userUpVotes.includes(this.vote.postId)) {
        postService.deleteVote(this.vote.postId, this.vote.userId);
        this.$store.commit("REMOVE_UPVOTED_POST", this.vote.postId);
        this.userUpVoted = false;
      } else if (this.$store.state.userDownVotes.includes(this.vote.postId)) {
        postService.updateVote(this.vote);
        this.$store.commit("REMOVE_DOWNVOTED_POST", this.vote.postId);
        this.$store.commit("ADD_UPVOTED_POSTS", this.vote.postId);
        this.userDownVoted = false;
        this.userUpVoted = true;
      } else {
        postService.upvotePost(this.vote);
        this.$store.commit("ADD_UPVOTED_POSTS", this.vote.postId);
        this.userUpVoted = true;
      }
    },
    downVote() {
      if (this.userId === undefined) {
        this.$router.push("/login");
      }
      if (this.$store.state.userDownVotes.includes(this.vote.postId)) {
        postService.deleteVote(this.vote.postId, this.vote.userId);
        this.$store.commit("REMOVE_DOWNVOTED_POST", this.vote.postId);
        this.userDownVoted = false;
      } else if (this.$store.state.userUpVotes.includes(this.vote.postId)) {
        postService.updateVote(this.vote);
        this.$store.commit("REMOVE_UPVOTED_POST", this.vote.postId);
        this.$store.commit("ADD_DOWNVOTED_POSTS", this.vote.postId);
        this.userUpVoted = false;
        this.userDownVoted = true;
      } else {
        postService.downvotePost(this.vote);
        this.$store.commit("ADD_DOWNVOTED_POSTS", this.vote.postId);
        this.userDownVoted = true;
      }
    },
    deletePost() {
      this.$store.commit("DELETE_POST", this.post);
      postService
        .deletePost(this.post.postId)
        .then(() => {
          this.$router.push("/");
        })
        .catch((error) => {
          this.handleErrorResponse(error, "deleting");
        });
    },
    async doDelete() {
      const ok = await this.$refs.confirmDialogue.show({
        title: "Delete Post",
        message:
          "Are you sure you want to delete this post? It cannot be undone.",
        okButton: "Delete",
      });
      // If you throw an error, the method will terminate here unless you surround it wil try/catch
      if (ok) {
        this.deletePost();
      }
    },
    setVotedPosts() {
      if (this.userId !== undefined) {
        this.$store.state.allUserVotes.forEach((vote) => {
          if (vote.upvote) {
            this.$store.commit("ADD_UPVOTED_POSTS", vote.postId);
          } else {
            this.$store.commit("ADD_DOWNVOTED_POSTS", vote.postId);
          }
          this.hasUserVotedOnPost();
        });
      }
    },
    hasUserVotedOnPost() {
      if (this.userId !== undefined) {
        if (this.$store.state.userUpVotes.includes(this.post.postId)) {
          this.userUpVoted = true;
          this.userInitiallyUpvoted = true;
          this.$store.commit("SUBTRACT_VOTE_COUNT_FOR_POST", this.post.postId);
        } else if (this.$store.state.userDownVotes.includes(this.post.postId)) {
          this.userDownVoted = true;
          this.userInitiallyDownvoted = true;
          this.$store.commit("ADD_VOTE_COUNT_FOR_POST", this.post.postId);
        }
      }
    },
  },
  mounted() {
    this.$store.commit("CLEAR_VOTE_ARRAYS");
    this.setVotedPosts();
    postService.getVotesByPost(this.post.postId).then((response) => {
      this.upVotes = response.data.upvotes;
      this.downVotes = response.data.downvotes;
    });
  },
  computed: {
    amIAModerator() {
      return this.userIsModerator();
    },
    getUpVotes() {
      if (this.userUpVoted) {
        return this.initialUpVotes + 1;
      } else {
        return this.initialUpVotes;
      }
    },
    getDownVotes() {
      if (this.userDownVoted) {
        return this.initialDownVotes + 1;
      } else {
        return this.initialDownVotes;
      }
    },
    initialDownVotes() {
      if (this.userInitiallyDownvoted) {
        return this.post.downvotes - 1;
      } else {
        return this.post.downvotes;
      }
    },
    initialUpVotes() {
      if (this.userInitiallyUpvoted) {
        return this.post.upvotes - 1;
      } else {
        return this.post.upvotes;
      }
    },
  },
  watch: {
    userUpVoted(newValue) {
      if (newValue) {
        this.$store.commit("ADD_VOTE_COUNT_FOR_POST", this.post.postId);
      } else {
        this.$store.commit("SUBTRACT_VOTE_COUNT_FOR_POST", this.post.postId);
      }
    },
    userDownVoted(newValue) {
      if (newValue) {
        this.$store.commit("SUBTRACT_VOTE_COUNT_FOR_POST", this.post.postId);
      } else {
        this.$store.commit("ADD_VOTE_COUNT_FOR_POST", this.post.postId);
      }
    },
  },
};
</script>

<style scoped>
#up-votes {
  display: flex;
}
#votes-and-delete {
  display: flex;
  flex: 1;
}
#voteTest {
  display: flex;
  flex: 1;
}
.link-color {
  text-decoration-color: none;
}
button {
  background-color: transparent;
  background-repeat: no-repeat;
  border: none;
  cursor: pointer;
  overflow: hidden;
  outline: none;
  font-size: 17px;
  width: 80%;
  height: 90%;
  position: static;
}

.head-banner {
  display: flex;
  background-color: #a0c6d3;
  width: 100%;
  height: 100%;
  padding: 7px 0 7px 0;
  border-top-right-radius: 5px;
  border-top-left-radius: 5px;
}

.post-title {
  font-size: 25px;
  font-weight: bold;
}

.inline,
.votes {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
.up-vote {
  display: flex;
  align-items: center;
}
.down-vote {
  display: flex;
  align-items: center;
}

.highlighted {
  text-decoration-line: underline;
}

#post-image {
  width: 500px;
}

#post-container {
  border: groove;
  border-width: 5px;
  border-color: #76acbd;
  background-color: #c5d6db;
  margin-bottom: 10px;
  border-radius: 10px;
  width: 70%;
  box-shadow: 0 0 5px black;
}

#forum-name {
  font-weight: bold;
}
</style>
