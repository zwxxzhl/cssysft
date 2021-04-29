import activitiApi from "@/api/acl/activiti";

const tools = {
  //查看流程实例详情带颜色
  viewColor(bpmnModeler, row) {
    activitiApi.gethighLine(row.id).then(ret => {
      if (ret.success) {
        const ColorJson = tools.getByColor(ret.data);
        activitiApi.getProcessDefineXml({
          id: row.processDefinitionId
        }).then(res => {
          if (res.success) {
            bpmnModeler
              .importXML(res.data.xml)
              .then((res) => {
                for (const i in ColorJson) {
                  tools.setColor(ColorJson[i], bpmnModeler)
                }
              })
              .catch((err) => {
              });
          }
        })
      }
    })

  },
  //设置颜色
  setColor(json, bpmnModeler) {
    var modeling = bpmnModeler.get('modeling');
    var elementRegistry = bpmnModeler.get('elementRegistry')
    var elementToColor = elementRegistry.get(json.name);
    if (elementToColor) {
      modeling.setColor([elementToColor], {
        stroke: json.stroke,
        fill: json.fill
      });
    }
  },
  //根据数据返回颜色对象
  getByColor(data) {
    var ColorJson = []
    for (var k in data['highLine']) {
      var par = {
        "name": data['highLine'][k],
        "stroke": "green",
        "fill": "green"
      }
      ColorJson.push(par)
    }
    for (var k in data['highPoint']) {
      var par = {
        "name": data['highPoint'][k],
        "stroke": "gray",
        "fill": "#eae9e9"

      }
      ColorJson.push(par)
    }
    for (var k in data['iDo']) {
      var par = {
        "name": data['iDo'][k],
        "stroke": "green",
        "fill": "#a3d68e"
      }
      ColorJson.push(par)
    }
    for (var k in data['waitingToDo']) {
      var par = {
        "name": data['waitingToDo'][k],
        "stroke": "green",
        "fill": "yellow"
      }
      ColorJson.push(par)
    }
    return ColorJson
  },
  //查看流程
  view(bpmnModeler, row) {
    activitiApi.getProcessDefineXml({
      id: row.id
    }).then(res => {
      if (res.success) {
        bpmnModeler
          .importXML(res.data.xml)
          .then((res) => {
          })
          .catch((err) => {
          });
      }
    })
  },
  //部署流程
  deploy(bpmnModeler, vm) {
    bpmnModeler
      .saveXML({format: true})
      .then((res) => {
        let str = tools.formXML(res.xml);
        activitiApi.addDeploymentByString({bpmnStr: str}).then(res => {
          if (res.success) {
            vm.$message({
              type: 'success',
              message: res.message
            })
            vm.bpmnVisible = false;
            vm.fetchData(1);
          }
        })
      })
      .catch((err) => {
        console.error("部署失败", err);
      });
  },
  formXML(data) {
    var temp = data.replace(/camunda/ig, "activiti");
    temp = temp.replace(/FormField/ig, 'formProperty');
    return temp;
  },
  //导出图片
  exportImg(bpmnModeler) {
    bpmnModeler.saveSVG({format: true})
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
      .saveXML({format: true})
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