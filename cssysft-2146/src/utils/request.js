import axios from 'axios'
import {ElMessage, ElMessageBox} from 'element-plus'
import store from '../store'
import {getToken} from '@/utils/auth'
import de from "element-plus/packages/locale/lang/de";


// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_BASE_API, // api 的 base_url
  timeout: 30000 // 请求超时时间 毫秒
})

// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters['user/token']) {
      config.headers['token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
)

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * code为非20000是抛错 可结合自己业务进行修改
     */
    const res = response.data
    if (res.code !== 20000) {
      ElMessage({
        message: res.message,
        type: 'error',
        duration: 3 * 1000
      })
      return Promise.reject('error')
    } else {
      return response.data
    }
  },
  error => {
    console.log('err' + error) // for debug
    ElMessage({
      message: error.message,
      type: 'error',
      duration: 3 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
