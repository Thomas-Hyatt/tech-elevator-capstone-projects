<template>
  <div>
    <!-- If current reply is not deleted, show text & delete reply button -->
    <div v-if="!this.reply.deleted">
      <span class="username">{{ reply.usernameFrom }}</span>
      <span class="reply">: {{ reply.replyText }}</span>
      <!-- Delete reply button -->
      <button
        v-if="this.reply.userFrom == this.$store.state.user.id"
        v-on:click="deleteReply()"
      >
        delete reply
      </button>
    </div>
    <span class="reply" v-else>This reply has been deleted</span>

    <!-- If current reply is not deleted, show replier's username -->
    <reply-input
      class="reply-input"
      :postId="this.reply.postId"
      :isDirectReply="false"
      :targetReply="this.reply"
    />

    <ul class="sub-replies" v-if="subReplies.length">
      <div v-for="(principalReplyObject, index) in subReplies" :key="index">
        <recursive-reply
          v-bind="{
            reply: principalReplyObject.reply,
            subReplies: principalReplyObject.subReplies,
          }"
        />
      </div>
    </ul>
  </div>
</template>

<script>
import replyService from "../services/ReplyService.js";

import replyInput from "./ReplyInput.vue";

export default {
  name: "recursive-reply",
  components: {
    replyInput,
  },
  props: {
    reply: {
      type: Object,
      required: true,
    },
    subReplies: {
      type: Array,
      default: () => [],
    },
  },
  methods: {
    deleteReply() {
      this.$store.commit("DELETE_REPLY", this.reply);
      replyService.deleteReply(this.reply.replyId).catch((error) => {
        this.handleErrorResponse(error, "deleting");
      });
    },
  },
};
</script>

<style>
.sub-replies {
  padding-left: 25px;
}
.username {
  font-family: "inter";
  font-size: 18px;
  font-weight: bold;
  text-decoration-line: underline;
}
.reply {
  font-family: "inter";
  font-size: 18px;
}
</style>
