import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Manage.vue'
import Manage from "../views/Manage";
import store from "../store";

Vue.use(VueRouter)

const routes = [
    // 固定路由
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '*',
    name: 'NotFound',
    component: () => import('../views/404.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

// 刷新页面会导致路由重置
export const setRoutes = () => {
  // 取出menus数据
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {
    const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/login", children: [
        { path: 'person', name: '个人信息', component: () => import('../views/Person') },
        // { path: 'password', name: '修改密码', component: () => import('../views/Password.vue') },
      ] }
    const menus = JSON.parse(storeMenus);
    // 拼接动态子路由
    menus.forEach(item => {
      // 当且仅当path不为null的时候，采取设置路由
      if (item.path) {
        let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue') }
        manageRoute.children.push(itemMenu)
      } else if (item.children.length) {
        item.children.forEach(item => {
          if (item.path) {
            let itemMenu = {
              path: item.path.replace("/", ""),
              name: item.name,
              component: () => import('../views/' + item.pagePath + '.vue')
            }
            manageRoute.children.push(itemMenu)
          }
        })
      }
    })

    // 获取当前的路由对象名称数组
    const currentRoutes = router.getRoutes().map(v => v.name)
    if (!currentRoutes.includes('Manage')) {
      // 添加到当前路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

// 重置就再set一次路由
setRoutes()

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置当前的路由名称，为了在Header组件中去使用
  localStorage.setItem("currentPathName", to.name)
  // 触发store的数据更新
  store.commit("setPath")
  // 放行路由
  next()
})

export default router
