<template>
  <h1>Logout</h1>
</template>

<script>
export default {
  created() {
    this.$store.commit("LOGOUT");
    this.$router.push("/");
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
};
</script>
