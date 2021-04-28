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

// 导入模型图
import Modeler from "bpmn-js/lib/Modeler";

// 右边面板
import propertiesPanelModule from "bpmn-js-properties-panel";
// import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/bpmn";
// 右边面板扩展，增加功能描述模块
// import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";

//汉化
import customTranslate from "./customTranslate/customTranslate";

import {xmlStr} from "./xmlStr.js";
import tools from "./tools.js";

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
          // 右边面板内容
          propertiesProviderModule,
          propertiesPanelModule,
          // 汉化
          customTranslateModule,
        ],
        // moddleExtensions: {
        //   // 右边面板扩展，增加功能描述模块
        //   camunda: camundaModdleDescriptor,
        // },
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
