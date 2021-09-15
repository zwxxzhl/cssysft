<template>
  <el-form ref="refTableForm" :model="form" :rules="rules">
    <el-table
      ref="refTable"
      v-bind="$attrs"
      :data="form.formList">

      <template v-for="(col, index) in tableColumn" :key="index">
        <el-table-column
          :type="col.columnObj.type"
          :index="col.columnObj.index || indexMethod"
          :show-overflow-tooltip="col.columnObj.showOverflowTooltip"
          :header-align="col.columnObj.headerAlign || 'center'"
          :align="col.columnObj.align || 'center'"
          :width="col.columnObj.width"
          :min-width="col.columnObj.minWidth"
          :column-key="index.toString()"
          :prop="col.columnObj.prop"
          :label="col.columnObj.label"
          :formatter="col.columnObj.formatter"
          :fixed="col.columnObj.fixed">

          <template #[slotHeaderFun(col)]="scope">
            <table-header :config="col.columnObj" :scope="scope"
                          @[col.columnObj.headerEvent]="(val, e) => col.columnObj.headerEvent && $emit(col.columnObj.headerEvent, val, e)">

              <template #[col.columnObj.headerSlotName]>
                <slot :name="col.columnObj.headerSlotName" v-bind="scope"></slot>
              </template>

            </table-header>
          </template>

          <template #[slotDefaultFun(col)]="scope">
            <template v-if="col.columnObj.rowSlot">
              <slot :name="col.columnObj.rowSlotName" v-bind="scope"></slot>
            </template>

            <template v-else>
              <template v-if="col.domObj && 'checkbox' === col.domObj.dom">
                <zwx-form-item :config="col.formItemObj">
                  <zwx-checkbox
                    :form="scope.row" :config="col.domObj"
                    @change="val => col.domObj.change && $emit(col.domObj.change, val, scope.row)">
                  </zwx-checkbox>
                </zwx-form-item>
              </template>

              <template v-else-if="col.domObj && 'input' === col.domObj.dom">
                <zwx-form-item :config="col.formItemObj">
                  <zwx-input
                    :form="scope.row" :config="col.domObj"
                    @change="val => col.domObj.change && $emit(col.domObj.change, val, scope.row)"
                    @select.stop="val => col.domObj.select && $emit(col.domObj.select, val, scope.row)">
                  </zwx-input>
                </zwx-form-item>
              </template>

              <span v-else>{{ scope.row[col.columnObj.prop] }}</span>
            </template>
          </template>

        </el-table-column>
      </template>

    </el-table>
  </el-form>
</template>

<script setup>
import TableHeader from "./parts/table-header.vue";
import ZwxFormItem from '../com-el/zwx-form-item.vue';
import ZwxSelect from '../com-el/zwx-select.vue';
import ZwxCascader from "../com-el/zwx-cascader.vue";
import ZwxDatePicker from "../com-el/zwx-date-picker.vue";
import ZwxInput from "../com-el/zwx-input.vue";
import ZwxCheckbox from "../com-el/zwx-checkbox.vue";
import ZwxRadioGroup from "../com-el/zwx-radio-group.vue";

import {
  ref,
  toRef,
  toRefs,
  reactive,
  defineComponent,
  inject,
  getCurrentInstance,
  onMounted,
  watch,
  computed,
  watchEffect,
  useAttrs,
  useSlots
} from "vue";

defineComponent({
  inheritAttrs: false
});

const props = defineProps({
  form: {
    type: Object,
    required: false,
    default: () => ({})
  },
  rules: {
    type: Object,
    required: false,
    default: () => ({})
  },
  tableColumn: {
    type: Array,
    required: false,
    default: () => ([])
  }
});

const slotHeaderFun = (col) => {
  if ('selection' === col.columnObj.type) {
    return null;
  } else {
    return 'header'
  }
}

const slotDefaultFun = (col) => {
  if ('selection' === col.columnObj.type) {
    return null;
  } else if ('index' === col.columnObj.type) {
    return null;
  } else if (col.columnObj.formatter) {
    return null;
  } else {
    return 'default'
  }
}


let columnRowSlot = computed(() => {

});


const attrs = useAttrs();
const slots = useSlots();
console.log('zwx-table attrs');
console.log(attrs);
console.log('zwx-table slots');
console.log(slots);

const indexMethod = (index) => {
  if (attrs['page-size']) {
    return (attrs['current-page'] - 1) * attrs['page-size'] + index + 1;
  } else {
    return index;
  }
}


const refTable = ref(null);
const refTableForm = ref(null);

onMounted(() => {
  console.log('zwx-table refTableForm');
  console.log(refTableForm.value);
})

watchEffect(() => {
  console.log('zwx-table ==> watchEffect');
})

defineExpose({
  refTable, refTableForm
});

</script>

<style scoped>
.el-table :deep(.el-table__cell div .el-checkbox) {
  height: unset !important;
}
</style>
