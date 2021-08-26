<template>
    <div>
      <template v-for="(row, idx) in buttonRow" :key="idx">
        <el-row class="op-flex-center flex-warp" type="flex">

          <template v-for="(item, index) in row" :key="index">

            <el-col :span="item.span">

              <zwxButton :config="item" @click="val => item.click && $emit(item.click, val)"></zwxButton>

            </el-col>

          </template>

        </el-row>
      </template>
    </div>
</template>

<script setup>
import zwxButton from '../com-el/zwx-button.vue';

import {
  defineProps, defineComponent
} from "vue";

const props = defineProps({
  buttonRow: {
    type: Array,
    required: true
  }
});

defineComponent({
  inheritAttrs: false
});

</script>

<style scoped>

</style>
