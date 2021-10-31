<template>

  <el-radio
      v-model="form[config.model]"
      :class="config.class"
      :style="config.style"
      :size="config.size"
      :border="config.border"
      :label="config.label"
      :disabled=config.disabled>
    {{ config.show }}
  </el-radio>

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
