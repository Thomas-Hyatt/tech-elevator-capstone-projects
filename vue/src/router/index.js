import Vue from "vue";
import Router from "vue-router";
import CreatePost from "../views/CreatePost";
import CreateForum from "../views/CreateForum";
import ForumView from "../views/ForumView.vue";
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Logout from "../views/Logout.vue";
import PostDetails from "../views/PostDetails.vue";
import Register from "../views/Register.vue";
import store from "../store/index";
import UserPosts from "../views/UserPosts";
import SearchPosts from "../views/SearchPosts";
import SearchForums from "../views/SearchForums";

Vue.use(Router);

/**
 * The Vue Router is used to "direct" the browser to render a specific view component
 * inside of App.vue depending on the URL.
 *
 * It also is used to detect whether or not a route requires the user to have first authenticated.
 * If the user has not yet authenticated (and needs to) they are redirected to /login
 * If they have (or don't need to) they're allowed to go about their way.
 */

const router = new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/login",
      name: "login",
      component: Login,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: "/logout",
      name: "logout",
      component: Logout,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: "/register",
      name: "register",
      component: Register,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path:"/posts/search",
      name: "search-posts",
      component: SearchPosts,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path:"/forums/search",
      name: "search-forums",
      component: SearchForums,
      meta: {
        requiresAuth: false,
      }
    },
    {
      path: "/:forumName",
      name: "forum-view",
      component: ForumView,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: "/:forumName/:postId",
      name: "post-details",
      component: PostDetails,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: "/createPost",
      name: "create-post",
      component: CreatePost,
      meta: {
        requiresAuth: true,
      },
    },
    
    {
      path: "/createForum",
      name: "create-forum",
      component: CreateForum,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: "/:username",
      name: "user-posts",
      component: UserPosts,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: "/",
      name: "home",
      component: Home,
      meta: {
        requiresAuth: false,
      },
    },
  ],
});

router.beforeEach((to, from, next) => {
  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some((x) => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === "") {
    next("/login");
  } else {
    // Else let them go to their next destination
    next();
  }
});

export default router;
