<template>
  <el-sub-menu v-if="menu.children && !menu.hidden" :index="menu.path">
    <template #title>
      <i :class="menu.meta && menu.meta.icon || ''"></i>
      {{ menu.meta && menu.meta.title || '自定义' }}
    </template>
    <template v-for="item in menu.children" :key="item.id">
      <tree-item :menu="item"></tree-item>
    </template>
  </el-sub-menu>
  <el-menu-item v-if="!menu.children && !menu.hidden" :index="menu.path">
    <template #title>
      <i :class="menu.meta && menu.meta.icon || ''"></i>
      {{ menu.meta && menu.meta.title || '自定义' }}
    </template>
  </el-menu-item>
</template>

<script>
export default {
  name: "TreeItem",
  props: {
    menu: Object,
  },
};
</script>

<style>
</style>
