<template>

  <el-form-item
    :label="config.label"
    :prop="config.prop"
    :label-width="config.labelWidth || '120px'"
    :size="config.size"
  >

    <slot></slot>

  </el-form-item>

</template>

<script setup>
import {
  defineProps
} from "vue";

const props = defineProps({
  config: {
    type: Object,
    required: true
  }
});

</script>

<style scoped>

</style>