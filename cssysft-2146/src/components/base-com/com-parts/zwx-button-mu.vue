<template>
    <div>
      <template v-for="(row, idx) in buttonRow" :key="idx">
        <el-row type="flex">

          <template v-for="(item, index) in row" :key="index">

            <el-col :span="item.rowObj.span">

              <zwx-button :config="item.domObj" @click="val => item.domObj.click && $emit(item.domObj.click, val)"></zwx-button>

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
  buttonRow: {
    type: Array,
    required: true
  }
});

</script>

<style scoped>

</style>
