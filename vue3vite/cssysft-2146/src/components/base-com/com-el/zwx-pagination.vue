<template>
  <div>
    <!-- @current-change 以及 选中页码输入框(el-input) 中数字会触发 @select 事件，与 el-table 中事件冲突 -->
    <el-pagination
      :total="total"
      :current-page="currentPage"
      :page-size="pageSize"
      :page-sizes="[1, 5, 10, 20, 30, 40, 50, 100]"
      @current-change="$emit('page-current-change', $event)"
      @size-change="$emit('page-size-change', $event)"
      @select.stop="$emit('page-select', $event)"
      layout="sizes, prev, pager, next, jumper, ->, total, slot">
    </el-pagination>
  </div>
</template>

<script setup>

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

</script>

<style scoped>

</style>
