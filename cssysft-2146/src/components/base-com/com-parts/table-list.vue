<template id="table-list">
  <div id="table-list-div">

    <el-form class="form-css" :model="form" :rules="rules" ref="refElForm" size="mini">
      <el-table
        ref="refElTable"
        :size="props.size"
        :stripe="props.stripe"
        :border="props.border"
        :style="props.style"
        :height="props.height"
        :max-height="props.maxHeight"
        :header-row-class-name="props.headerRowClassName"
        :header-cell-class-name="props.headerCellClassName"
        :row-class-name="props.rowClassName"
        :cell-class-name="props.cellClassName"
        :highlight-current-row="props.highlightCurrentRow"
        :data="form.formList"
        :row-key="props.rowKey"
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
          <el-table-column type="index" label="序号" width="55" header-align="center" align="center"></el-table-column>
        </template>

        <el-table-column
          v-for="(col, index) in props.tableHead"
          v-if="col.formatter"
          :key="index"
          show-overflow-tooltip
          :header-align="col.headerAlign || 'center'"
          :align="col.align || 'center'"
          :width="col.width || ''"
          :min-width="col.minWidth || ''"
          :prop="col.prop"
          :label="col.label"
          :column-key="index.toString()"
          :formatter="formatter"
        >
          <template #header="scope">

            <template v-if="!col.headerSlot">
              <span v-if="col.validate" class="red-xin" @click="$emit('header-click', $event, 'redXin')">*</span>

              <span v-if="col.iconLeft" :class="col.iconLeftClass" @click="$emit('header-click', $event, 'iconLeft')" style="padding-top: 5px;"></span>

              <span @click="$emit('header-click', $event, scope.column.label)">{{ scope.column.label }}</span>

              <span v-if="col.iconRight" :class="col.iconRightClass" @click="$emit('header-click', $event, 'iconRight')" style="padding-top: 5px;"></span>
            </template>

            <template v-else>
              <slot :name="col.prop.toLowerCase()+'header'" v-bind="this"></slot>
            </template>

          </template>
        </el-table-column>

        <el-table-column
          v-else
          :key="index"
          show-overflow-tooltip
          :header-align="col.headerAlign || 'center'"
          :align="col.align || 'center'"
          :width="col.width || ''"
          :min-width="col.minWidth || ''"
          :prop="col.prop"
          :label="col.label"
          :column-key="index.toString()"
        >
          <template #header="scope">

            <template v-if="!col.headerSlot">
              <span v-if="col.validate" class="red-xin" @click="$emit('header-click', $event, 'redXin')">*</span>

              <span v-if="col.iconLeft" :class="col.iconLeftClass" @click="$emit('header-click', $event, 'iconLeft')" style="padding-top: 5px;"></span>

              <span @click="$emit('header-click', $event, scope.column.label)">{{ scope.column.label }}</span>

              <span v-if="col.iconRight" :class="col.iconRightClass" @click="$emit('header-click', $event, 'iconRight')" style="padding-top: 5px;"></span>
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

      </el-table>
    </el-form>

  </div>
</template>

<script setup>
import {
  ref, useContext, defineEmit, inject, getCurrentInstance, onMounted,
  defineProps, computed
} from "vue";

import userPropsTableList from './composables/UserPropsTableList';

const { props } = userPropsTableList();
const comMethod = inject('comMethod');

const formatter = (row, column, cellValue, index) => {
  let obj = props.tableHead.find(f => f.formatter && f.prop === column.property);
  if (obj) {
    if (comMethod[obj.formatter]) {
      return comMethod[obj.formatter](row, column, cellValue, index);
    } else {
      console.error("没有格式化函数: " + obj.formatter);
      return row[column.property];
    }
  }
}

computed(() => {

});

expose({

});

</script>


<script>
Vue.component('table-list', {

  computed: {
    formatterFun: function () {
      if (this.propFormatter && !ComUtil.isEmptyFunction(this.propFormatter)) {
        return this.propFormatter;
      } else {
        return this.formatter;
      }
    }
  },
  data() {
    return {
      form: {
        formList: []
      },
      rules: null,

      mixProp: {},
      dictObj: {}
    }
  },
  created() {
    this.rules = this.getRules();
    this.$emit('created', this);
  },
  mounted() {
    this.$emit('mounted');
  },
  watch: {
    tableList: {
      handler: function (newVal, oldVal) {
        this.form.formList = newVal;
      },
      deep: true
    }
  },
  methods: {
    getRules() {
      let rules = {};
      for (const it of this.tableHead) {
        if (it.validate) {
          let obj = {};
          this.$set(obj, 'required', true);
          this.$set(obj, 'message', it.validMsg);
          this.$set(obj, 'trigger', it.validTrigger);
          rules[it.prop] = [obj];
        }
      }
      return rules;
    },
    formatter(row, column, cellValue, index) {
      let obj = this.tableHead.find(f => f.formatter && f.prop === column.property);
      if (obj) {
        if (this[obj.formatter]) {
          return this[obj.formatter](row, column, cellValue, index);
        } else {
          console.error("没有格式化函数: " + obj.formatter);
          return row[column.property];
        }
      }
    }
  }
});

</script>

<style scoped>

#table-list-div input::-webkit-outer-spin-button,
#table-list-div input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}

#table-list-div input[type="number"] {
  -moz-appearance: textfield;
}

#table-list-div .el-form-item--mini.el-form-item {
  margin-bottom: 0;
}

#table-list-div .el-form-item {
  margin-bottom: 0;
}

#table-list-div .red-xin {
  color: #F56C6C;
  font-size: 12px;
  margin-right: 4px;
}
</style>