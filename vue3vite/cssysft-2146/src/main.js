import { createApp } from 'vue'
import App from '@/App.vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import ElZhCn from 'element-plus/es/locale/lang/zh-cn'

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

import {Search} from '@element-plus/icons-vue'

/*Object.keys(ElPlusIcon).forEach(key => {
  app.component(key, ElPlusIcon[key])
})*/
// todo
app.component('search', Search)

app
    .use(router)
    .use(store)
    .use(ElementPlus, {locale: ElZhCn,})
    .mount('#app')
