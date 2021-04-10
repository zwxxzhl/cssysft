/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
**/
import Layout from '../views/layout/Layout.vue'

export const constantRoutes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/dashboard',
    meta: { title: '项目管理', icon: '' },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/index.vue'),
        meta: { title: '管理页', icon: '' }
      },
    ]
  },
  {
    path: '/login',
    component: () => import('../views/login/index.vue'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('../views/404.vue'),
    hidden: true
  },

  // // 首页
  // {
  //   path: '/',
  //   component: Layout,
  //   children: [{
  //     name: 'Dashboard',
  //     path: 'dashboard',
  //     component: () => import('@/views/dashboard/index'),
  //     meta: { title: '项目管理首页', icon: 'dashboard' }
  //   }]
  // }
]
/**
 * 动态路由
 */
// const asyncRoutes = [
//   {
//     path: '/acl',
//     component: Layout,
//     redirect: '/acl/user/list',
//     name: '权限管理',
//     meta: { title: '权限管理', icon: 'chart' },
//     children: [
//       {
//         path: 'user/list',
//         name: '用户管理',
//         component: () => import('@/views/acl/user/list'),
//         meta: { title: '用户管理' }
//       },
//       {
//         path: 'role/list',
//         name: '角色管理',
//         component: () => import('@/views/acl/role/list'),
//         meta: { title: '角色管理' }
//       },
//       {
//         path: 'role/form',
//         name: '角色添加',
//         component: () => import('@/views/acl/role/form'),
//         meta: { title: '角色添加' },
//         hidden: true
//       },
//       {
//         path: 'role/update/:id',
//         name: '角色修改',
//         component: () => import('@/views/acl/role/form'),
//         meta: { title: '角色修改' },
//         hidden: true
//       },
//       {
//         path: 'role/distribution/:id',
//         name: '角色权限',
//         component: () => import('@/views/acl/role/roleForm'),
//         meta: { title: '角色权限' },
//         hidden: true
//       },
//       {
//         path: 'menu/list',
//         name: '菜单管理',
//         component: () => import('@/views/acl/menu/list'),
//         meta: { title: '菜单管理' }
//       },
//       {
//         path: 'user/add',
//         name: '用户添加',
//         component: () => import('@/views/acl/user/form'),
//         meta: { title: '用户添加' },
//         hidden: true
//       },
//       {
//         path: 'user/update/:id',
//         name: '用户修改',
//         component: () => import('@/views/acl/user/form'),
//         meta: { title: '用户修改' },
//         hidden: true
//       },
//       {
//         path: 'user/role/:id',
//         name: '用户角色',
//         component: () => import('@/views/acl/user/roleForm'),
//         meta: { title: '用户角色' },
//         hidden: true
//       }

//     ]
//   },

//   { path: '*', redirect: '/404', hidden: true }
// ]

export default constantRoutes