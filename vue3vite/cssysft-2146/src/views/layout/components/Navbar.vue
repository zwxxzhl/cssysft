<template>
  <div class="navbar">
    <div style="display: flex;">
      <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened"/>
      <breadcrumb></breadcrumb>
    </div>

    <el-dropdown class="avatar-container" trigger="click">
      <div class="avatar-wrapper">
        <img :src="avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
        <i class="el-icon-caret-bottom"/>
      </div>
      <template #dropdown>
        <el-dropdown-menu class="user-dropdown">
          <router-link class="inlineBlock" to="/">
            <el-dropdown-item>主页</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click="logout">登出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Hamburger from '@/components/Hamburger/index.vue'
import Breadcrumb from '@/components/Breadcrumb/index.vue'

export default {
  name: 'Navbar',
  components: {
    Hamburger,
    Breadcrumb
  },
  computed: {
    ...mapGetters({
      sidebar: 'app/sidebar',
      avatar: 'user/avatar'
    })
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/ToggleSideBar')
    },
    logout() {
      this.$store.dispatch('user/LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  display: flex;
  justify-content: space-between;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenfull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }
  .avatar-container {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .avatar-wrapper {
      cursor: pointer;
      margin-top: 5px;
      position: relative;
      line-height: initial;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>

