<template>
  <el-form :model="form" :rules="rules" ref="refForm" :size="size" :disabled="disabled">

    <template v-for="(row, idx) in formRow" :key="idx">
      <el-row class="op-flex-center flex-warp" type="flex">

        <template v-for="(item, index) in row" :key="index">

          <el-col :span="item.span" v-if="'select' === item.dom">

            <el-form-item :config="item">

              <zwx-select :form="form" :config="item" @change="$emit(item.change)"></zwx-select>

            </el-form-item>

          </el-col>
        </template>

        <el-col :span="item.span" v-else-if="'cascader' === item.dom">

          <el-form-item :config="item">

            <zwx-cascader :form="form" :config="item" @change="$emit(item.change)"></zwx-cascader>

          </el-form-item>

        </el-col>

        <el-col :span="item.span" v-else-if="'date-picker' === item.dom">

          <el-form-item :config="item">

            <zwx-date-picker :form="form" :config="item" @change="$emit(item.change)"></zwx-date-picker>

          </el-form-item>

        </el-col>

        <el-col :span="item.span" v-else-if="'input' === item.dom">

          <el-form-item :config="item">

            <zwx-input :form="form" :config="item" @change="$emit(item.change)"></zwx-input>

          </el-form-item>

        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'checkbox' === formitem.domType">

          <el-form-item :config="item">

            <zwx-checkbox :form="form" :config="item" @change="$emit(item.change)"></zwx-checkbox>

          </el-form-item>

        </el-col>

        <el-col :span="item.span" v-else-if="'radio-group' === item.dom">

          <el-form-item :config="item">

            <zwx-radio-group :form="form" :config="item" @change="$emit(item.change)"></zwx-radio-group>

          </el-form-item>

        </el-col>

<!--        todo 具名插槽-->
        <el-col :span="item.span" v-else-if="'slot' === item.dom">
          <slot></slot>
        </el-col>
      </el-row>
    </template>
  </el-form>
</template>

<script setup>
import zwxSelect from '../com-el/zwx-select.vue';
import zwxFormItem from '../com-el/zwx-form-item.vue';

import {
  ref,
  toRef,
  toRefs,
  reactive,
  useContext,
  defineEmit,
  inject,
  getCurrentInstance,
  onMounted,
  watch,
  computed,
  defineProps, watchEffect
} from "vue";
import ZwxCascader from "../com-el/zwx-cascader";
import ZwxDatePicker from "../com-el/zwx-date-picker";
import ZwxInput from "../com-el/zwx-input";
import ZwxCheckbox from "../com-el/zwx-checkbox";
import ZwxRadioGroup from "../com-el/zwx-radio-group";
export default {
  components: {ZwxRadioGroup, ZwxCheckbox, ZwxInput, ZwxDatePicker, ZwxCascader}
}
const props = defineProps({
  size: {
    type: String,
    required: false,
    default: ''
  },
  stripe: {
    type: Boolean,
    required: false,
    default: false
  },
  border: {
    type: Boolean,
    required: false,
    default: false
  },
  style: {
    type: String,
    required: false,
    default: 'width: 100%'
  },
  height: {
    type: Number,
    required: false,
    default: null
  },
  maxHeight: {
    type: Number,
    required: false,
    default: null
  },
  headerRowClassName: {
    type: Function,
    required: false,
    default: () => {
    }
  },
  headerCellClassName: {
    type: Function,
    required: false,
    default: () => {
    }
  },
  rowClassName: {
    type: Function,
    required: false,
    default: () => {
    }
  },
  cellClassName: {
    type: Function,
    required: false,
    default: () => {
    }
  },
  highlightCurrentRow: {
    type: Boolean,
    required: false,
    default: false
  },
  rowKey: {
    type: String,
    required: false
  },
  tableData: {
    type: Array,
    required: false,
    default: () => ([])
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
  },
  rules: {
    type: Object,
    required: false,
    default: () => ({})
  }
});

const comMethod = inject('comMethod');

const refElTable = ref(null);
const refElForm = ref(null);
const form = ref({formList: []});

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
  form.value.formList = props.tableData;
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

.red-xin {
  color: #F56C6C;
  font-size: 12px;
  margin-right: 4px;
}

</style>