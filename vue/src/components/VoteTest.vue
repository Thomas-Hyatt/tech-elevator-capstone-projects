<template>
  <div id="votes-container">
    <span id="up-votes" class="votesTest"
      ><button
        id="up-button"
        type="submit"
        v-on:click="upVote()"
        :class="userUpVoted ? 'upVoted' : 'upBasic'"
      >
        <img src="../assets/upArrow.png" id="upArrow" />
      </button>
      &nbsp;&nbsp;&nbsp;{{ this.numberOfUpVotes }}&nbsp;&nbsp;&nbsp;</span
    >&nbsp;
    <span id="down-votes" class="votesTest"
      ><button
        id="down-button"
        type="submit"
        v-on:click="downVote()"
        :class="userDownVoted ? 'downVoted' : 'downBasic'"
      >
        <img src="../assets/downArrow.png" id="downArrow" />
      </button>
      &nbsp;&nbsp;&nbsp;{{ this.numberOfDownVotes }}&nbsp;&nbsp;&nbsp;</span
    >
  </div>
</template>

<script>
import PostService from "../services/PostService";
export default {
  props: {
    post: Object,
  },
  created() {
    PostService.getAllPostVotes().then((response) => {
      this.$store.commit("SET_POST_VOTES", response.data);
    });
  },
  computed: {
    userVotes() {
      return this.$store.state.postVotes.filter((element) => {
        return element.userId === this.$store.state.user.id;
      });
    },
    postVotes() {
      return this.$store.state.postVotes.filter((element) => {
        return element.postId === this.post.postId;
      });
    },
    numberOfUpVotes() {
      console.log(this.postVotes);
      return this.postVotes.filter((element) => {
        return element.upvote;
      }).length;
    },
    numberOfDownVotes() {
      return this.postVotes.length - this.numberOfUpVotes;
    },
    userUpVoted() {
      return this.postVotes.some((element) => {
        return element.userId === this.$store.state.user.id && element.upvote;
      });
    },
    userDownVoted() {
      return this.postVotes.some((element) => {
        return element.userId === this.$store.state.user.id && !element.upvote;
      });
    },
  },

  methods: {
    upVote() {
      let vote = {
        userId: this.$store.state.user.id,
        postId: this.post.postId,
        upvote: true,
      };
      if (this.userDownVoted) {
        PostService.updateVote(vote);
        this.$store.commit("SWITCH_VOTE", vote);
      } else if (this.userUpVoted) {
        PostService.deleteVote(vote.postId, vote.userId);
        this.$store.commit("DELETE_VOTE", vote);
      } else {
        PostService.upvotePost(vote);
        this.$store.commit("ADD_VOTE", vote);
      }
    },
    downVote() {
      let vote = {
        userId: this.$store.state.user.id,
        postId: this.post.postId,
        upvote: false,
      };
      if (this.userUpVoted) {
        PostService.updateVote(vote);
        this.$store.commit("SWITCH_VOTE", vote);
      } else if (this.userDownVoted) {
        PostService.deleteVote(vote.postId, vote.userId);
        this.$store.commit("DELETE_VOTE", vote);
      } else {
        PostService.upvotePost(vote);
        this.$store.commit("ADD_VOTE", vote);
      }
    },
  },
};
</script>

<style scoped>
.votesTest {
  display: flex;
  align-items: center;
}
#votes-container {
  display: flex;
  flex-direction: row;
}
.downVoted {
  background-color: firebrick;
}
.upVoted {
  background-color: navy;
}

.upBasic {
  background-color: slategrey;
}
.downBasic {
  background-color: slategrey;
}
.upBasic:hover {
  background-color: navy;
}
.downBasic:hover {
  background-color: firebrick;
}
#upArrow {
  height: 40px;
  padding: 5px 5px 10px 5px;

  background-color: none;
}
#downArrow {
  height: 40px;
  padding: 5px 5px 10px 5px;

  background-color: none;
}
#up-button {
  color: none;
  background-color: none;
  border-radius: 10px;
  border-color: none;
  height: 40px;
  width: 40px;
}
#down-button {
  color: none;
  background-color: none;
  border-radius: 10px;
  border-color: none;
  height: 40px;
  width: 40px;
}
</style>
