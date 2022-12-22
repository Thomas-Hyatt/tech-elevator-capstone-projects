<template>
  <div class="post-container">
    <h1 class="title pink-border">Post Details</h1>
    <div class="posts">
      <Post :post="post" />
    </div>
      <reply-input
        class="reply-input"
        :postId="this.post.postId"
        :isDirectReply="true"
      />
    
    <!-- v-if is required here to make sure when post details page is rendered, the replies passed in has been updated to a nonzero length rather than its default [] state with 0 elements. Without it here, the replies could be unaccessible the first time post-details page is rendered. -->
    <reply-section
      class="reply-section"
      v-if="this.replies.length"
      :replies="this.replies"
    />
  </div>
</template>

<script>
import replyService from "../services/ReplyService.js";
import replySection from "../components/ReplySection.vue";
import replyInput from "../components/ReplyInput.vue";
import Post from "../components/Post.vue";

export default {
  name: "post-details",
  data() {
    return {
      isUpActive: false,
      isDownActive: false,
    };
  },
  components: { Post, replyInput, replySection },
  computed: {
    post() {
      return this.$store.state.posts.find((post) => {
        return post.postId === this.$store.state.activePostId;
      });
    },
    replies() {
      return this.$store.state.activeReplies;
    },
  },
  methods: {
    // update currently active replies in $store
    getReplies() {
      replyService
        .getRepliesByPost(this.$route.params.postId)
        .then((response) => {
          this.$store.commit("SET_ACTIVE_REPLIES", response.data);
        });
    },
  },
  created() {
    this.$store.commit("SET_ACTIVE_POST", this.$route.params.postId);
    this.getReplies();
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
};
</script>

<style scoped>
.title {
  margin-top: -10px;
  margin-right: 0px;
  margin-bottom: 34px;
  margin-left: 0px;
}

.reply-section {
  align-content: center;
  margin-top: 15px;
  padding-top: 15x;
}

#detail-img {
  width: 500px;
}
.text {
  font-family: sans-serif;
  color: royalblue;
  font-size: 20px;
}

.links {
  color: pink;
}
.post-container {
  padding: 10px;
}

#user {
  text-decoration-line: underline;
}
</style>
