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
import BpmnJs from "@/components/BpmnJs/index.vue";

import tools from "./tools.js";
import {ref, useContext, provide} from "vue";

const {expose} = useContext();

const refBpmnJs = ref(null);
const refDialogDiv = ref(null);

const bpmnVisible = ref(false);
const openBpmnAdd = ref(true);
const bpmnData = ref(null);

provide('openBpmnAdd', openBpmnAdd.value);

//导出svg
const onExportImg = () => {
  tools.exportImg(refBpmnJs.value.bpmnModeler);
}

//导出bpmn
const onExportBpmn = () => {
  tools.exportBpmn(refBpmnJs.value.bpmnModeler);
}

//前进
const onForward = () => {
  tools.forward(refBpmnJs.value.bpmnModeler);
}

//后退
const onRetreat = () => {
  tools.retreat(refBpmnJs.value.bpmnModeler);
}

//部署流程
const onDeploy = () => {
  tools.deploy(refBpmnJs.value.bpmnModeler);
}

//关闭bpmn
const onCancel = () => {
  bpmnVisible.value = false;
}

//创建bpmn
const onOpenBpmn = (row, val) => {
  openBpmnAdd.value = val;
  bpmnData.value = row;
  bpmnVisible.value = true;
}

//bpmn模态框打开事件
const onOpened = () => {
  refDialogDiv.value.style.height = parent.innerHeight * 0.7 + "px";
  if (!openBpmnAdd.value) {
    tools.view(refBpmnJs.value.bpmnModeler, bpmnData.value);
  }
}

expose({
  onOpenBpmn,
  refBpmnJs
});
</script>

<style scoped>

</style>