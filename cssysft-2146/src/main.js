import { createApp } from 'vue'
import App from '@/App.vue'

import ElementPlus from 'element-plus'
import 'element-plus/lib/theme-chalk/index.css'
import locale from 'element-plus/lib/locale/lang/zh-cn';

import router from '@/router'
import store from '@/store'

// import '@/permission' // permission control
import { hasBtnPermission } from './utils/permission' // button permission

let app = createApp(App)
app.config.globalProperties.hasPerm = hasBtnPermission

app.use(router).use(store).use(ElementPlus, {locale}).mount('#app')
