<template>

  <el-input
    v-model="form[config.model]"
    :class="config.class"
    :size="config.size"
    :type="config.type"
    :rows="config.rows"
    :placeholder="config.placeholder"
    :disabled="config.disabled">
  </el-input>

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

const onChange = (val) => {
  console.log(val)
  debugger
}

</script>

<style scoped>

</style>
