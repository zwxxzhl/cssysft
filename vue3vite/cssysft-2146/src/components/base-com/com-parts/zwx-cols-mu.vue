<template>
  <div>
    <template v-for="(row, idx) in colList" :key="idx">
      <el-row v-bind="$attrs">

        <template v-for="(item, index) in row" :key="index">

          <el-col :span="item.rowObj.span || 2" :style="item.rowObj.colStyle" v-if="'button' === item.domObj.dom">

            <zwx-button :config="item.domObj" @click="val => item.domObj.click && $emit(item.domObj.click, val)"></zwx-button>

          </el-col>

          <el-col :span="item.rowObj.span || 2" :style="item.rowObj.colStyle" v-else-if="'slot' === item.domObj.dom">

            <slot :name="item.domObj.slotName"></slot>

          </el-col>

        </template>

      </el-row>
    </template>
  </div>
</template>

<script setup>
import ZwxButton from '../com-el/zwx-button.vue';

const props = defineProps({
  colList: {
    type: Array,
    required: false,
    default: () => []
  }
});

</script>

<style scoped>

</style>
