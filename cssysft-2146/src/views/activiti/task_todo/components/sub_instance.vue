<template>
  <el-dialog
    v-model="bpmnVisible"
    title="流程图"
    width="90%"
    top="1vh"
    @opened="onOpened"
  >
    <div ref="refSubListDiv" class="dialog-layout">
      <sub-list ref="refSubList"></sub-list>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onCancel">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import SubList from "./sub_list.vue";

import {ref, useContext, provide} from "vue";

const {expose} = useContext();

const bpmnVisible = ref(false);
const refSubListDiv = ref(null);
const refSubList = ref(null);

const row = ref(null);

provide('row', row);

const onCancel = () => {
  bpmnVisible.value = false;
}

const onOpen = (row) => {
  row.value = row;
  bpmnVisible.value = true;
}

const onOpened = () => {
  refSubListDiv.value.style.height = parent.innerHeight * 0.78 + "px";
}

expose({
  onOpen
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