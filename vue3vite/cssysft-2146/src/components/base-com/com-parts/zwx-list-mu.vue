<template>
  <zwx-table
    ref="refZwxTable"
    v-bind="$attrs"
    :form="form">

    <template v-for="slotName in Object.keys($slots)" #[slotName]="scope">
      <slot :name="slotName" v-bind="scope"></slot>
    </template>

  </zwx-table>

  <zwx-pagination
    v-if="paginationShow"
    ref="refZwxPagination"
    style="padding: 5px 0; text-align: left"
    v-bind="$attrs">
  </zwx-pagination>
</template>

<script setup>
import ZwxTable from '../com-el/zwx-table.vue';
import ZwxPagination from '../com-el/zwx-pagination.vue';

import {
  ref, defineComponent, useAttrs, useSlots
} from "vue";

const props = defineProps({
  form: {
    type: Object,
    required: true
  },
  paginationShow: {
    type: Boolean,
    required: false,
    default: false
  }
});

defineComponent({
  inheritAttrs: false
});

const refZwxTable = ref(null);
const refZwxPagination = ref(null);

const attrs = useAttrs();
const slots = useSlots();
console.log('parent-attrs');
console.log(attrs);
console.log('parent-slots');
console.log(slots);
console.log(Object.keys(slots));

defineExpose({
  refZwxTable, refZwxPagination
});

</script>

<style scoped>

</style>
