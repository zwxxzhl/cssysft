const tools = {
  //导出
  export() {
    debugger
    bpmnModeler
      .saveXML({ format: true })
      .then((xml) => {
        debugger
        var eleLink = document.createElement("a");
        eleLink.download = "activiti.bpmn";
        eleLink.style.display = "none";

        const blob = new Blob([xml]);
        eleLink.href = URL.createObjectURL(blob);

        document.body.appendChild(eleLink);
        eleLink.click();

        document.body.removeChild(eleLink);
      })
      .catch((err) => {
        debugger
        console.error("无法保存BPMN", err);
      });
  },
  //前进
  forward() {
    bpmnModeler.get("commandStack").redo();
  },
  //后退
  retreat() {
    bpmnModeler.get("commandStack").undo();
  },
}

export default tools