<template>
  <div>
    <!-- @current-change 需重命名，与 el-table 中事件冲突；另选择页码输入框(el-input) 中数字会触发 @select 事件，也与 el-table 中事件冲突，无法拦截，开发时需要注意 -->
    <el-pagination
      :current-page="currentPage"
      :total="total"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: right"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="$emit('page-current-change', $event)"
      @size-change="$emit('size-change', $event)">
    </el-pagination>
  </div>
</template>

<script setup>
import ZwxTable from '../com-el/zwx-table.vue';

import {
  defineComponent, defineProps
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

defineComponent({
  inheritAttrs: false
});
</script>

<style scoped>

</style>
