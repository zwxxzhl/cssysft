<template>
  <div class="containers" style="height: 100%">
    <div ref="canvas" class="canvas" style="height: 100%"></div>
    <div ref="panel" v-if="useOpenBpmnAdd" id="js-properties-panel" class="panel"></div>
  </div>
</template>

<script setup>
/* 左边工具栏的样式 去掉该样式左边工具栏不显示 */
import "bpmn-js/dist/assets/diagram-js.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css";
/* 左边工具栏内各个工具的样式，不添加该样式，左边工具栏内不显示任何工具 */
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css";
/*右边工具栏样式*/
import "bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css";

// 导入模型图
import Modeler from "bpmn-js/lib/Modeler";
// 导入只读预览图
import Viewer from 'bpmn-js/lib/Viewer';
import ModelingModule from 'bpmn-js/lib/features/modeling';

// 右边面板
import propertiesPanelModule from "bpmn-js-properties-panel";
import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
// 右边面板扩展，增加功能描述模块
import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";

//汉化
import customTranslate from "./customTranslate/customTranslate";

import {xmlStr} from "./xmlStr.js";

/** ----------------------------------------------------------------------------------- **/

import {useContext, ref, onMounted, inject} from 'vue'

const {expose} = useContext();

const canvas = ref(null);
let bpmnModeler = ref(null);

const useOpenBpmnAdd = inject('openBpmnAdd');

const newDiagram = () => {
  bpmnModeler.importXML(xmlStr)
    .then((res) => {
    })
    .catch((err) => {
    })
}

const initModeler = () => {
  let customTranslateModule = {
    translate: ["value", customTranslate],
  };

  bpmnModeler = new Modeler({
    container: canvas.value,
    // 右边面板挂载
    propertiesPanel: {
      parent: "#js-properties-panel",
    },
    additionalModules: [
      // 右边面板内容
      propertiesProviderModule,
      propertiesPanelModule,
      // 汉化
      customTranslateModule,
    ],
    moddleExtensions: {
      // 右边面板扩展，增加功能描述模块
      camunda: camundaModdleDescriptor,
    }
  });

  newDiagram();
}

const initViewer = () => {
  let customTranslateModule = {
    translate: ["value", customTranslate],
  };

  bpmnModeler = new Viewer({
    container: canvas.value,
    additionalModules: [
      ModelingModule,
      customTranslateModule
    ]
  })
}

onMounted(() => {
  if (useOpenBpmnAdd) {
    initModeler();
  } else {
    initViewer();
  }
})

expose({
  bpmnModeler
})

</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.containers {
  display: flex;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}

.canvas {
  width: 100%;
  height: 100%;

  .properties-panel-parent {
    display: none;
  }
}

.panel {
  right: 0;
  top: 0;
  width: 300px;
}
</style>
