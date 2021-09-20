<template>
  <el-dialog
    v-model="visible"
    :title="title"
    :width="width"
    :top="top"
    @opened="opened"
  >
    <div class="dialog-layout" ref="refDialogContent">
      <slot name="content"></slot>
    </div>
    <template #footer v-if="footer">
      <div class="dialog-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="confirm">确认</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref, provide} from "vue";
import enums from "../../utils/enums";

const props = defineProps({
  title: String,
  width: String,
  top: String,
  heightPercent: Number,
  footer: Boolean
})

const visible = ref(false);
const refDialogContent = ref(null);
const dialogData = ref(null);
const dialogHeight = ref(0);
let openType = ref(enums.formType.add);

provide('dialogData', dialogData);
provide('openType', openType);

let emit = defineEmits(['opened', 'confirm', 'cancel']);

const close = () => {
  visible.value = false;
}
provide('dialogClose', close);

const confirm = () => {
  emit('confirm');
}

const cancel = () => {
  emit('cancel');
}

const open = (row, pageVm, type) => {
  dialogHeight.value = parent.innerHeight * props.heightPercent;
  openType.value = type;

  dialogData.value = row;
  visible.value = true;

  if (pageVm) {
    pageVm.initData();
  }
}

const opened = () => {
  refDialogContent.value.style.height = dialogHeight.value + "px";
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
.dialog-layout {
  position: relative;
}
</style>
