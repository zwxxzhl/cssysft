<template>

  <el-checkbox
    v-model="form[config.model]"
    @change="$emit('change')"
    :class="config.class"
    :size="config.size"
    :border="config.border"
    :true-label="config.trueLabel"
    :false-label="config.falseLabel"
  >
    {{ config.show }}
  </el-checkbox>

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