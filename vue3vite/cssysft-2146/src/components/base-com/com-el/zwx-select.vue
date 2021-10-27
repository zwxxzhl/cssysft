<template>

  <el-select
    v-model="form[config.model]"
    :class="config.class"
    :style="config.style"
    :size="config.size"
    :clearable="config.clearable"
    :filterable="config.filterable"
    :multiple="config.multiple"
    :placeholder="config.placeholder"
    :disabled="config.disabled">

    <el-option
      v-for="item in config.options"
      :key="item[config.option.key]"
      :label="config.option.labelFunc && item[config.option.labelFunc] || item[config.option.label]"
      :value="item[config.option.value]">
    </el-option>

  </el-select>

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
