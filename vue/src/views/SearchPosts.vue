<template>
  <div class="search">
    <div id="body">
      <searched-posts id="post-list" />
    </div>
  </div>
</template>

<script>
import SearchedPosts from "../components/SearchedPosts.vue";
export default {
  components: { SearchedPosts },
  created() {
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
};
</script>

<style scoped>
#body {
  display: flex;
}
#post-list {
  justify-content: center;
  width: 100%;
}
</style>
