<template>
  <div class="containers" ref="content" style="height: 100%">
    <div class="canvas" ref="canvas" style="height: 100%"></div>
    <div id="js-properties-panel" class="panel" ref="panel"></div>
  </div>
</template>

<script>
/* 左边工具栏的样式 去掉该样式左边工具栏不显示 */
import "bpmn-js/dist/assets/diagram-js.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css";
/* 左边工具栏内各个工具的样式，不添加该样式，左边工具栏内不显示任何工具 */
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css";
/*右边工具栏样式*/
import "bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css";

import Modeler from "bpmn-js/lib/Modeler";
import propertiesPanelModule from "bpmn-js-properties-panel";

//activiti
import propertiesProviderModule from './properties-panel/provider/activiti';
import activitiModdleDescriptor from './activiti.json';
import customTranslate from './customTranslate/customTranslate';
import customControlsModule from './customControls';

import {xmlStr} from "./newDiagram.js";
import tools from "../tools.js";

let bpmnModeler = null;

export default {
  name: "BpmnJs",
  data() {
    return {
      canvas: null,
      createDiagram: true,
    };
  },
  mounted() {
    this.initBpmnJs();
  },
  methods: {
    viewColor(row) {
      tools.viewColor(bpmnModeler, row);
    },
    view(row) {
      tools.view(bpmnModeler, row);
    },
    deploy(vm) {
      tools.deploy(bpmnModeler, vm);
    },
    exportImg() {
      tools.exportImg(bpmnModeler);
    },
    exportBpmn() {
      tools.exportBpmn(bpmnModeler);
    },
    forward() {
      tools.forward(bpmnModeler);
    },
    retreat() {
      tools.retreat(bpmnModeler);
    },
    initBpmnJs() {
      let customTranslateModule = {
        translate: ["value", customTranslate],
      };

      this.canvas = this.$refs.canvas;

      bpmnModeler = new Modeler({
        container: this.canvas,
        // 右边面板挂载
        propertiesPanel: {
          parent: "#js-properties-panel",
        },
        additionalModules: [
          propertiesPanelModule,
          propertiesProviderModule,
          customControlsModule,
          customTranslateModule
        ],
        moddleExtensions: {
          activiti:activitiModdleDescriptor
        }
      });
    },
    newDiagram() {
      bpmnModeler.importXML(xmlStr)
        .then((res) => {
        })
        .catch((err) => {
        });
    },
  },
};
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
