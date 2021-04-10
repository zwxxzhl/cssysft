<template>
    <div class="containers" ref="content">
    <div class="canvas" ref="canvas"></div>
    <!-- 右边属性部分 -->
    <!-- <div id="js-properties-panel" class="panel"></div> -->
  </div>
</template>

<script>
import "bpmn-js/dist/assets/diagram-js.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn-codes.css";

// import "bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css";
// import "bpmn-js-properties-panel/dist/assets/bpmn-js-properties-panel.css";

// 必须导入
import BpmnModeler from "bpmn-js/lib/Modeler";

// import BpmnViewer from 'bpmn-js'
// 右边工具栏属性模块
import propertiesPanelModule from "bpmn-js-properties-panel";
// 左边工具栏配置模块
import propertiesProviderModule from "bpmn-js-properties-panel/lib/provider/camunda";
import camundaModdleDescriptor from "camunda-bpmn-moddle/resources/camunda";

import { xmlStr } from "./xmlStr.js";

export default {
  name: "BpmnJs",
  data() {
    return {
      // bpmn建模器
      bpmnModeler: null,
      canvas: null,
    };
  },
  mounted() {debugger
    this.bpmnModeler = new BpmnModeler({
      container: this.$refs.canvas,
    });

    this.bpmnModeler.importXML(xmlStr)
      .then((res) => {
        // 调整在正中间
        this.bpmnModeler.get("canvas").zoom("fit-viewport", "auto");
        console.log("rendered");
      })
      .catch((err) => {
        console.log("error rendering", err);
      });
  },
};
</script>

<style scoped>
.containers {
  position: absolute;
  background-color: #ffffff;
  width: 100%;
  height: 100%;
}
.canvas {
  width: 100%;
  height: 100vh;
}
</style>