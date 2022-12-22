<template>
  <nav class="forumsTab pink-border">
    <h1 id="nav-title"><em>Navigation</em></h1>
    <ul class="scrollable">
      <li>
        <br />
        <router-link class="highlighted" :to="{ name: 'create-post' }">
          Create Post
        </router-link>
      </li>
      <br />
      <li>
        <router-link class="highlighted" :to="{ name: 'create-forum' }">
          Create Forum
        </router-link>
      </li>
      <br />
      <li>
        <router-link class="highlighted" :to="{ name: 'search-forums' }">
          Browse All Forums
        </router-link>
      </li>
      <br />
    </ul>
  </nav>
</template>

<script>
export default {};
</script>

<style scoped>
ul {
  height: calc(100% - 15px);
  list-style: none;
  font-family: "Inter";
}
ul li {
  text-align: left;
}
#nav-title {
  font-family: "Poppins";
  color: #7c9eb7;
  font-weight: bold;
  font-size: 18px;
}
</style>
