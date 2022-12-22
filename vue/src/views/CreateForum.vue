<template>
  <div>
    <h1 class="title pink-border">Create A Forum</h1>
    <form class="create-forum" v-on:submit.prevent>
      <label for="name-input">Give your forum a name</label>
      <input class="name-input" type="text" placeholder="Forum Name" v-model="forum.name" />
      <label for="description-input">Describe your forum</label>
      <textarea class="description-input" v-model="forum.description" />
      <button type="submit" id="create-btn" v-on:click="saveForum()">Create Forum</button>
    </form>
  </div>
</template>

<script>
import forumService from "../services/ForumService.js";

export default {
  name: "create-forum",
  data() {
    return {
      forum: {
        name: "",
        description: "",
      },
    };
  },
  computed: {
    newForumUser() {
      return this.createNewForumUser();
    }
  },
  created() {
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
  methods: {
    createNewForumUser() {
      const newForumUser = {};
      newForumUser.forumName = this.forum.name;
      newForumUser.moderator = true;
      newForumUser.userId = this.$store.state.user.id;
      newForumUser.username = this.$store.state.user.username;
      return newForumUser;
    },
    saveForum() {
      this.$store.commit("ADD_FORUM_USER", this.newForumUser);
      this.$store.commit("SAVE_FORUM", this.forum);
      forumService
        .addForum(this.forum)
        .then((response) => {
          if (response.status === 201) {
            // this.$router.push("/");
            this.$router.push("/");
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
.create-forum {
  display: flex;
  flex-direction: column;
  justify-content: left;
  width: 20%;
  margin-left: 10px;
}

label {
  font-family: "inter";
  font-size: 18px;
  padding: 20px 0 5px 0;
}

.description-input, .name-input {
  width: 100%;
	padding: 5px;
	font-size: 20px;
	background-color: rgba(46, 46, 46, 0.2);
	border: none;
	border-radius: 5px;
	box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.25) inset;
	color: rgb(0, 0, 0);
}

.description-input {
  height: 100px;
  width: 400px;
}

#create-btn {
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
