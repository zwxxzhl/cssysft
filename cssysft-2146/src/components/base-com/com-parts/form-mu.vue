<template>
  <el-form :model="form" :rules="rules" ref="refForm" :size="size" :disabled="disabled">

    <template v-for="(row, idx) in formRow" :key="idx">
      <el-row class="op-flex-center flex-warp" type="flex">

        <template v-for="(item, index) in row" :key="index">

          <el-col :span="item.span" v-if="'select' === item.dom">
            <zwx-form-item :config="item">

              <zwx-select :form="form" :config="item" @change="changeBus($event, item)"></zwx-select>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.span" v-else-if="'cascader' === item.dom">
            <zwx-form-item :config="item">

              <zwx-cascader :form="form" :config="item" @change="changeBus($event, item)"></zwx-cascader>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.span" v-else-if="'date-picker' === item.dom">
            <zwx-form-item :config="item">

              <zwx-date-picker :form="form" :config="item" @change="changeBus($event, item)"></zwx-date-picker>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.span" v-else-if="'input' === item.dom">
            <zwx-form-item :config="item">

              <zwx-input :form="form" :config="item" @change="changeBus($event, item)"></zwx-input>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.span" class="col-class" v-else-if="'checkbox' === item.dom">
            <zwx-form-item :config="item">

              <zwx-checkbox :form="form" :config="item" @change="changeBus($event, item)"></zwx-checkbox>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.span" v-else-if="'radio-group' === item.dom">
            <zwx-form-item :config="item">

              <zwx-radio-group :form="form" :config="item" @change="changeBus($event, item)"></zwx-radio-group>

            </zwx-form-item>
          </el-col>

          <el-col :span="item.span" v-else-if="'slot' === item.dom">

            <slot :name="item.slotName" v-bind="this"></slot>

          </el-col>

        </template>

      </el-row>
    </template>
  </el-form>
</template>

<script setup>
import zwxSelect from '../com-el/zwx-select.vue';
import zwxFormItem from '../com-el/zwx-form-item.vue';
import ZwxCascader from "../com-el/zwx-cascader.vue";
import ZwxDatePicker from "../com-el/zwx-date-picker.vue";
import ZwxInput from "../com-el/zwx-input.vue";
import ZwxCheckbox from "../com-el/zwx-checkbox.vue";
import ZwxRadioGroup from "../com-el/zwx-radio-group.vue";

import {
  ref,
  defineProps,
  getCurrentInstance
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


const { emit } = getCurrentInstance();

const refForm = ref(null);

const changeBus = (val, item) => {
  if (item.change) {
    emit(item.change, val);
  }
}

</script>

<style scoped>

</style>