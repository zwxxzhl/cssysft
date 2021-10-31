<template>

  <el-checkbox
    v-model="form[config.model]"
    :class="config.class"
    :style="config.style"
    :size="config.size"
    :border="config.border"
    :label="config.label"
    :true-label="config.trueLabel"
    :false-label="config.falseLabel"
    :disabled=config.disabled>
    {{ config.show }}
  </el-checkbox>

</template>

<script setup>

const props = defineProps({
  config: {
    type: Object,
    required: true
  },
  form: {
    type: Object,
    required: true,
  }
});

</script>

<style scoped>

</style>
