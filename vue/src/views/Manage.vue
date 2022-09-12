<template>
    <el-container style="min-height: 100vh">

      <el-aside :width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%);">
      <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow" :menus="user.menus"/>
      </el-aside>

      <el-container>
        <el-header style="border-bottom: 1px solid #ccc;">
          <Header :collapseBtnClass="collapseBtnClass"
                  @asideCollapse="collapse" :user="user" />
        </el-header>

        <el-main>
          <!-- 表示当前页面的子路由，即children会在router-view里面展示 -->
          <router-view @refreshUser = "getUser"/>
        </el-main>

      </el-container>
    </el-container>
</template>

<script>

import Aside from "../components/Aside";
import Header from "../components/Header";

export default {
  name: 'Home',
  data() {
    return { // 默认情况
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false, //默认是展开
      sideWidth: 200,
      logoTextShow: true, // 默认是展开
      user: {},
    }
  },

  components: {
    Aside,
    Header
  },

  created() {
    // 从后台获取最新的User数据
    this.getUser()
  },

  methods: {
    collapse() { // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse // 取反
      if (this.isCollapse) { // 当收缩时，修改宽度，更改标签样式，将文本收起来
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else { // 展开
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    getUser() {
      // 从后台的缓存中拿到username，再通过username去获取到后台的头像信息，因为原来的user中没有头像信息
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      // 从后台获取User数据，传递给Header组件
      this.request.get("/user/username/" + username).then(res => {
        // 重新赋值后台最新的User数据
        this.user = res.data
      })
    }
  }
};
</script>

