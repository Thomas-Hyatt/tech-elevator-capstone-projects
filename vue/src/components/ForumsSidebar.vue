<template>
  <div>
    <active-forums />
    <my-forums />
  </div>
</template>

<script>
import ActiveForums from "./ActiveForums.vue";
import MyForums from "./MyForums.vue";
export default {
  components: { ActiveForums, MyForums },
};
</script>

<style></style>
