<template>
  <div class="votes">
    <div class="up-vote" >
        <button @mouseover="isUpActive=true" @mouseleave="isUpActive=false" @click="upClick=!upClick; downClick=false" class="ui button toggle" >
            <i v-if="toggleUp(upClick) == false && isUpActive == false" >
                <font-awesome-icon :icon="['far', 'circle-up']" size="lg" class="up-color" />
            </i>
            <span v-else>
                <font-awesome-icon :icon="['fas', 'circle-up']" size="lg" class="up-color" />
            </span>
        </button>
    </div>
    <div class="down-vote" >
        <button @mouseover="isDownActive=true" @mouseleave="isDownActive=false" @click="downClick=!downClick; upClick=false" class="ui button toggle" >
            <i v-if="toggleDown(downClick) == false && isDownActive == false" >
                <font-awesome-icon :icon="['far', 'circle-down']" size="lg" class="down-color" />
            </i>
            <span v-else>
                <font-awesome-icon :icon="['fas', 'circle-down']" size="lg" class="down-color" />
            </span>
        </button>
        </div>
    </div>
</template>

<script>
export default {
data() {
    return {
      isUpActive: false,
      isDownActive: false,
      upClick: false,
      downClick: false
    }
  },
  methods: {
    toggleUp(clicked) {
      if (this.upClick == true) {
        clicked = true;
        return clicked;
      } else {
        clicked = false
        return clicked;
      }
    },
    toggleDown(clicked) {
      if (this.downClick == true) { 
        clicked = true;
        return clicked;
      } else {
        clicked = false
        return clicked;
      }
    },
    upVote() {
      this.upClick = true;
      this.$store.commit("UP_VOTE", this.upClick=true);
    },
    downVote() {
      this.downClick = true;
      this.$store.commit("DOWN_VOTE", this.downClick=true);
    },
  }
}
</script>

<style scoped>
button {
    background-color: transparent;
    background-repeat: no-repeat;
    border: none;
    cursor: pointer;
    overflow: hidden;
    outline: none;
    font-size: 17px;
    width: 40%;
    height: 70%;
}

.inline, .votes {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>