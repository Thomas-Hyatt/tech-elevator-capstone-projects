<template>
  <div id="register" class="text-center">
    <form class="form-register" @submit.prevent="register">
      <h1 class="h3 mb-3 font-weight-normal">Create Account</h1>
      <div class="alert alert-danger" role="alert" v-if="registrationErrors">
        {{ registrationErrorMsg }}
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
      <input
        type="password"
        id="confirm-pw"
        class="form-control"
        placeholder="Confirm Password"
        v-model="user.confirmPassword"
        required
      />
      <router-link class="prompt" :to="{ name: 'login' }">Have an account?</router-link>
      <button id="create-acc" class="btn btn-lg btn-primary btn-block" type="submit">
        Create Account
      </button>
    </form>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  name: "register",
  data() {
    return {
      user: {
        username: "",
        password: "",
        confirmPassword: "",
        role: "user",
      },
      registrationErrors: false,
      registrationErrorMsg: "There were problems registering this user.",
    };
  },
  created() {
    this.$store.commit("SET_ACTIVE_FORUM", "");
  },
  methods: {
    register() {
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = "Password & Confirm Password do not match.";
      } else {
        authService
          .register(this.user)
          .then((response) => {
            if (response.status == 201) {
              this.$router.push({
                path: "/login",
                query: { registration: "success" },
              });
            }
          })
          .catch((error) => {
            const response = error.response;
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = "Bad Request: Validation Errors";
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = "There were problems registering this user.";
    },
  },
};
</script>

<style>
.form-register {
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

.form-control {
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

#create-acc {
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
