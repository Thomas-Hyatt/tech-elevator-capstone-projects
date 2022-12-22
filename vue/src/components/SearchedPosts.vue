<template>
  <div class="posts">
      <post class="searchedPosts" v-for="post in searchedPosts" :key="post.id" :post="post" />
  </div>
</template>

<script>
import Post from "./Post.vue";
import postService from "../services/PostService.js";
export default {
    name: "searched-posts",
    components: { Post },
    methods: {
    getPosts(searchTerm) {
        postService.searchForPosts(searchTerm).then((response) => {
          this.$store.commit("SET_SEARCHED_POSTS", response.data);
        });
    },
    
  },
  computed: {
      searchedPosts() {
          return this.$store.state.searchedPosts;
      }
  },
  created() {
      this.getPosts(this.$store.state.searchTerm);
  }
}
</script>

<style>
.posts {
  text-align: center;
}

.searchedPosts {
  text-align: center;
}
</style>