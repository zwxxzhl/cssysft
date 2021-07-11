import { constantRoutes } from '@/router/routes'
import { getMenu } from '@/api/login'
import Layout from '@/views/layout/Layout.vue'

const coms = import.meta.globEager('../../views/**/**.vue')

const PRE_URL = import.meta.env.VITE_PRE_URL

function filterAsyncRouter(asyncRouterMap, router, routeParent) { // 遍历后台传来的路由字符串，转换为组件对象
  try {
    const accessedRouters = asyncRouterMap.filter(route => {
      if (route.component) {
        if (route.component === 'Layout') { // Layout组件特殊处理
          route.component = Layout
        } else {
          const component = route.component

          // route.component = () => import(`../../views${component}.vue`)
          route.component = coms[`../../views${component}.vue`].default
        }

        routeParent && (route.path =  routeParent.path + '/' + route.path) || (route.path = `${PRE_URL}` + route.path)
        routeParent && router.addRoute(routeParent.name, route) || router.addRoute(route)
      }
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, router, route)
      }
      return true
    })
    return accessedRouters
  } catch (e) {
    console.log(e)
  }
}

const permission = {
  state: () => ({
    routes: [],
    addRoutes: []
  }),
  getters: {
    routes: state => state.routes,
    addRoutes: state => state.addRoutes
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    }
  },
  actions: {
    async generateRoutes({ commit }, router) {
      // 取后台路由
      const asyncRouter = await getMenu()

      return new Promise(resolve => {
        const tmp = asyncRouter.data.permissionList
        const accessedRoutes = filterAsyncRouter(tmp, router)
        commit('SET_ROUTES', accessedRoutes)
        console.log(accessedRoutes);
        resolve(accessedRoutes)
      })
    }
  }
}

export default permission
