<template>
  <div>
    <div class="reply-input">
      <div>
        <label v-if="this.isDirectReply" for="reply-input"
          >Reply to This Post</label
        >
        <div v-else>
          <h2>
            Reply to
            <label
              id="reply-to"
              v-if="!this.targetReply.deleted"
              for="reply-input"
              >@{{ this.targetReply.usernameFrom }}</label
            >

            <label v-else for="reply-input">Reply to deleted reply</label>
          </h2>
        </div>
      </div>
      <div>
        <textarea id="reply-text" v-model="replyInput.replyText" />

        <!-- <label v-if="this.isDirectReply" class="direct-reply-label" for="direct-reply-input">Reply to This Post</label>
          <div v-else>
            <label class="sub-reply-label" v-if="!this.targetReply.deleted" for="sub-reply-input">Reply to {{this.targetReply.  usernameFrom}}</label>
            <label class="sub-reply-label" v-else for="sub-reply-input">Reply to deleted reply</label>
          </div>
        </div>
        <div>
          <input v-if="this.isDirectReply" type="text" id="direct-reply-input" name="directReplyInput" v-model="replyInput.replyText"/>
          <input v-else type="text" id="sub-reply-input" name="subReplyInput" v-model="replyInput.replyText"/> -->
      </div>
    </div>
    <div>
      <button
        v-if="this.isDirectReply"
        id="direct-reply-button"
        :disabled="this.reply.replyText.length == 0"
        v-on:click="saveReply()"
      >
        Reply
      </button>
      <button
        v-else
        id="sub-reply-button"
        :disabled="this.reply.replyText.length == 0"
        v-on:click="saveReply()"
      >
        Reply
      </button>
    </div>
  </div>
</template>
<script>
import replyService from "../services/ReplyService.js";
export default {
  name: "reply-input",
  data() {
    return {
      replyInput: {
        replyText: "",
        mediaLink: "",
      },
    };
  },
  props: {
    postId: Number,
    isDirectReply: Boolean,
    targetReply: {
      type: Object,
      default: () => ({}),
    },
  },
  computed: {
    reply() {
      return this.createReply(this.replyInput);
    },
  },
  methods: {
    createReply(replyInput) {
      const reply = {};
      if (this.targetReply.replyId) {
        reply.replyToReplyId = this.targetReply.replyId;
      } else {
        reply.replyToReplyId = 0;
      }
      reply.postId = this.postId;
      reply.userFrom = this.$store.state.user.id;
      reply.usernameFrom = this.$store.state.user.username;
      reply.replyText = replyInput.replyText;
      reply.mediaLink = replyInput.mediaLink;
      return reply;
    },
    getReplyWithId() {
      console.log("get reply with id method");
      let replyWithId = {};

      replyService.getRepliesByPost(this.postId).then((response) => {
        const postReplies = response.data;
        console.log(postReplies);
        // find replies retrieved from the database similar to the newly saved one
        const similarReplies = postReplies.filter((postReply) => {
          console.log(postReply.replyId);
          let similar = true;
          if (this.reply.postId != postReply.postId) {
            similar = false;
          } else if (this.reply.userFrom != postReply.userFrom) {
            similar = false;
          } else if (this.reply.replyToReplyId != postReply.replyToReplyId) {
            similar = false;
          } else if (this.reply.replyText != postReply.replyText) {
            similar = false;
          } else if (this.reply.mediaLink != postReply.mediaLink) {
            similar = false;
          }
          return similar;
        });
        console.log(similarReplies);
        console.log(similarReplies.length);
        // if there's only one, that's the one that was just saved
        if (similarReplies.length == 1) {
          console.log("this is activated");
          replyWithId = similarReplies[0];

          // else, find the most recently saved reply
        } else if (similarReplies.size > 1) {
          replyWithId = similarReplies.reduce((accumulator, currentReply) => {
            if (accumulator.replyId >= currentReply.replyId) {
              return accumulator;
            } else {
              return currentReply;
            }
          });
        }

        console.log(replyWithId);

        this.replyInput = {
          replyText: "",
          mediaLink: "",
        };
        this.$store.commit("SAVE_REPLY", replyWithId);

        return replyWithId;
      });
    },
    saveReply() {
      replyService
        .addReply(this.reply)
        .then((response) => {
          if (response.status === 201) {
            this.getReplyWithId();
          }
        })
        .catch((error) => {
          this.handleErrorResponse(error, "adding");
        });
    },
  },
};
</script>
<style>
#reply-to {
  font-family: "Inter";
  text-decoration-line: underline;
  font-weight: bold;
  font-size: 15px;
}
.reply-input {
  font-family: "Inter";
  text-indent: 10%;
  text-align: left;
}
#reply-text {
  border-radius: 5px;
  height: 85px;
  font-family: "Inter";
  display: inline-block;
  width: 50%;
  font-size: 15px;
  padding: 2px 0 0 2px;
  background-color: rgba(46, 46, 46, 0.2);
  border: none;
  border-radius: 5px;
  box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.25) inset;
  color: rgb(0, 0, 0);
}
/* .reply-input {
      text-indent: 10%;
      text-align: left;
    }
    .direct-reply-label {
      display: inline-block;
      width: 300px;
      margin: .5% 2% .5% 5%;
      padding:5px 5px 5px 5px ;
      font-family: Georgia, "Times New Roman", Times, serif;
      color: #2e6c99;
      align-content: center;
    }
    #direct-reply-input {
      display: block;
      margin: auto;
      padding:10px 10px 50px 15px ;
      border: groove;
      border-width: 5px;
      border-color: #76acbd;
      background-color: #c5d6db;
      border-radius: 10px;
      width: 65%;
      box-shadow: 0 0 5px black;

    }
    #direct-reply-button {
      display: inline-block;
      margin: 1% 2% 1% 7.5%;
      padding:5px 5px 5px 5px ;
      align-content: center;
      border: groove;
      border-width: 5px;
      border-color: #76acbd;
      background-color: #c5d6db;
      border-radius: 10px;
      width: 60px;
      box-shadow: 0 0 5px black;
    }

    .sub-reply-label {
      display: block;
      align-content: left;
      width: 300px;
      margin: 5px 5px 5px 6.8%;
      padding:5px 5px 5px 5px ;
      font-family: Georgia, "Times New Roman", Times, serif;
      color: #2e6c99;
    }
    #sub-reply-input {
      display: inline-block;
      padding:10px 10px 25px 15px ;
      border: groove;
      border-width: 5px;
      border-color: #76acbd;
      background-color: #c5d6db;
      border-radius: 10px;
      width: 45%;
      box-shadow: 0 0 5px black;

    }
    #sub-reply-button {
      display: block;
      margin: 1% 2% 1% 10%;
      padding:5px 5px 5px 5px ;
      align-content: center;
      border: groove;
      border-width: 5px;
      border-color: #76acbd;
      background-color: #c5d6db;
      border-radius: 10px;
      width: 60px;
      box-shadow: 0 0 5px black;
    } */
</style>
