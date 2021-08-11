<template>

  <el-cascader
    v-model="form[config.model]"
    :class="config.class"
    :size="config.size"
    :options="config.options"
    :props="{ checkStrictly: false, value: config.value, label: config.label }"
    :clearable="config.clearable"
    :filterable="config.filterable"
    :placeholder="config.placeholder"
    :disabled="config.disabled">
  </el-cascader>

</template>

<script setup>
import {
  defineProps
} from "vue";

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
