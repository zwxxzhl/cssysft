<template>
  <div class="containers" ref="content">
    <div id="canvas" class="canvas"></div>
  </div>
</template>

<script>
import "bpmn-js/dist/assets/diagram-js.css";
import "bpmn-js/dist/assets/bpmn-font/css/bpmn.css";
import Modeler from "bpmn-js/lib/Modeler";

import { xmlStr } from "./xmlStr.js";

export default {
  name: "BpmnJs",
  data() {
    return {};
  },
  async mounted() {
    // create a modeler
    const modeler = new Modeler({ container: "#canvas" });

    // import diagram
    try {
      await modeler.importXML(xmlStr);
      // ...
    } catch (err) {
      // err...
    }
  },
};
</script>

<style scoped>

.canvas {
  width: 100%;
  height: 100vh;
}
</style>