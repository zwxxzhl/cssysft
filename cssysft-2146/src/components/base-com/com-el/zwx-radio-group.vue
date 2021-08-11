<template>

  <el-radio-group
    v-model="form[config.model]"
    :class="config.class"
    :size="config.size"
    :disabled="config.disabled">

    <el-radio
      v-for="item in config.options"
      :key="item[config.key]"
      :label="item[config.value]"
      :border="config.border">

      {{ item[config.label] }}

    </el-radio>
  </el-radio-group>

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
