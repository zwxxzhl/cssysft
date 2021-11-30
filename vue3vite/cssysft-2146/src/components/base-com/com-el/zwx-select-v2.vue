<template>

  <el-select-v2
    v-model="form[config.model]"
    :class="config.class"
    :style="config.style"
    :size="config.size"
    :value-key="config.valueKey"
    :options="config.options"
    :clearable="config.clearable"
    :filterable="config.filterable"
    :multiple="config.multiple"
    :placeholder="config.placeholder"
    :disabled="config.disabled">
  </el-select-v2>

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
