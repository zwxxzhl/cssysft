<template>
  <el-form class="form-css" :model="form" :rules="rules" ref="refElForm" size="mini">
    <el-table
      ref="refElTable"
      :size="size"
      :stripe="stripe"
      :border="border"
      :style="style"
      :height="height"
      :max-height="maxHeight"
      :header-row-class-name="headerRowClassName"
      :header-cell-class-name="headerCellClassName"
      :row-class-name="rowClassName"
      :cell-class-name="cellClassName"
      :highlight-current-row="highlightCurrentRow"
      :data="form.formList"
      :row-key="rowKey"
      @select="$emit('select')"
      @select-all="$emit('select-all')"
      @selection-change="$emit('selection-change')"
      @row-click="$emit('row-click')"
      @row-dblclick="$emit('row-dblclick')"
    >
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
          :formatter="formatter"
        >
          <template #header="scope">

            <template v-if="!col.headerSlot">
              <span v-if="col.validate" class="red-xin" @click="$emit('header-click', $event, 'redXin')">*</span>

              <span v-if="col.iconLeft" :class="col.iconLeftClass" @click="$emit('header-click', $event, 'iconLeft')"
                    style="padding-top: 5px;"></span>

              <span @click="$emit('header-click', $event, scope.column.label)">{{ scope.column.label }}</span>

              <span v-if="col.iconRight" :class="col.iconRightClass" @click="$emit('header-click', $event, 'iconRight')"
                    style="padding-top: 5px;"></span>
            </template>

            <template v-else>
              <slot :name="col.prop.toLowerCase()+'header'" v-bind="this"></slot>
            </template>

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
          :column-key="index.toString()"
        >
          <template #header="scope">

            <template v-if="!col.headerSlot">
              <span v-if="col.validate" class="red-xin" @click="$emit('header-click', $event, 'redXin')">*</span>

              <span v-if="col.iconLeft" :class="col.iconLeftClass" @click="$emit('header-click', $event, 'iconLeft')"
                    style="padding-top: 5px;"></span>

              <span @click="$emit('header-click', $event, scope.column.label)">{{ scope.column.label }}</span>

              <span v-if="col.iconRight" :class="col.iconRightClass" @click="$emit('header-click', $event, 'iconRight')"
                    style="padding-top: 5px;"></span>
            </template>

            <template v-else>
              <slot :name="col.prop.toLowerCase()+'header'" v-bind="this"></slot>
            </template>

          </template>

          <template #default="scope">

            <template v-if="!col.rowslot">
              <div v-if="col.dom === 'checkbox'">
                <el-checkbox v-model="scope.row[col.prop]" :true-label="1" :false-label="0" disabled></el-checkbox>
              </div>
              <span v-else>{{ scope.row[col.prop] }}</span>
            </template>

            <template v-else>
              <slot :name="col.prop.toLowerCase()+'row'" v-bind="this"></slot>
            </template>

          </template>
        </el-table-column>
      </template>

    </el-table>

    <el-pagination
      v-if="paginationShow"
      :current-page="currentPage"
      :total="total"
      :page-size="pageSize"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: right"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="$emit('current-change')"
      @size-change="$emit('size-change')">
    </el-pagination>
  </el-form>
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