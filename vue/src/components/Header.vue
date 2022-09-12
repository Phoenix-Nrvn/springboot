<template>
  <div style="font-size: 12px; line-height: 60px; display: flex">
    <div style="flex: 1; font-size: 18px">
      <span :class="collapseBtnClass" style="cursor: pointer" @click="isCollapse"></span>

      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="'/home'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
      <div style="display: inline-block">
        <img style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px"
             :src="user.avatarUrl" alt="avatar" >
        <span>{{ user.nickname }}</span>
        <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <!-- <router-link to="/person">个人信息</router-link> -->
          <span style="text-decoration: none" @click="toPerson">个人信息</span>
          <!-- <router-link to="/person"></router-link> -->
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    isCollapse: Boolean,
    user: Object,
  },
  computed: {
    currentPathName() {
      // 需要监听的数据
      return this.$store.state.currentPathName;
    }
  },
  watch: {
    currentPathName (newVal, oldVal) {
      console.log(newVal)
    }
  },
  data() {
    return {
      // user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")):{}
    }
  },
  methods: {
    logout() {
      this.$store.commit("logout")
      this.$message.success("退出成功")
    },
    toPerson() {
      this.$router.push("/person")
    }
  }
}
</script>

<style scoped>

</style>