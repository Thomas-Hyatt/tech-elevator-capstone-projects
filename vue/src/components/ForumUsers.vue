<template>
  <div v-show="onForumPage" class="forumsTab pink-border" id="forum-users">
    <h2>
      <em class="forum-users">{{ forum }} Users</em>
    </h2>
    <ul class="scrollable">
      <li v-for="element in forumUsers" :key="element.userId">
        <br /><img
          v-if="element.moderator"
          class="mod-logo"
          src="../assets/duckie.png"
        />
        <router-link
        id="users"
          class="highlighted"
          :to="{ name: 'user-posts', params: { username: element.username } }"
        >
          {{ element.username }}
        </router-link>

        <button
          id="promote"
          type="submit"
          v-if="!element.moderator && userIsModerator"
          v-on:click="promoteToModerator(element)"
        >
          Promote
        </button>
      </li>
    </ul>
  </div>
</template>

<script>
import forumService from "../services/ForumService.js";
export default {
  name: "forum-users",

  methods: {
    getForumUsers() {
      forumService.getForumUsers().then((response) => {
        this.$store.commit("SET_FORUM_USERS", response.data);
      });
    },
    promoteToModerator(forum) {
      let forumUser = {
        forumId: forum.forumId,
        userId: forum.userId,
      };
      let fullForumUser = {
        forumId: forum.forumId,
        forumName: forum.forumName,
        moderator: true,
        userId: forum.userId,
        username: forum.username,
      };
      this.$store.commit("PROMOTE_USER_TO_MODERATOR", fullForumUser);
      forumService.promoteToModerator(forumUser);
    },
  },
  computed: {
    forums() {
      return this.$store.state.forumUsers;
    },
    forum() {
      return this.$store.state.activeForumName;
    },
    forumUsers() {
      return this.forums.filter((element) => {
        return element.forumName == this.forum;
      });
    },
    currentUser() {
      return this.$store.state.user.username;
    },
    onForumPage() {
      return this.$route.name === "forum-view";
    },
    userIsModerator() {

      if (this.$store.state.user.username == 'admin') {
        return true;
      }
      let obj = this.forumUsers.find((forum) => {
        return this.currentUser === forum.username;
      });
      if (obj != undefined) {
        return obj.moderator;
      } else return false;
    },
  },
  created() {
    this.getForumUsers();
  },
};
</script>

<style scoped>
h2 {
  text-align: center;
}
#promote {
  height: 30px;
  border-radius: 5px;
  background-color: rgb(66, 116, 209);
  box-shadow: 0 0 10px 2px rgb(144, 183, 255) inset;
  border-color: rgb(46, 46, 46);
  color: rgb(206, 231, 255);
  font-family: "inter";
  font-weight: bold;
  font-size: 15px;
  
}
.forum-users {
  font-family: "poppins";
  font-weight: bold;
  color: #7c9eb7;
  font-size: 18px;
}
#forum-users {
  display: flex;
  flex-direction: column;
}
ul {
  position: relative;
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: space-evenly;
  list-style: none;
  text-align: center;
  font-family: monospace;
}
#users {
  list-style: none;
  text-align: center;
  font-family: "inter";
  height: calc(100% - 15px);
}
.mod-logo {
  height: 20px;
}
</style>
