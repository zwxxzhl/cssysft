<template>
  <el-form :model="form" :rules="rules" ref="refForm" :size="size" :disabled="disabled">

    <template v-for="(row, idx) in formRow" :key="idx">
      <el-row class="op-flex-center flex-warp" type="flex">

        <template v-for="(item, index) in row" :key="index">
          <el-col class="col-class" :span="item.span" v-if="'select' === item.dom">

            <el-form-item
              :label="item.label"
              :prop="item.validate && item.model || ''"
              :label-width="item.labelWidth && item.labelWidth || '120px'">

              <el-select
                class="form-width-100"
                v-model="form[item.model]"
                @change="eventBusFun(item.eveName,item.eveParams,$event)"
                :clearable="item.clearable"
                :filterable="item.filterable"
                :multiple="item.multiple"
                :placeholder="item.placeholder"
                :disabled="item.disabled"
                autocomplete="off">
                <el-option
                  v-for="item in mixProp[item.options]"
                  :key="item[item.key]"
                  :label="(!item.optionShow || item.optionShow === 'label') && item[item.label] || item.optionShow === 'label(key)'
                                        && (item[item.label] + '(' + item[item.key] + ')' ) || (item[item.key] + '(' + item[item.label] + ')' )"
                  :value="item[item.value]">
                </el-option>
              </el-select>

            </el-form-item>

          </el-col>
        </template>

        <el-col :span="formitem.span" class="col-class" v-else-if="'select-dictObj' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <el-select class="form-width-100" v-model="form[formitem.model]"
                       @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                       :clearable="formitem.clearable" :filterable="formitem.filterable" :multiple="formitem.multiple"
                       :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
              <el-option
                v-for="item in dictObj[formitem.options]"
                :key="item[formitem.key]"
                :label="(!formitem.optionShow || formitem.optionShow === 'label') && item[formitem.label] || formitem.optionShow === 'label(key)'
                                        && (item[formitem.label] + '(' + item[formitem.key] + ')' ) || (item[formitem.key] + '(' + item[formitem.label] + ')' )"
                :value="item[formitem.value]">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'cascader-mixProp' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <el-cascader class="form-width-100" v-model="form[formitem.model]"
                         :options="mixProp[formitem.options]"
                         :props="{ checkStrictly: false, value: formitem.value, label: formitem.label }"
                         @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                         :clearable="formitem.clearable" :filterable="formitem.filterable" :placeholder="formitem.placeholder"
                         :disabled="formitem.disabled" autocomplete="off">
            </el-cascader>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'select-0-1' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <el-select class="form-width-100" v-model="form[formitem.model]"
                       @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                       :placeholder="formitem.placeholder" :clearable="formitem.clearable"
                       :disabled="formitem.disabled" autocomplete="off">
              <el-option
                v-for="item in formitem.options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'date-picker' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <el-date-picker class="form-width-100" v-model="form[formitem.model]"
                            :type="formitem.type" :value-format="formitem.valueFormat"
                            @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                            :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
            </el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'input' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <el-input class="form-width-100" v-model="form[formitem.model]"
                      @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                      :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
            </el-input>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'textarea' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <el-input class="form-width-100" v-model="form[formitem.model]"
                      :type="formitem.type" :rows="formitem.rows"
                      @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                      :placeholder="formitem.placeholder" :disabled="formitem.disabled" autocomplete="off">
            </el-input>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'checkbox' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <el-checkbox v-if="formitem.domKey === 'isDel'" class="form-width-100" v-model="form[formitem.model]" :true-label="0" :false-label="1">{{
                formitem.placeholder
              }}
            </el-checkbox>
            <el-checkbox v-else class="form-width-100" v-model="form[formitem.model]" :true-label="1" :false-label="0">{{ formitem.placeholder }}</el-checkbox>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'radio-mixProp' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <div class="radio-layout">
              <el-radio-group class="form-width-100" v-model="form[formitem.model]"
                              @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                              :disabled="formitem.disabled" autocomplete="off">
                <el-radio v-for="item in mixProp[formitem.options]" :key="item[formitem.key]" :label="item[formitem.value]">{{ item[formitem.label] }}</el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else-if="'radio-dictObj' === formitem.domType">
          <el-form-item :label="formitem.formLabel" :prop="formitem.validate && formitem.model || ''"
                        :label-width="formitem.formLabelWidth && formitem.formLabelWidth || '120px'">
            <div class="radio-layout">
              <el-radio-group class="form-width-100" v-model="form[formitem.model]"
                              @change="eventBusFun(formitem.eveName,formitem.eveParams,$event)"
                              :disabled="formitem.disabled" autocomplete="off">
                <el-radio v-for="item in dictObj[formitem.options]" :key="item[formitem.key]" :label="item[formitem.value]">{{ item[formitem.label] }}</el-radio>
              </el-radio-group>
            </div>
          </el-form-item>
        </el-col>

        <el-col :span="formitem.span" class="col-class" v-else="'span' === formitem.domType">
          <el-form-item>
            <div :class="formitem.class">
              <i v-if="mixProp[formitem.model]" class="el-icon-info"></i>
              <span>{{ mixProp[formitem.model] }}</span>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </template>
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