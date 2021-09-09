<template>
  <el-form ref="refForm" :model="form" :rules="rules" :size="size" :disabled="disabled">

    <template v-for="(row, idx) in formRow" :key="idx">
      <el-row>

        <template v-for="(item, index) in row" :key="index">

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-if="'select' === item.domObj.dom">
            <zwx-form-item :config="item.formItemObj">

              <zwx-select :form="form" :config="item.domObj" @change="val => item.domObj.change && $emit(item.domObj.change, val)"></zwx-select>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-else-if="'cascader' === item.domObj.dom">
            <zwx-form-item :config="item.formItemObj">

              <zwx-cascader :form="form" :config="item.domObj" @change="val => item.domObj.change && $emit(item.domObj.change, val)"></zwx-cascader>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-else-if="'date-picker' === item.domObj.dom">
            <zwx-form-item :config="item.formItemObj">

              <zwx-date-picker :form="form" :config="item.domObj" @change="val => item.domObj.change && $emit(item.domObj.change, val)"></zwx-date-picker>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-else-if="'input' === item.domObj.dom">
            <zwx-form-item :config="item.formItemObj">

              <zwx-input :form="form" :config="item.domObj" @change="val => item.domObj.change && $emit(item.domObj.change, val)"></zwx-input>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-else-if="'checkbox' === item.domObj.dom">
            <zwx-form-item :config="item.formItemObj">

              <zwx-checkbox :form="form" :config="item.domObj" @change="val => item.domObj.change && $emit(item.domObj.change, val)"></zwx-checkbox>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-else-if="'radio-group' === item.domObj.dom">
            <zwx-form-item :config="item.formItemObj">

              <zwx-radio-group :form="form" :config="item.domObj" @change="val => item.domObj.change && $emit(item.domObj.change, val)"></zwx-radio-group>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-else-if="'button' === item.domObj.dom">

            <zwx-button :config="item.domObj" @click="val => item.domObj.click && $emit(item.domObj.click, val)"></zwx-button>

          </el-col>

          <el-col :span="item.rowObj.span || 0" :style="item.rowObj.colStyle" v-else-if="'slot' === item.domObj.dom">

            <slot :name="item.domObj.slotName" v-bind="this"></slot>

          </el-col>

        </template>

      </el-row>
    </template>
  </el-form>
</template>

<script setup>
import ZwxSelect from '../com-el/zwx-select.vue';
import ZwxFormItem from '../com-el/zwx-form-item.vue';
import ZwxCascader from "../com-el/zwx-cascader.vue";
import ZwxDatePicker from "../com-el/zwx-date-picker.vue";
import ZwxInput from "../com-el/zwx-input.vue";
import ZwxCheckbox from "../com-el/zwx-checkbox.vue";
import ZwxRadioGroup from "../com-el/zwx-radio-group.vue";
import ZwxButton from '../com-el/zwx-button.vue';

import {
  defineProps, ref, useContext
} from "vue";

const props = defineProps({
  form: {
    type: Object,
    required: true
  },
  formRow: {
    type: Array,
    required: true
  },
  rules: {
    type: Object,
    required: false,
    default: () => ({})
  },
  size: {
    type: String,
    required: false,
    default: ''
  },
  disabled: {
    type: Boolean,
    required: false,
    default: false
  }
});

const {expose} = useContext();

const refForm = ref(null);

expose({
  refForm
});

</script>

<style scoped>

</style>
