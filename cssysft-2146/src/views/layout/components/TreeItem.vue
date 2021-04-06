<template>
  <el-submenu v-if="menu.children" :index="menu.path">
    <template #title><i :class="menu.icon"></i>{{ menu.title }}</template>
    <template v-for="item in menu.children" :key="item.id">
      <tree-item :menu="item"></tree-item>
    </template>
  </el-submenu>
  <el-menu-item v-else :index="menu.path" :route="menu.route">
    <template #title><i :class="menu.icon"></i>{{ menu.title }}</template>
  </el-menu-item>
</template>

<script>
export default {
  name: "TreeItem",
  props: {
    menu: Object
  }
}
</script>

<style>

</style>