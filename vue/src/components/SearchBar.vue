<template>
  <div id="search-bar" class="$info">
    <form class="inline" @submit.prevent="search">
      <font-awesome-icon id="fa-icon" icon="fa-solid fa-magnifying-glass" pull="left" size="xl" />
      <select name="search" id="search-drop-down" v-model="searchOption">
        <option value="posts">Posts</option>
        <option value="forums">Forums</option>
      </select>
      <div class="textbox">
        <input type="text" name="search" id="search" v-model="searchTerm" />&nbsp;
      </div>
      <button type="submit" id="button">Search</button>
    </form>
  </div>
</template>

<script>
import postService from "../services/PostService.js";
import forumService from "../services/ForumService.js";
export default {
  name: "search-bar",
  data() {
    return {
      searchTerm: "",
      searchOption: "posts"
    }
  },
  methods: {
    search() {
      this.$store.commit("SET_SEARCH_TERM", this.searchTerm)
      if (this.$route.path !== `/${this.searchOption}/search`) {
        this.$router.push(`/${this.searchOption}/search`);
      } else if (this.searchOption === "posts") {
        postService.searchForPosts(this.searchTerm).then((response) => {
          this.$store.commit("SET_SEARCHED_POSTS", response.data);
        });
      } else if (this.searchOption === "forums") {
        forumService.searchForums(this.searchTerm).then((response => {
          this.$store.commit("SET_SEARCHED_FORUMS", response.data)
        }))
      }
    }
  }
};
</script>

<style>
#search-bar {
  align-content: center;
}

#fa-icon {
  margin: 6px 10px 0 0;
  align-content: center;
}

#button {
  font-family: "Poppins";
  font-size: 18px;
  font-weight: bold;
  height: 37px;
  width: 20%;
  border-radius: 5px;
  background-color: rgb(66, 116, 209);
  box-shadow: 0 0 10px 0 rgb(144, 183, 255) inset;
  border-color: rgb(46, 46, 46);
  color: rgb(206, 231, 255);
  margin: 0 0 0 9px;
}

#search {
  margin: 0 5px 0 5px;
}

.inline {
  display: flex;
  flex-direction: row;
}

#search-drop-down {
  font-family: "Poppins";
  font-size: 15px;
  height: 37px;
  border-radius: 5px;
  background-color: rgb(255, 181, 181);
	border-radius: 5px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.25) inset;
	color: rgb(0, 0, 0);
  text-align: center;
  width: 20%;
}
 
.textbox {
	position: relative;
	width: 300px;
}
.textbox > input[type=text] {
	width: 100%;
  height: 37px;
	padding: 5px;
	font-size: 20px;
	background-color: rgba(46, 46, 46, 0.2);
	border: none;
	border-radius: 5px;
	box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.25) inset;
	color: rgb(0, 0, 0);
}
.resp-textbox > input[type=text]:focus {
	outline: none;
	box-shadow: 325px 2px 5px rgba(0, 0, 0, 0.25) inset;
}
.textbox > input[type=text]::placeholder {
  color: rgb(0, 0, 0);
}
</style>
