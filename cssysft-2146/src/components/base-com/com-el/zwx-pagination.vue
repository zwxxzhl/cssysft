<template>

  <el-pagination
    :key="currentPage"
    :total="total"
    :current-page="currentPage"
    :page-size="pageSize"
    :page-sizes="[1, 5, 10, 20, 30, 40, 50, 100]"
    layout="sizes, prev, pager, next, jumper, ->, total, slot">
  </el-pagination>

</template>

<script setup>
import {
  defineProps, ref, watch
} from "vue";

const props = defineProps({
  total: {
    type: Number,
    required: false,
    default: 0
  },
  currentPage: {
    type: Number,
    required: false,
    default: 1
  },
  pageSize: {
    type: Number,
    required: false,
    default: 10
  }
});

let key = ref(0);

watch([() => props.currentPage, () => props.pageSize],
  ([pageVal, pagePreVal], [sizeVal, sizePreVal]) => {
    key.value++;
    console.log("触发了watch");
  })

</script>

<style scoped>

</style>
