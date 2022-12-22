<template>
  <div class="forumsTab pink-border">
    <h2><em id="my-forums">My Forums</em></h2>
    <ul class="scrollable">
      <li v-for="forum in myForums" :key="forum.forumName">
        <router-link
          class="highlighted"
          :to="{ name: 'forum-view', params: { forumName: forum.forumName } }"
        >
          <br />{{ forum.forumName }}</router-link
        >
      </li>
    </ul>
  </div>
</template>

<script>
import forumService from "../services/ForumService.js";
export default {
  name: "active-forums",

  methods: {
    getForumUsers() {
      forumService.getForumUsers().then((response) => {
        this.$store.commit("SET_FORUM_USERS", response.data);
      });
    },
  },
  computed: {
    forumUsers() {
      return this.$store.state.forumUsers;
    },
    myForums() {
      return this.forumUsers.filter((forumUser) => {
        return forumUser.userId == this.$store.state.user.id;
      });
    },
    currentUser() {
      return this.$store.state.user.username;
    },
  },
  created() {
    this.getForumUsers();
  },
};
</script>

<style scoped>
#my-forums {
  font-family: "poppins";
  font-weight: bold;
  color: #7c9eb7;
  font-size: 18px;
}
h2 {
  text-align: center;
}
ul {
  list-style: none;
  text-align: center;
  font-family: "inter";
  height: calc(100% - 15px);
}
</style>
