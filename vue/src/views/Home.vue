<template>
  <div class="home">
    <div id="body">
      <h1 id="home-title" class="title pink-border">Welcome to saiddit</h1>
      <posts id="post-list" />
    </div>

    <!-- ACTIVE FORUMS ON THE RIGHT SIDE -->

    <!-- POSTS COMPONENT -->
  </div>
</template>

<script>
import posts from "../components/Posts.vue";
export default {
  name: "home",
  components: { posts },

  created() {
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
};
</script>

<style scoped>
#post-list {
  justify-content: center;
  width: 100%;
}
</style>
