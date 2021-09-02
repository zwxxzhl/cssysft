<template>
  <el-form ref="refTableForm" :model="form" :rules="rules">
    <el-table
      ref="refTable"
      v-bind="$attrs"
      :data="form.formList">

      <template v-if="selectionShow">
        <el-table-column type="selection" header-align="center" align="center" fixed="left"></el-table-column>
      </template>

      <template v-if="indexShow">
        <el-table-column type="index" label="序号" width="55" header-align="center" align="center">
          <template #default="scope">
            <span v-if="$attrs['page-size']">{{ ($attrs['current-page'] - 1) * $attrs['page-size'] + scope.$index + 1 }}</span>
            <span v-else>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
      </template>

      <template v-for="(col, index) in tableColumn" :key="index">
        <el-table-column
          v-if="col.columnObj.formatter"
          show-overflow-tooltip
          :header-align="col.columnObj.headerAlign || 'center'"
          :align="col.columnObj.align || 'center'"
          :width="col.columnObj.width || ''"
          :min-width="col.columnObj.minWidth || ''"
          :prop="col.columnObj.prop || ''"
          :label="col.columnObj.label || ''"
          :column-key="index.toString()"
          :formatter="formatter">

          <template #header="scope">

            <table-header :config="col.columnObj" :scope="scope" @[col.columnObj.headerEvent]="(val, e) => col.columnObj.headerEvent && $emit(col.columnObj.headerEvent, val, e)">

              <template #[col.columnObj.headerSlotName]>
                <slot :name="col.columnObj.headerSlotName" v-bind="scope"></slot>
              </template>

            </table-header>

          </template>
        </el-table-column>

        <el-table-column
          v-else
          show-overflow-tooltip
          :header-align="col.columnObj.headerAlign || 'center'"
          :align="col.columnObj.align || 'center'"
          :width="col.columnObj.width || ''"
          :min-width="col.columnObj.minWidth || ''"
          :prop="col.columnObj.prop || ''"
          :label="col.columnObj.label || ''"
          :column-key="index.toString()">

          <template #header="scope">

            <table-header :config="col.columnObj" :scope="scope" @[col.columnObj.headerEvent]="(val, e) => col.columnObj.headerEvent && $emit(col.columnObj.headerEvent, val, e)">

              <template #[col.columnObj.headerSlotName]>
                <slot :name="col.columnObj.headerSlotName" v-bind="scope"></slot>
              </template>

            </table-header>

          </template>

          <template #default="scope">

            <template v-if="!col.columnObj.rowslot">

              <zwx-form-item :config="col.formItemObj" v-if="col.domObj && 'checkbox' === col.domObj.dom">
                <zwx-checkbox :form="scope.row" :config="col.domObj" @change="val => col.domObj.change && $emit(col.domObj.change, val)"></zwx-checkbox>
              </zwx-form-item>

              <span v-else>{{ scope.row[col.columnObj.prop] }}</span>

            </template>

            <template v-else>
              <slot :name="col.columnObj.rowSlotName" v-bind="this"></slot>
            </template>

          </template>
        </el-table-column>
      </template>

      <slot name="operation" v-bind="this"></slot>

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
  form: {
    type: Object,
    required: true,
    default: () => ({formList: []})
  },
  rules: {
    type: Object,
    required: false,
    default: () => ({})
  },
  tableColumn: {
    type: Array,
    required: true,
    default: () => ([])
  },
  indexShow: {
    type: Boolean,
    required: false,
    default: true
  },
  selectionShow: {
    type: Boolean,
    required: false,
    default: false
  }
});

defineComponent({
  inheritAttrs: false
});

const { attrs, slots } = useContext();
console.log('attrs');
console.log(attrs);
console.log('slots');
console.log(slots);

const comMethod = inject('comMethod');

const refTable = ref(null);
const refTableForm = ref(null);

onMounted(() => {
  console.log(refTableForm.value);
})

console.log("comMethod");
console.log(comMethod);

const formatter = (row, column, cellValue, index) => {
  let obj = props.tableColumn.find(f => f.formatter && f.prop === column.property);
  if (obj) {
    if (comMethod[obj.formatter]) {
      return comMethod[obj.formatter](row, column, cellValue, index);
    } else {
      console.error("没有格式化函数: " + obj.formatter);
      return row[column.property];
    }
  }
}

watchEffect(() => {

})

computed(() => {

});

</script>

<style scoped>

</style>
