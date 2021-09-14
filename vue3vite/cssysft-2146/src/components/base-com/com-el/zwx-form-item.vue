<template>

  <el-form-item
    :label="config.label"
    :prop="config.prop"
    :rules="config.rules"
    :label-width="config.labelWidth || '100px'"
    :size="config.size"
    :show-message="config.showMessage"
    :inline-message="config.inlineMessage"
    :style="config.style">

    <slot></slot>

  </el-form-item>

</template>

<script setup>

const props = defineProps({
  config: {
    type: Object,
    required: true
  }
});

</script>

<style scoped>

</style>
