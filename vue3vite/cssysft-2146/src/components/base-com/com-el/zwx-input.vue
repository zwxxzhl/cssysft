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
