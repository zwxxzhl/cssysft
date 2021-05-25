<template>
  <el-dialog
    v-model="visible"
    :title="title"
    :width="width"
    :top="top"
    @opened="opened"
  >
    <div ref="refDialogContent" class="dialog-layout">
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
const dialogdata = ref(null);
const dialogheight = ref(0);

provide('dialogdata', dialogdata);
provide('dialogheight', dialogheight);

let emit = defineEmit(['opened', 'confirm', 'cancel']);

const confirm = () => {
  emit('confirm');
}

const cancel = () => {
  emit('cancel');
}

const close = () => {
  visible.value = false;
}

const open = (row) => {
  dialogdata.value = row;
  visible.value = true;
}

const opened = () => {
  dialogheight.value = parent.innerHeight * props.heightPercent;
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
  overflow: auto;
}
</style>