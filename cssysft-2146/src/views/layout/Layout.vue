
<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside width="180px">
      <menu-tree :menuRoutes="menuRoutes"></menu-tree>
    </el-aside>

    <el-container>
      <el-header class="header-layout">
        <navbar></navbar>
      </el-header>

      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapGetters } from "vuex";
import MenuTree from "./components/MenuTree.vue";
import Navbar from "./components/Navbar.vue";

export default {
  name: "Layout",
  components: {
    MenuTree,
    Navbar,
  },
  computed: {
    ...mapGetters({
      menuRoutes: "permission/routes",
      sidebar: "app/sidebar",
    }),
  },
  data() {
    return {
    };
  },
};
</script>

<style>
  html,
  body,
  #app,
  .el-container {
    padding: 0px;
    margin: 0px;
    height: 100%;
  }

  .el-header {
    /* background-color: #b3c0d1; */
    color: #333;
    line-height: 60px;
    border-bottom: 1px solid #b3c0d1;
  }

  .el-aside {
    color: #333;
    background-color: rgb(238, 241, 246)
  }

  .el-submenu .el-menu-item {
      min-width: 180px;
  }
</style>
