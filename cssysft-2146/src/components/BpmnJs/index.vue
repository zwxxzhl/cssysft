<template>
  <div class="containers" ref="content">
    <div class="canvas" ref="canvas"></div>
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

// 右边工具栏属性模块
import propertiesPanelModule from "bpmn-js-properties-panel";
// 左边工具栏配置模块
import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
// 扩展activiti描述功能模块
import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";

//汉化
import customTranslate from "./customTranslate/customTranslate";

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

      bpmnModeler = new BpmnModeler({
        container: this.$refs.canvas,
        // 添加控制板(右边属性配置栏部分)
        propertiesPanel: {
          parent: "#js-properties-panel",
        },
        additionalModules: [
          //左边工具栏及节点
          propertiesProviderModule,
          //右边工具栏
          propertiesPanelModule,
          //汉化
          customTranslateModule,
        ],
        moddleExtensions: {
          //模块扩展，扩展activiti描述
          camunda: camundaModdleDescriptor,
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

<style scoped>
.containers {
  /* position: absolute; */
  display: flex;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}
.canvas {
  width: 100%;
  height: 100%;
}
.panel {
  /* position: absolute; */
  right: 0;
  top: 0;
  width: 300px;
}
</style>
