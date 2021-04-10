
<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside width="180px" style="background-color: rgb(238, 241, 246)">
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
    const item = {
      date: "2016-05-02",
      name: "王小虎",
      address: "上海市普陀区金沙江路 1518 弄",
    };
    const menuRoutes = [
      {
        id: "1",
        title: "测试1",
        icon: "el-icon-message",
        path: "",
        children: [
          {
            id: "1-1",
            title: "明细1",
            icon: "",
            path: "/hello",
          },
          {
            id: "1-2",
            title: "明细2",
            icon: "el-icon-message",
            path: "/hel",
            children: [
              {
                id: "1-2-1",
                title: "明细3",
                icon: "",
                path: "/hello",
              },
              {
                id: "1-2-2",
                title: "明细4",
                icon: "el-icon-message",
                path: "/hel",
              },
            ],
          },
        ],
      },
      {
        id: "2",
        title: "测试2",
        icon: "el-icon-message",
        path: "/login",
      },
    ];
    return {
      tableData: Array(20).fill(item)
    };
  },
};
</script>

<style>
.el-header {
  /* background-color: #b3c0d1; */
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #b3c0d1;
}

.el-aside {
  color: #333;
}
</style>
