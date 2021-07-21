import { createRouter, createWebHistory } from 'vue-router'
import constantRoutes from './routes'
import store from '@/store'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie

const PRE_URL = import.meta.env.VITE_PRE_URL

const router = createRouter({
  history: createWebHistory("/"), //history模式使用HTML5模式
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

let flag = 0
router.beforeEach(async(to, from, next) => {
  next()

  /*// start progress bar
  NProgress.start()

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === `${PRE_URL}/login`) {
      // if is logged in, redirect to the home page
      next({ path: `${PRE_URL}/` })
      NProgress.done()
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      const hasRoles = store.getters['user/roles'] && store.getters['user/roles'].length > 0
      // flog handle refresh
      if (flag > 0 && hasRoles) {
        next()
      } else {
        try {
          flag ++
          // get user info
          await store.dispatch('user/GetInfo')
          // generate accessible routes map based on roles
          await store.dispatch('permission/generateRoutes', router)

          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/FedLogOut')
          ElMessage.error(error || 'Has Error')
          next(`${PRE_URL}/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /!* has no token*!/

    let arr = whiteList.filter(f => to.path.indexOf(f) !== -1)
    if (arr.length > 0) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`${PRE_URL}/login?redirect=${to.path}`)
      NProgress.done()
    }
  }*/
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})

export default router;