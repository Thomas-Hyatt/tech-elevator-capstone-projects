<template>
  <div id="login" class="text-center">
    <form class="form-sign-in" @submit.prevent="login">
      <h1 class="h3 mb-3 font-weight-normal">Please Sign In</h1>
      <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
      <div
        class="alert alert-success"
        role="alert"
        v-if="this.$route.query.registration"
      >
        Thank you for registering, please sign in.
      </div>
      <label for="username" class="sr-only">Username</label>
      <input
        type="text"
        id="username"
        class="form-control"
        placeholder="Username"
        v-model="user.username"
        required
        autofocus
      />
      <label for="password" class="sr-only">Password</label>
      <input
        type="password"
        id="password"
        class="form-control"
        placeholder="Password"
        v-model="user.password"
        required
      />
      <router-link class="prompt" :to="{ name: 'register' }">Need an account?</router-link>
      <button class="sign-in" type="submit">Sign in</button>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "login",
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      invalidCredentials: false,
    };
  },
  created() {
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch((error) => {
          const response = error.response;

          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    },
  },
};
</script>

<style>
.form-sign-in {
  display: flex;
  flex-direction: column;
  justify-content: left;
  width: 20%;
  margin-left: 10px;
}

.alert-danger, .alert-success {
  font-family: "inter";
  width: 200%;
  padding: 10px 0 5px 0;
}

.prompt {
  font-family: "inter";
  font-size: 18px;
  padding: 20px 0 0 0;
  width: 120%;
}

label {
  font-family: "inter";
  font-size: 18px;
  width: 120%;
}

#username, #password {
  margin: 0 0 10px 0;
  width: 130%;
	padding: 5px;
	font-size: 18px;
	background-color: rgba(46, 46, 46, 0.2);
	border: none;
	border-radius: 5px;
	box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.25) inset;
	color: rgb(0, 0, 0);
}

.sign-in {
  height: 40px;
  border-radius: 5px;
  background-color: rgb(66, 116, 209);
  box-shadow: 0 0 10px 2px rgb(144, 183, 255) inset;
  border-color: rgb(46, 46, 46);
  color: rgb(206, 231, 255);
  font-family: "inter";
  font-weight: bold;
  font-size: 20px;
}
</style>