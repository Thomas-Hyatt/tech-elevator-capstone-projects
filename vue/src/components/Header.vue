<template>
  <div id="header" class="border">
    <router-link v-bind:to="{ name: 'home' }" id="logo">
      <img id="pageLogo" src="../assets/duckie.png" />
      <p id="saiddit" class="highlighted">saiddit</p>
    </router-link>
    <search-bar class="search-bar" />
    <div v-if="$store.state.token != ''">
      <font-awesome-icon icon="fa-solid fa-user" size="lg" />
      {{ $store.state.user.username }}
    </div>
    <router-link v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">
      Logout
    </router-link>
    <router-link v-bind:to="{ name: 'login' }" v-else> Login </router-link>
  </div>
</template>

<script>
import SearchBar from "../components/SearchBar.vue";

export default {
  components: { SearchBar },
};
</script>

<style scoped>
.border {
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  border-bottom-style: groove;
  border-bottom-width: 5px;
  border-bottom-color: #ffaeb5;
  box-shadow: 0 0 5px black;
}
.search-bar {
  margin-top: 10px;
  margin-bottom: -10px;
}
#header {
  display: flex;
  justify-content: space-around;
  width: 100%;
  height: 100px;
}
#pageLogo {
  height: 50px;
  padding: 0 10px 0 10px;
}
div {
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
}
#logo {
  display: flex;
  flex-direction: row;
}
#saiddit {
  font-weight: bold;
  font-size: 40px;
  height: 100%;
}
p {
  font-size: 25px;
  font-family: "Poppins";
  color: #7c9eb7;
  font-weight: bold;
}
</style>
