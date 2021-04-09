import { constantRoutes } from '@/router/routes'
import { getMenu } from '@/api/login'
import Layout from '@/views/layout/Layout.vue'

function filterAsyncRouter(asyncRouterMap, path) { // 遍历后台传来的路由字符串，转换为组件对象
  try {
    const accessedRouters = asyncRouterMap.filter(route => {
      if (route.component) {
        if (route.component === 'Layout') { // Layout组件特殊处理
          route.component = Layout
        } else {
          const component = route.component
          route.component = () => import(`../../views${component}.vue`)
        }
        path && (route.path = path + '/' + route.path)
      }
      if (route.children && route.children.length) {
        route.children = filterAsyncRouter(route.children, route.path)
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
    async generateRoutes({ commit }, roles) {
      // 取后台路由
      const asyncRouter = await getMenu()

      return new Promise(resolve => {
        const tmp = asyncRouter.data.permissionList
        const accessedRoutes = filterAsyncRouter(tmp)

        commit('SET_ROUTES', accessedRoutes[0])
        resolve(accessedRoutes[0])
      })
    }
  }
}

export default permission
