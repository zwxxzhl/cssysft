<template>

  <el-input
    v-model="form[config.model]"
    @change="$emit('change')"
    :class="config.class"
    :size="config.size"
    :type="config.type"
    :rows="config.rows"
    :placeholder="config.placeholder"
    :disabled="config.disabled"
  >
  </el-input>

</template>

<script setup>
import {
  defineEmit,
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

let emit = defineEmit(['change']);

</script>

<style scoped>

</style>