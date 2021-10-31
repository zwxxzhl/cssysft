<template>

  <el-checkbox-group
      v-model="form[config.model]"
      :class="config.class"
      :style="config.style"
      :size="config.size"
      :disabled="config.disabled">

    <el-checkbox
        v-for="item in config.options"
        :label="item.label"
        :border="item.border">

      {{ item.show }}

    </el-checkbox>
  </el-checkbox-group>

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
