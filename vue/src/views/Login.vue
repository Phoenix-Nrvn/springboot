<template>
<div class="wrapper">
  <!-- 白色窗口，上下空200px， 左右自动， 背景白色，宽度350px，高度300px 里边内容和外边框相距20px，圆角弧度 -->
  <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
    <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
    <!-- 整个校验过程，分为四步哦！注意，这里通过ref来操作表单 -->
    <el-form :rules="rules" :model="user" ref="userForm">
      <el-form-item prop="username">
        <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="user.username"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="user.password"></el-input>
      </el-form-item>
      <el-form-item>
        <div style="margin: 10px 0; text-align: center">
        <el-button type="primary" size="small" autocomplete="off" @click="login()">登录</el-button>
        <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      user: {
        username: '',
        password: ''
      },
      rules: {
        username: [
            /* blur：鼠标失焦时，会触发校验*/
          {required: true, message: "请输入用户名", trigger: 'blur'}
        ],
        password: [
          {required: true, message:"请输入密码", trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    /* 将user对象发送到后台进行判断 */
    login() {
      // 通过ref属性名，可获取表单
      this.$refs['userForm'].validate((valid) => {
        // 表单校验合法
        if (valid) {
          this.request.post("/user/login", this.user).then(res => {
            if (res.code === '200') {
              // 以字符串形式，存储用户信息到浏览器
              localStorage.setItem("user", JSON.stringify(res.data))
              this.$router.push("/home")
              this.$message.success("登录成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style>
  .wrapper {
    /* vh 是个单位，表示视图窗口的高度，这里是100%的窗口高度*/
    height: 100vh;
    /*从左上角往右下角去渐变*/
    background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
    overflow: hidden;
  }
</style>