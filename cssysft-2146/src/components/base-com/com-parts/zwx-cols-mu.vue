<template>
    <div>
      <template v-for="(row, idx) in colList" :key="idx">
        <el-row type="flex">

          <template v-for="(item, index) in row" :key="index">

            <el-col :span="item.rowObj.span" v-if="'button' === item.domObj.dom">

              <zwx-button :config="item.domObj" @click="val => item.domObj.click && $emit(item.domObj.click, val)"></zwx-button>

            </el-col>

            <el-col :span="item.rowObj.span" v-else-if="'slot' === item.domObj.dom">

              <slot :name="item.domObj.slotName" v-bind="this"></slot>

            </el-col>

          </template>

        </el-row>
      </template>
    </div>
</template>

<script setup>
import ZwxButton from '../com-el/zwx-button.vue';

import {
  defineProps, defineComponent
} from "vue";

defineComponent({
  inheritAttrs: false
});

const props = defineProps({
  colList: {
    type: Array,
    required: true
  }
});

</script>

<style scoped>

</style>
