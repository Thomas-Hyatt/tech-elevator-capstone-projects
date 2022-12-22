<template>
  <div class="forumsTab pink-border" id="active">
    <h2><em id="forums">Active Forums</em></h2>
    <ul class="scrollable">
      <li v-for="forum in forums" :key="forum.name">
        <router-link
          class="highlighted"
          :to="{ name: 'forum-view', params: { forumName: forum.name } }"
        >
          {{ forum.name }}</router-link
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
    getForums() {
      forumService.getForums().then((response) => {
        this.$store.commit("SET_FORUMS", response.data);
      });
    },
    activeForums() {
      forumService.getActiveForums().then((response) => {
        this.$store.commit("SET_ACTIVE_FORUMS", response.data);
      });
    },
  },
  computed: {
    forums() {
      return this.$store.state.activeForums;
    },
  },
  created() {
    this.activeForums();
    this.getForums();
  },
};
</script>

<style scoped>
#forums {
  font-family: "poppins";
  font-weight: bold;
  color: #7c9eb7;
  font-size: 17px;
}

#active {
  display: flex;
  flex-direction: column;
}

h2 {
  text-align: center;
}

ul {
  position: relative;
  display: flex;
  flex: 1;
  flex-direction: column;
  justify-content: space-evenly;
  list-style: none;
  text-align: center;
  font-family: "inter";
}
</style>
