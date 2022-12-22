<template>
  <div>
    <ul
      v-for="(principalReplyObject, index) in nestedReplies"
      :key="index"
      class="replies"
    >
      <Reply
        v-bind="{
          reply: principalReplyObject.reply,
          subReplies: principalReplyObject.subReplies,
        }"
      />
    </ul>
  </div>
</template>

<script>
import Reply from "./Reply.vue";

export default {
  props: {
    replies: Array,
  },
  components: { Reply },
  methods: {
    createNestedReplies() {
      // create array of sub-replies
      console.log("method activated");
      console.log(this.replies);
      const subReplies = this.replies.filter(
        (reply) => reply.replyToReplyId != 0
      );
      const subRepliesLength = subReplies.length;

      // add all direct post replies to nested replies
      let nestedReplies = [];
      this.replies.forEach((currentReply) => {
        if (currentReply.replyToReplyId == 0) {
          const replyObject = {
            reply: currentReply,
          };
          nestedReplies.push(replyObject);
        }
      });

      // Recursively add the sub-replies to the nested replies. Stop after a certain point
      let subReplyIndex = 0;
      let loop_count = 0;
      while (subReplies.length > 0 && loop_count < subRepliesLength * 100) {
        const subReplyObject = subReplies[subReplyIndex]; // The sub-reply object to be added
        const aggregationResult = this.aggregate(subReplyObject, nestedReplies);
        nestedReplies = aggregationResult[0]; // update nested replies array
        let replyAdded = aggregationResult[1]; // defines whether the reply has been added
        // if a sub-reply is successfully added, remove the sub-reply from the sub-reply array
        if (replyAdded) {
          subReplies.splice(subReplyIndex, 1);
          // else, move on to the next sub-reply object
        } else {
          if (subReplyIndex + 1 < subReplies.length) {
            subReplyIndex++;
          }
        }

        // start over and start from beginning when reaching the end
        if (subReplyIndex == subReplies.length - 1) {
          subReplyIndex = 0;
        }
        loop_count++;
      }
      return nestedReplies;
    },

    // Recursively search within a nested comment object array to find the target reply and add the sub-reply to it
    aggregate(replyObjectToAdd, repliesArray, replyAdded = false) {
      const newRepliesArray = repliesArray.map((replyObject) => {
        const newReplyObject = replyObject;

        // If the current reply object is the one being replied to by the sub-reply
        if (replyObject.reply.replyId == replyObjectToAdd.replyToReplyId) {
          const principalSubReplyObjectToAdd = {
            reply: replyObjectToAdd,
          };
          // If the current reply object has no sub-replies yet
          if (!replyObject.subReplies) {
            newReplyObject.subReplies = [principalSubReplyObjectToAdd];
          } else {
            newReplyObject.subReplies.push(principalSubReplyObjectToAdd);
          }
          replyAdded = true;

          // If the current reply object is not the one being replied to, search deeper to see if any sub-reply objects is the one being replied to
        } else {
          if (replyObject.subReplies) {
            const aggregationResult = this.aggregate(
              replyObjectToAdd,
              replyObject.subReplies
            );
            newReplyObject.subReplies = aggregationResult[0];
            if (aggregationResult[1]) {
              replyAdded = true;
            }
          }
        }

        return newReplyObject;
      });

      return [newRepliesArray, replyAdded];
    },
  },
  computed: {
    nestedReplies() {
      return this.createNestedReplies();
    },
  },
};
</script>

<style>
.replies {
  margin-left: 4%;
  text-indent: 10%;
  text-align: left;
}
</style>
