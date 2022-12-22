<template>
  <div id="app">
    <div id="div-header">
      <Header id="app-header" />
    </div>
    <div id="container">
      <nav-sidebar id="nav-bar" />
      <router-view class="main-content" />
      <forums-sidebar id="active-forums" />
    </div>
  </div>
</template>

<script>
import ForumsSidebar from "./components/ForumsSidebar.vue";
import Header from "./components/Header.vue";
import NavSidebar from "./components/NavSidebar.vue";

export default {
  components: { Header, NavSidebar, ForumsSidebar },
};
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter&family=Poppins:wght@300&display=swap');

#app {
  background-color: rgb(255, 211, 67);
  height: 100%;
}

textarea, input, select {
  font-family: "inter";
}

.pink-border {
  border-radius: 10px;
  border: groove;
  border-width: 5px;
  border-color: #ffaeb5;
  box-shadow: 0 0 5px black;
}

.title {
  background-color: #ffd3d7;
  text-align: center;
  font-size: 50px;
  font-family: "poppins";
  color: #7c9eb7;
  width: 100%;
  margin: 0 10px 0 10px;
}

.highlighted:hover {
  color: #293daf;
}

#container {
  display: flex;
  justify-content: center;
  top: 100px;
  border-radius: 10px;
  padding-top: 90px;
}

#app-header {
  display: flex;
  justify-content: space-between;
  background-color: #ffd3d7;
  padding: 5px;
  align-items: center;
  border-radius: 0px;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
}

#user {
  padding: 0 2px 0 0;
}

.up-vote,
.down-vote {
  padding: 0 4px 0 4px;
}

.up-color {
  color: rgb(0, 55, 206);
}

.down-color {
  color: rgb(214, 0, 0);
}

.delete-color:hover {
  color: rgb(214, 0, 0);
}

.delete-btn {
  padding: 0.5em 1em;
  background-color: transparent;
  background-repeat: no-repeat;
  color: rgb(23, 31, 138);
  font-weight: bold;
  font-size: 16px;
  text-transform: uppercase;
  cursor: pointer;
}

.forum-name {
  font-family: "inter";
  font-weight: bold;
}

.forumsTab {
  background-color: #ffd3d7;
  width: 150px;
  padding: 10px;
  margin-top: 10px;
  margin-right: 10px;
  margin-left: 10px;
  margin-bottom: 20px;
  border-radius: 10px;
  height: calc(50vh - 70px);
  box-shadow: 0 0 5px black;
}

.sidebar {
  background-color: #ffd3d7;
  width: 150px;
  padding: 10px;
  margin-top: 10px;
  margin-right: 10px;
  margin-left: 10px;
  margin-bottom: 20px;
  border-radius: 10px;
  height: calc(100vh - 100px);
}
#nav-bar {
  position: fixed;
  top: 90px;
  left: 0;
  height: calc(100vh - 120px);
}
#active-forums {
  position: fixed;
  top: 90px;
  right: 0;
  height: calc(100vh - 120px);
}

.main-content {
  margin: 10px 0 10px 0;
  border-radius: 10px;
  width: calc(100vw - 400px);
  min-height: calc(100vh - 110px);
}

.posts {
  padding: 10px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.scrollable {
  overflow-y: auto;
}
</style>
