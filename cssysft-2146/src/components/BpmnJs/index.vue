<template>
  <div class="containers" style="height: 100%">
    <div ref="refCanvas" class="canvas" style="height: 100%"></div>
    <div ref="refPanel" v-if="modelerStr === useModelerOrViewer" id="js-properties-panel" class="panel"></div>
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
// 原生面板
// import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
// import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";
// 自定义面板
import bpmnPropertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/bpmn';
import propertiesProviderModule from './customPropertiesPanel/magic';
import camundaModdleDescriptor from './customPropertiesPanel/magic/descriptors/magic';

//汉化
import customTranslate from "./customTranslate/customTranslate";

import customPaletteProvider from "./customPaletteProvider/index";
import customContextPadProvider from "./customContestPadProvider/index";

/** ----------------------------------------------------------------------------------- **/

import {useContext, ref, onMounted, inject, getCurrentInstance} from 'vue'

import {xmlStr} from "./xmlStr";
import enums from "../../utils/enums";
import tools from "./tools";

const {expose} = useContext();
const currentInstance = getCurrentInstance();

const refCanvas = ref(null);

let bpmnJs = {
  modeler: null
};
const modelerStr = enums.bpmnjs.modeler;

const useModelerOrViewer = inject('modelerOrViewer');

const viewColor = (row) => {
  tools.viewColor(bpmnJs.modeler, row);
}

const view = (row) => {
  tools.view(bpmnJs.modeler, row);
}

const deploy = () => {
  return tools.deploy(bpmnJs.modeler);
}

const exportImg = () => {
  tools.exportImg(bpmnJs.modeler);
}

const exportBpmn = () => {
  tools.exportBpmn(bpmnJs.modeler);
}

const forward = () => {
  tools.forward(bpmnJs.modeler);
}

const retreat = () => {
  tools.retreat(bpmnJs.modeler);
}

const newDiagram = () => {
  bpmnJs.modeler.importXML(xmlStr)
    .then((res) => {
    })
    .catch((err) => {
    })
}

const initModeler = () => {
  let customTranslateModule = {
    translate: ["value", customTranslate],
  };

  bpmnJs.modeler = new Modeler({
    container: refCanvas.value,
    // 右边面板挂载
    propertiesPanel: {
      parent: "#js-properties-panel",
    },
    additionalModules: [
      customPaletteProvider,
      customContextPadProvider,
      // 右边面板内容
      propertiesPanelModule,
      bpmnPropertiesProviderModule,
      propertiesProviderModule,
      // 汉化
      customTranslateModule,
    ],
    moddleExtensions: {
      // 右边面板扩展，增加功能描述模块
      camunda: camundaModdleDescriptor,
    }
  })
}

const initViewer = () => {
  let customTranslateModule = {
    translate: ["value", customTranslate],
  };

  bpmnJs.modeler = new Viewer({
    container: refCanvas.value,
    additionalModules: [
      ModelingModule,
      customTranslateModule
    ]
  })
}

onMounted(() => {
  if (modelerStr === useModelerOrViewer.value) {
    initModeler();
  } else {
    initViewer();
  }
})

expose({
  viewColor,
  view,
  deploy,
  exportImg,
  exportBpmn,
  forward,
  retreat,
  newDiagram
});

</script>

<style>
/* palette 一列展示 */
.djs-palette.two-column.open {
  width: 49px;
}
</style>

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
