
<template>
  <el-container style="height: 500px; border: 1px solid #eee">
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
      <menu-tree :menuRoutes="menuRoutes"></menu-tree>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px">
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>主页</el-dropdown-item>
              <el-dropdown-item>登出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <span>王小虎</span>
      </el-header>

      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import MenuTree from './components/MenuTree.vue';
export default {
  components: { MenuTree },
  name: "Layout",
  data() {
    const item = {
      date: "2016-05-02",
      name: "王小虎",
      address: "上海市普陀区金沙江路 1518 弄",
    }
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
            path: '/hello'
          },
          {
            id: "1-2",
            title: "明细2",
            icon: "el-icon-message",
            path: '/hel',
            children: [
              {
                id: "1-2-1",
                title: "明细3",
                icon: "",
                path: '/hello'
              },
              {
                id: "1-2-2",
                title: "明细4",
                icon: "el-icon-message",
                path: '/hel'
              },
            ]
          }
        ],
      },
      {
        id: "2",
        title: "测试2",
        icon: "el-icon-message",
        path: "/login"
      }
    ]
    return {
      tableData: Array(20).fill(item),
      menuRoutes
    };
  },
};
</script>

<style>
.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>
