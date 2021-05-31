<template>
  <el-dialog
    v-model="visible"
    :title="title"
    :width="width"
    :top="top"
    @opened="opened"
  >
    <div class="dialog-layout" ref="refDialogContent">
      <slot name="content" v-bind="this"></slot>
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
import {ref, useContext, provide, defineEmit, defineProps} from "vue";

const props = defineProps({
  title: String,
  width: String,
  top: String,
  heightPercent: Number,
  footer: Boolean
})

const {expose} = useContext();

const visible = ref(false);
const refDialogContent = ref(null);
const dialogData = ref(null);
const dialogheight = ref(0);

provide('dialogData', dialogData);

let emit = defineEmit(['opened', 'confirm', 'cancel']);

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
  dialogheight.value = parent.innerHeight * props.heightPercent;

  dialogData.value = row;
  visible.value = true;

  if (pageVm) {
    pageVm.initData(type);
  }
}

const opened = () => {
  refDialogContent.value.style.height = dialogheight.value + "px";
  emit('opened');
}

expose({
  open,
  close
});
</script>

<style>
.el-dialog__body {
  padding: 5px 20px;
  color: #606266;
  font-size: 14px;
  word-break: break-all;
}
</style>

<style scoped>
.dialog-layout {
  position: relative;
}
</style>