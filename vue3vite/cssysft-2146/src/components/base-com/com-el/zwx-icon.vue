<template>
  <el-icon :size="size" :color="color">
    <component :is="name"></component>
  </el-icon>
</template>

<script setup>
import { defineComponent } from "vue";
import * as Icons from "@element-plus/icons-vue";



const props = defineProps({
  name: {
    type: String,
    required: true,
  },
  size: {
    type: String,
    default: "",
  },
  color: {
    type: String,
    default: "",
  },
});
</script>
