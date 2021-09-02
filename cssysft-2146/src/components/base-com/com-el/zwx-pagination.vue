<template>

    <!-- @current-change 与 el-table 中事件冲突；另选择页码输入框(el-input) 中数字会触发 @select 事件，也与 el-table 中事件冲突；
    所以不要与 el-table 一起封装，除非冲突的两个事件，在 el-talbe 与 el-pagination 中全部重命名 -->

    <el-pagination
      :current-page="currentPage"
      :total="total"
      :page-size="pageSize"
      :page-sizes="[1, 5, 10, 20, 30, 40, 50, 100]"
      layout="sizes, prev, pager, next, jumper, ->, total, slot">
    </el-pagination>

</template>

<script setup>
import {
  defineProps
} from "vue";

const props = defineProps({
  currentPage: {
    type: Number,
    required: false,
    default: 1
  },
  total: {
    type: Number,
    required: false,
    default: 0
  },
  pageSize: {
    type: Number,
    required: false,
    default: 10
  }
});
</script>

<style scoped>

</style>
