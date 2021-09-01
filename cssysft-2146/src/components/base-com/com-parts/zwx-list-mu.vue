<template>
  <div>
    <zwx-table
      ref="refZwxTable"
      v-bind="$attrs">
    </zwx-table>

    <!-- @current-change 需重命名，与 el-table 中事件冲突；另选 el-input 框中数字会触发 @select 事件，也与 el-table 中事件冲突，无法拦截。混合组件肯定冲突，开发时需要注意 -->
    <el-pagination
      v-if="paginationShow"
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
  ref,
  toRef,
  toRefs,
  reactive,
  useContext,
  defineEmit,
  defineComponent,
  inject,
  getCurrentInstance,
  onMounted,
  watch,
  computed,
  defineProps,
  watchEffect
} from "vue";

const props = defineProps({
  paginationShow: {
    type: Boolean,
    required: false,
    default: false
  },
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

const {attrs, slots} = useContext();

</script>

<style scoped>

</style>
