import activitiApi from "@/api/acl/processDefinition";

const tools = {
  //部署
  deploy(bpmnModeler, busvm) {
    bpmnModeler
      .saveXML({ format: true })
      .then((res) => {
        activitiApi.addDeploymentByString({bpmnStr: res.xml}).then(res => {debugger
          // debugger
          if (res.success) {
            this.$message({
              type: 'success',
              message: res.message
            })
            busvm.taskVisible = false;
          }
        })
      })
      .catch((err) => {
        console.error("部署失败", err);
      });
  },
  //导出图片
  exportImg(bpmnModeler) {
    bpmnModeler.saveSVG({ format: true })
      .then(res => {
        var encodedData = encodeURIComponent(res.svg);

        let elink = document.createElement("a");
        elink.download = 'diagram.svg';
        elink.style.display = "none";
        elink.href = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData;
        document.body.appendChild(elink);
        elink.click();
        URL.revokeObjectURL(elink.href);
        document.body.removeChild(elink);
      }).catch(err => {
        console.error('保存失败', err);
      })
  },
  //导出xml
  exportBpmn(bpmnModeler) {
    bpmnModeler
      .saveXML({ format: true })
      .then((res) => {
        var encodedData = encodeURIComponent(res.xml);

        let elink = document.createElement("a");
        elink.download = 'diagram.bpmn';
        elink.style.display = "none";
        elink.href = 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData;
        document.body.appendChild(elink);
        elink.click();
        URL.revokeObjectURL(elink.href);
        document.body.removeChild(elink);
      })
      .catch((err) => {
        console.error("无法保存BPMN", err);
      });
  },
  //前进
  forward(bpmnModeler) {
    bpmnModeler.get("commandStack").redo();
  },
  //后退
  retreat(bpmnModeler) {
    bpmnModeler.get("commandStack").undo();
  },
}

export default tools