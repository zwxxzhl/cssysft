import { createApp } from 'vue'
import App from '@/App.vue'

import ElementPlus from 'element-plus'
import 'element-plus/lib/theme-chalk/index.css'
import locale from 'element-plus/lib/locale/lang/zh-cn'

import '@/styles/index.scss' // global css

import router from '@/router'
import store from '@/store'

import { hasBtnPermission } from '@/utils/permission' // button permission

import dayjs from "dayjs";
import 'dayjs/locale/zh-cn'
import relativeTime from 'dayjs/plugin/relativeTime'
dayjs.locale('zh-cn')
dayjs.extend(relativeTime)

let app = createApp(App)
app.config.globalProperties.hasPerm = hasBtnPermission
app.config.globalProperties.dayjs = dayjs

app
.use(router)
.use(store)
.use(ElementPlus, {locale})
.mount('#app')
