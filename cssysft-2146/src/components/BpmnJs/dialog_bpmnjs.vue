<template>
  <el-dialog
    v-model="bpmnVisible"
    title="流程图"
    width="80%"
    top="1vh"
    @opened="onOpened"
  >
    <div ref="refDialogDiv" class="dialog-layout">
      <bpmn-js ref="refBpmnJs"></bpmn-js>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="onExportImg">导出图片</el-button>
        <el-button @click="onExportBpmn">导出Bpmn</el-button>
        <el-button @click="onForward">前进</el-button>
        <el-button @click="onRetreat">撤销</el-button>
        <el-button type="primary" @click="onDeploy"> 部署</el-button>
        <el-button @click="onCancel">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import BpmnJs from "./index.vue";

import {ref, useContext, provide, defineEmit} from "vue";
import enums from "../../utils/enums";

const {expose} = useContext();

const bpmnVisible = ref(false);
const refDialogDiv = ref(null);
const refBpmnJs = ref(null);

const modelerOrViewer = ref(enums.bpmnjs.modeler);
const bpmnData = ref(null);

let newOrDetail = enums.bpmnjs.new;

provide('modelerOrViewer', modelerOrViewer);

let emit = defineEmit(['deployed']);

// 导出svg
const onExportImg = () => {
  refBpmnJs.value.exportImg();
}

// 导出bpmn
const onExportBpmn = () => {
  refBpmnJs.value.exportBpmn();
}

// 前进
const onForward = () => {
  refBpmnJs.value.forward();
}

// 后退
const onRetreat = () => {
  refBpmnJs.value.retreat();
}

// 部署流程
const onDeploy = () => {
  refBpmnJs.value.deploy().then(res => {
    if (res && res.success) {
      bpmnVisible.value = false;
      emit('deployed');
    }
  })
}

// 关闭bpmn
const onCancel = () => {
  bpmnVisible.value = false;
}

// 创建bpmn
const onOpenBpmn = (row, val1, val2) => {
  bpmnData.value = row;
  modelerOrViewer.value = val1;
  newOrDetail = val2
  bpmnVisible.value = true;
}

// bpmn模态框打开事件
const onOpened = () => {
  refDialogDiv.value.style.height = parent.innerHeight * 0.78 + "px";
  if (enums.bpmnjs.detail === newOrDetail) {
    refBpmnJs.value.view(bpmnData.value);
  } else if (enums.bpmnjs.detailColor === newOrDetail) {
    refBpmnJs.value.viewColor(bpmnData.value);
  } else {
    refBpmnJs.value.newDiagram();
  }
}

expose({
  onOpenBpmn
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