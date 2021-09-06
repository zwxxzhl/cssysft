<template>

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
