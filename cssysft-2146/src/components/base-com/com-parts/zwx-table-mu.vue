<template>
  <el-form ref="refTableForm" :model="form" :rules="rules" size="mini">
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
            {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
          </template>
        </el-table-column>
      </template>

      <template v-for="(col, index) in tableColumn" :key="index">
        <el-table-column
          v-if="col.formatter"
          show-overflow-tooltip
          :header-align="col.headerAlign || 'center'"
          :align="col.align || 'center'"
          :width="col.width || ''"
          :min-width="col.minWidth || ''"
          :prop="col.prop || ''"
          :label="col.label || ''"
          :column-key="index.toString()"
          :formatter="formatter">

          <template #header="scope">

            <table-header :config="col" :scope="scope" @[col.headerEvent]="(val, e) => col.headerEvent && $emit(col.headerEvent, val, e)">

              <template #[col.hSlotName]>
                <slot :name="col.hSlotName" v-bind="scope"></slot>
              </template>

            </table-header>

          </template>
        </el-table-column>

        <el-table-column
          v-else
          show-overflow-tooltip
          :header-align="col.headerAlign || 'center'"
          :align="col.align || 'center'"
          :width="col.width || ''"
          :min-width="col.minWidth || ''"
          :prop="col.prop || ''"
          :label="col.label || ''"
          :column-key="index.toString()">

          <template #header="scope">

            <table-header :config="col" :scope="scope" @[col.headerEvent]="(val, e) => col.headerEvent && $emit(col.headerEvent, val, e)">

              <template #[col.headerSlotName]>
                <slot :name="col.headerSlotName" v-bind="scope"></slot>
              </template>

            </table-header>

          </template>

          <template #default="scope">

            <template v-if="!col.rowslot">

              <zwx-form-item :config="col.domObj" v-if="'checkbox' === col.dom">
                <zwx-checkbox :form="scope.row" :config="col.domObj" @change="val => col.domObj.change && $emit(col.domObj.change, val)"></zwx-checkbox>
              </zwx-form-item>

              <span v-else>{{ scope.row[col.prop] }}</span>

            </template>

            <template v-else>
              <slot :name="col.rowSlotName" v-bind="this"></slot>
            </template>

          </template>
        </el-table-column>
      </template>

    </el-table>

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
  </el-form>
</template>

<script setup>
import TableHeader from "./parts/table-header.vue";
import zwxFormItem from '../com-el/zwx-form-item.vue';
import zwxSelect from '../com-el/zwx-select.vue';
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
  },
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

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none
}

input[type="number"] {
  -moz-appearance: textfield;
}

.el-form-item--mini.el-form-item {
  margin-bottom: 0;
}

.el-form-item {
  margin-bottom: 0;
}


</style>
