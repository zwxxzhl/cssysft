<template>

  <el-select
    v-model="form[config.model]"
    @change="eventBus(config.eveName, $event)"
    :class="config.class"
    :size="config.size"
    :clearable="config.clearable"
    :filterable="config.filterable"
    :multiple="config.multiple"
    :placeholder="config.placeholder"
    :disabled="config.disabled"
  >
    <el-option
      v-for="item in config.options"
      :key="config[item.key]"
      :label="(!config.optionShow || config.optionShow === 'label') && config[item.label] || config.optionShow === 'label(key)'
                                        && (config[item.label] + '(' + config[item.key] + ')' ) || (config[item.key] + '(' + config[item.label] + ')' )"
      :value="config[item.value]">
    </el-option>
  </el-select>

</template>

<script setup>
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

const props = defineProps({
  config: {
    type: Object,
    required: true
  },
  form: {
    type: Object,
    required: true,
  },
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