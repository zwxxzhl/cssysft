export default [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/views/layout/Layout.vue'), //路由懒加载
  },
  {
    path: '/hello',
    name: 'HelloWorld',
    component: () => import('@/components/HelloWorld.vue'), //路由懒加载
  },
];