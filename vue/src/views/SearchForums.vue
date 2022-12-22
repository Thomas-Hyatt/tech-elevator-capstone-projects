<template>
  <div class="searchForums">
    <searched-forums />
  </div>
</template>

<script>
import SearchedForums from "../components/SearchedForums.vue";
export default {
  components: { SearchedForums },

  created() {
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
};
</script>

<style></style>
