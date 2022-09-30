// 跨域配置
const {serverIp} = require("../../public/config");
module.exports = {
    // 设置本地默认端口
    devServer: {
        port: 9876,
        proxy: {
            // 设置拦截器，拦截器格式 斜杠+拦截器名字
            '/api': {
                // 代理的目标地址
                target: `http://${serverIp}:9999`,
                // 是否设置同源
                changeOrigin: true,
                // 路径重写
                pathRewrite: {
                    // 选择忽略拦截器里面的内容
                    '^/api': ''
                }
            }
        }
    }
}