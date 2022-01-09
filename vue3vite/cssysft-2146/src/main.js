import { createApp, markRaw } from 'vue'
import App from '@/App.vue'

import ElementPlus from 'element-plus'
import * as icons from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'

import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'
import relativeTime from 'dayjs/plugin/relativeTime'
dayjs.locale('zh-cn')
dayjs.extend(relativeTime)

import qs from 'qs';

import '@/styles/index.scss' // global css

import router from '@/router'
import store from '@/store'

import { hasBtnPermission } from '@/utils/permission'

let app = createApp(App)
app.config.globalProperties.hasPerm = hasBtnPermission
app.config.globalProperties.dayjs = dayjs
app.config.globalProperties.qs = qs

Object.keys(icons).forEach(key => {
  app.component(transIconName(key), markRaw(icons[key]))
})
function transIconName(iconName) {
  return 'i' + iconName.replace(/[A-Z]/g, (match) => '-' + match.toLowerCase())
}

app.use(router)
    .use(store)
    .use(ElementPlus)
    .mount('#app')
