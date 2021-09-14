<template>

  <el-select
    v-model="form[config.model]"
    :class="config.class"
    :size="config.size"
    :clearable="config.clearable"
    :filterable="config.filterable"
    :multiple="config.multiple"
    :placeholder="config.placeholder"
    :disabled="config.disabled">

    <el-option
      v-for="item in config.options"
      :key="item[config.opKey]"
      :label="(!config.optionShow || config.optionShow === 'label') && item[config.opLabel]
              || config.optionShow === 'label(key)' && (item[config.opLabel] + '(' + item[config.opKey] + ')' )
              || (item[config.opKey] + '(' + item[config.opLabel] + ')' )"
      :value="item[config.opValue]">
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
