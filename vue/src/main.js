import Vue from 'vue'
import App from './App.vue'
import router from './router/index'
import store from './store/index'
import axios from 'axios'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faMagnifyingGlass, faUser, faCircleUp, faCircleDown, faCircleUser, faTrashCan, faMessage } from '@fortawesome/free-solid-svg-icons'
import { faArrowAltCircleUp, faArrowAltCircleDown } from '@fortawesome/free-regular-svg-icons'

library.add(faMagnifyingGlass, faUser, faCircleUp, faArrowAltCircleUp, faArrowAltCircleDown, 
  faCircleDown, faCircleUser, faTrashCan, faMessage)
Vue.component("font-awesome-icon", FontAwesomeIcon, {
  props: ["text"],
  template: "#font-awesome-icon",
  data() {
    return {
      isActive: true
    }
  }
})
Vue.config.productionTip = false

axios.defaults.baseURL = process.env.VUE_APP_REMOTE_API;
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
