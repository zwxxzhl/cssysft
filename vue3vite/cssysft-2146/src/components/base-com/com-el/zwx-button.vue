<template>

  <el-button
    :class="config.class"
    :size="config.size"
    :type="config.type"
    :plain="config.plain"
    :round="config.round"
    :circle="config.circle"
    :loading="config.loading"
    :disabled="config.disabled"
    :icon="config.icon">

    {{ config.label }}

  </el-button>

</template>

<script setup>

const props = defineProps({
  config: {
    type: Object,
    required: true
  }
});

</script>

<style scoped>

</style>
