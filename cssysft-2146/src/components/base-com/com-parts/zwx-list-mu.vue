<template>
  <div>
    <zwx-table
      ref="refZwxTable"
      v-bind="$attrs">
    </zwx-table>

    <zwx-pagination
      ref="refZwxPagination"
      v-if="paginationShow"
      v-bind="$attrs"
      style="padding: 30px 0; text-align: left"
      @current-change="$emit('page-current-change', $event)"
      @size-change="$emit('page-size-change', $event)">
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
