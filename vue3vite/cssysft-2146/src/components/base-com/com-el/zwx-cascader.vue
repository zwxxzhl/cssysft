<template>

  <el-cascader
    v-model="form[config.model]"
    :class="config.class"
    :style="config.style"
    :size="config.size"
    :options="config.options"
    :props="config.props"
    :clearable="config.clearable"
    :filterable="config.filterable"
    :placeholder="config.placeholder"
    :disabled="config.disabled">
  </el-cascader>

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
