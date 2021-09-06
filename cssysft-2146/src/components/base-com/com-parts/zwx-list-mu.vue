<template>
  <div>
    <zwx-table
      ref="refZwxTable"
      v-bind="$attrs">
    </zwx-table>

    <!-- @current-change 以及 选中页码输入框(el-input) 中数字会触发 @select 事件，与 el-table 中事件冲突 -->
    <zwx-pagination
      v-if="paginationShow"
      ref="refZwxPagination"
      style="padding: 5px 0; text-align: left"
      :current-page="$attrs.currentPage"
      :total="$attrs.total"
      :page-size="$attrs.pageSize"
      @current-change="$emit('page-current-change', $event)"
      @size-change="$emit('page-size-change', $event)"
      @select.stop="$emit('page-select', $event)">
    </zwx-pagination>
  </div>
</template>

<script setup>
import ZwxTable from '../com-el/zwx-table.vue';
import ZwxPagination from '../com-el/zwx-pagination.vue';

import {
  ref, useContext, defineComponent, defineProps
} from "vue";

const props = defineProps({
  paginationShow: {
    type: Boolean,
    required: false,
    default: false
  }
});

defineComponent({
  inheritAttrs: false
});

const {expose} = useContext();

const refZwxTable = ref(null);
const refZwxPagination = ref(null);

expose({
  refZwxTable, refZwxPagination
});

</script>

<style scoped>

</style>
