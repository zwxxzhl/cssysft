<template>
  <el-menu :default-openeds="[menuRoutes[0].path]" router>
    <template v-for="menu in menuRoutes" :key="menu.id">
      <tree-item :menu="menu"></tree-item>
    </template>
  </el-menu>
</template>

<script>
import TreeItem from './TreeItem.vue';
export default {
  components: { TreeItem },
  name: "MenuTree",
  props: {
    menuRoutes: Array
  }
}
</script>

<style>

</style>