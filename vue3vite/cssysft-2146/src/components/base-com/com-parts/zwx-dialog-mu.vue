<template>
  <el-dialog
    ref="refElDialog"
    v-model="visible"
    v-bind="$attrs"
    @opened="opened">
    <template #title>
      <slot name="title"></slot>
    </template>

    <div ref="refContent">
      <slot></slot>
    </div>

    <template #footer>
      <slot name="footer"></slot>
    </template>
  </el-dialog>
</template>

<script setup>
import {defineComponent, ref} from "vue";
import enums from "../../../utils/enums";

defineComponent({
  inheritAttrs: false
});

const props = defineProps({
  pageVm: {
    type: Object,
    required: true
  },
  heightPct: {
    type: Number,
    required: false,
    default: 1
  }
})

let emit = defineEmits(['opened']);

const visible = ref(false);
const refElDialog = ref(null);
const refContent = ref(null);

let height = ref(0);
let openType = ref(enums.formType.add);
let data = ref(null);

const close = () => {
  visible.value = false;
}

const open = (row, type) => {
  height.value = parent.innerHeight * props.heightPct;
  visible.value = true;

  openType.value = type;
  data.value = row;
}

const opened = () => {
  refContent.value.style.height = height.value + "px";
  props.pageVm.initData(data, openType, close);
  emit('opened');
}

defineExpose({
  refElDialog,
  open,
  close
});
</script>

<style scoped>
.el-dialog :deep(.el-dialog__body) {
  padding: 5px 20px;
  color: #606266;
  font-size: 14px;
  word-break: break-all;
}
</style>
