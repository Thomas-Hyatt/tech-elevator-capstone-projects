<template>
  <div>
    <h1 class="title pink-border">Create A Post</h1>
    <form class="create-post" v-on:submit.prevent>
      <label for="select-form">Select a forum to post in</label>
      <select class="select-forum" v-model="selectedForum">
        <option v-for="forum in forums" :key="forum.name" :value="forum">
          {{ forum.name }}
        </option>
      </select>
      <label for="title-input">Give your post a title</label>
      <input
        class="title-input"
        type="text"
        placeholder="Title"
        v-model="post.title"
      />
      <label id="media-label" for="media-input">Link any media of your choice</label>
      <input
        class="media-input"
        type="text"
        placeholder="Media-Link"
        v-model="post.mediaLink"
      />
      <label for="text-input">Add some text</label>
      <textarea
        class="text-input"
        placeholder="Text"
        v-model="post.text"
      />
      <button id="add-post" type="submit" v-on:click="savePost()">Add Post</button>
    </form>
  </div>
</template>

<script>
import forumService from "../services/ForumService.js";
import postService from "../services/PostService.js";

export default {
  name: "create-post",
  data() {
    return {
      selectedForum: {
        forumId: 0,
        name: "",
        description: "",
        dateCreated: "",
      },
      post: {
        userId: this.$store.state.user.id,
        username: this.$store.state.user.username,
        forumId: 0,
        forumName: "",
        title: "",
        text: "",
        mediaLink: "",
      },
    };
  },
  computed: {
    forums() {
      return this.$store.state.forums;
    },
  },
  methods: {
    getForums() {
      forumService.getForums().then((response) => {
        this.$store.commit("SET_FORUMS", response.data);
      });
    },
    savePost() {
      this.$store.commit("SAVE_POST", this.post);
      postService
        .addPost(this.post)
        .then((response) => {
          if (response.status === 201) {
            this.post = {
              userId: this.$store.state.user.id,
              username: this.$store.state.user.username,
              forumId: 0,
              forumName: "",
              title: "",
              text: "",
              mediaLink: "",
            };
            forumService.getActiveForums().then((response) => {
              this.$store.commit("SET_ACTIVE_FORUMS", response.data);
            });
            this.$router.push("/");
          }
        })
        .catch((error) => {
          this.handleErrorResponse(error, "adding");
        });
    },
  },
  watch: {
    selectedForum(newSelectedForum) {
      this.post.forumId = newSelectedForum.forumId;
      this.post.forumName = newSelectedForum.name;
    },
  },

  created() {
    this.getForums();
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
};
</script>

<style>
#media-label {
  width: 200%;
}

label {
  font-family: "inter";
  font-size: 18px;
  padding: 20px 0 5px 0;
  width: 120%;
}

.select-forum {
  border-radius: 5px;
  font-size: 18px;
  height: 37px;
  width: 100%;
	padding: 5px;
	font-size: 18px;
	background-color: rgba(46, 46, 46, 0.2);
	border: none;
	border-radius: 5px;
	box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.25) inset;
	color: rgb(0, 0, 0);
}

.create-post {
  display: flex;
  flex-direction: column;
  justify-content: left;
  width: 20%;
  margin-left: 10px;
}

.title-input, .media-input, .text-input {
  width: 500px;
	padding: 5px;
	font-size: 18px;
	background-color: rgba(46, 46, 46, 0.2);
	border: none;
	border-radius: 5px;
	box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.25) inset;
	color: rgb(0, 0, 0);
}

.media-input {
  width: 500px;
}

.text-input {
  height: 100px;
  width: 500px;
}

#add-post {
  height: 40px;
  border-radius: 5px;
  background-color: rgb(66, 116, 209);
  box-shadow: 0 0 10px 2px rgb(144, 183, 255) inset;
  border-color: rgb(46, 46, 46);
  color: rgb(206, 231, 255);
  font-family: "inter";
  font-weight: bold;
  font-size: 20px;
  margin: 20px 0 0 0;
}
</style>
