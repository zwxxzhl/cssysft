<template>
  <div class="containers" ref="content" style="height: 100%">
    <div class="canvas" ref="canvas" style="height: 100%"></div>
    <div id="js-properties-panel" class="panel"></div>
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

// 导入模型图
import BpmnModeler from "bpmn-js/lib/Modeler";
import propertiesPanelModule from "bpmn-js-properties-panel";
import propertiesProviderModule from "./resources/properties-panel/provider/activiti";
import customTranslate from "./resources/customTranslate/customTranslate";
import customControlsModule from "./resources/customControls";
import activitiModdleDescriptor from "./resources/activiti.json";

import { xmlStr } from "./xmlStr.js";

let bpmnModeler = null;

export default {
  name: "BpmnJs",
  data() {
    return {
      canvas: null,
    };
  },
  mounted() {
    this.initBpmnJs();
  },
  methods: {
    initBpmnJs() {
      let customTranslateModule = {
        translate: ["value", customTranslate],
      };

      this.canvas = this.$refs.canvas;

      bpmnModeler = new BpmnModeler({
        container: this.canvas,
        propertiesPanel: {
          parent: "#js-properties-panel",
        },
        additionalModules: [
          propertiesProviderModule,
          propertiesPanelModule,
          customControlsModule,
          customTranslateModule,
        ],
        moddleExtensions: {
          camunda: activitiModdleDescriptor,
        },
      });
      this.createNewDiagram();
    },
    createNewDiagram() {
      bpmnModeler
        .importXML(xmlStr)
        .then((res) => {})
        .catch((err) => {});
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
  position: page;
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
