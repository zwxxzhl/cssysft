<template>

  <el-form-item
    :label="config.label"
    :prop="config.model"
    :rules="config.rules"
    :label-width="config.labelWidth || '100px'"
    :size="config.size"
    :show-message="config.showMessage"
    :inline-message="config.inlineMessage">

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
