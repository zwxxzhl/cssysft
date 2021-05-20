import userApi from "@/api/acl/user";
import activitiApi from "@/api/acl/activiti";

export default function () {
  //启动子流程实例
  const onStartSubInstance = (row) => {
    activitiApi.startSubInstance({
      key: row.processDefinitionKey,
      procinstId: row.processInstanceId,
      name: row.name,
      variable: '自定义变量'
    }).then(res => {
      if (res.success) {
        this.$message({
          type: 'success',
          message: res.message
        })
      }
    })
  };
  //完成任务
  const onComplete = (row) => {
    activitiApi.completeTask(
      row.id
    ).then(res => {
      if (res.success) {
        this.fetchData();
        this.$message({
          type: 'success',
          message: res.message
        })
      }
    })
  };
  //导出svg
  const onExportImg = () => {
    this.$refs.refBpmnJs.exportImg();
  };
  //导出bpmn
  const onExportBpmn = () => {
    this.$refs.refBpmnJs.exportBpmn();
  };
  //前进
  const onForward = () => {
    this.$refs.refBpmnJs.forward();
  };
  //后退
  const onRetreat = () => {
    this.$refs.refBpmnJs.retreat();
  };
  //bpmn模态框打开事件
  const onOpened = () => {
    this.$refs.refDialogDiv.style.height = parent.innerHeight * 0.7 + "px";
    if (this.ifBpmnAdd) {
      this.$refs.refBpmnJs.newDiagram();
    } else {
      this.$refs.refBpmnJs.viewColor(this.bpmnData);
    }
  };
  //关闭bpmn
  const onCancel = () => {
    this.bpmnVisible = false;
  };
  //创建bpmn
  const onOpenBpmn = () => {
    this.ifBpmnAdd = true;
    this.bpmnVisible = true;
  };
  //部署流程
  const onDeploy = () => {
    this.$refs.refBpmnJs.deploy();
  };
  //查看流程
  const onViewBpmn = (row) => {
    this.ifBpmnAdd = false;
    this.bpmnData = row;
    this.bpmnVisible = true;
  };
  const onDeleteBpmn = (row) => {
    this.$confirm("此操作将删除流程, 是否继续?", "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    })
      .then(() => {
        // promise
        // 点击确定，远程调用ajax
        return userApi.removeById(id);
      })
      .then((response) => {
        this.fetchData(this.page);
        if (response.success) {
          this.$message({
            type: "success",
            message: "删除成功!",
          });
        }
      })
      .catch(() => {
        this.$message({
          type: "info",
          message: "已取消删除",
        });
      });
  };
  const changeSize = (size) => {
    this.limit = size;
    this.fetchData(1);
  };
  const fetchData = (page = 1) => {
    this.page = page;
    activitiApi
      .getTasks(this.page, this.limit, this.searchObj)
      .then((res) => {
        this.list = res.data.items;
        this.total = res.data.total;

        // 数据加载并绑定成功
        this.listLoading = false;
      });
  };
  const resetData = () => {
    this.searchObj = {};
    this.fetchData();
  };
  const handleSelectionChange = (selection) => {
    this.multipleSelection = selection;
  };

  return {
    onStartSubInstance,
    onComplete,
    onExportImg,
    onExportBpmn,
    onForward,
    onRetreat,
    onOpened,
    onCancel,
    onOpenBpmn,
    onDeploy,
    onViewBpmn,
    onDeleteBpmn,
    changeSize,
    fetchData,
    resetData,
    handleSelectionChange,
  }

}