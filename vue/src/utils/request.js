import axios from 'axios'
import ElementUI from "element-ui";
import {serverIp} from "../../public/config";

// 通过axios来create了一个request对象
const request = axios.create({
    // 全局统一加上了'/api'前缀，也就是说所有接口都会加上'/api'前缀，页面写接口就不用加了
    baseURL: `http://${serverIp}:9090`,
    timeout: 5000
})

// request拦截器
// 可以自请求发送前对请求做一些处理
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null;
    // 如果user存在，将其放在请求头中
    if (user) {
        // 设置请求头
        config.headers['token'] = user.token;
    }
    return config
}, error=> {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果返回的是文件
        if (response.config.responseType === 'blob') {
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        // 当权限不通过时，给出提示
        if (res.code === '401') {
            ElementUI.Message({
                message: res.msg,
                type: 'error'
            })
        }
        return res;
    },
    error => {
        console.log('err' + error)
        return Promise.reject(error)
    }
)

export default request