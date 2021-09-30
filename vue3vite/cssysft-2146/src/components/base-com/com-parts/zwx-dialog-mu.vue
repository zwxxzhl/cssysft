<template>
  <el-dialog
    v-model="visible"
    v-bind="$attrs"
    @opened="opened">
    <template #title>
      <slot name="title"></slot>
    </template>

    <div ref="refDialogContentDiv" class="dialog-content-layout">
      <component :is="currentComponent" ref="refDialogContent" v-bind="$attrs"></component>
    </div>

    <template #footer>
      <slot name="footer"></slot>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref} from "vue";
import enums from "../../../utils/enums";

const props = defineProps({
  currentComponent: {
    type: Object,
    required: false,
    default: () => ({})
  },
  heightPct: {
    type: Number,
    required: false,
    default: 1
  }
})

let emit = defineEmits(['opened']);

const visible = ref(false);
const refDialogContentDiv = ref(null);
const refDialogContent = ref(null);

let dialogHeight = ref(0);
let openType = ref(enums.formType.add);
let dialogData = ref(null);

const close = () => {
  visible.value = false;
}

const open = (row, type) => {
  dialogHeight.value = parent.innerHeight * props.heightPct;
  visible.value = true;

  openType.value = type;
  dialogData.value = row;
}

const opened = () => {
  refDialogContentDiv.value.style.height = dialogHeight.value + "px";
  refDialogContent.value.initData(dialogData, openType, close);
  emit('opened');
}

defineExpose({
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

.dialog-content-layout {
  position: relative;
}
</style>
