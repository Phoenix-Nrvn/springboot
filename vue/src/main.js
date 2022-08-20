import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './assets/global.css'
import request from "./utils/request";

Vue.config.productionTip = false
// 增加size之后，将会使用适合的大小稍微小一点的版本
Vue.use(ElementUI, {size: "mini"});

// 配置全局变量request
Vue.prototype.request = request

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
