<template>
  <div>
    <nav-bar />
    <forum-users />
  </div>
</template>

<script>
import ForumUsers from "./ForumUsers.vue";
import NavBar from "./NavBar.vue";
export default {
  components: { NavBar, ForumUsers },
};
</script>

<style></style>
