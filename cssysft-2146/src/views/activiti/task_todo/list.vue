<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.name" placeholder="任务名称"/>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"/>

      <el-table-column label="序号" width="70" align="center">
        <template #default="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="processName" label="流程名称"/>
      <el-table-column prop="name" label="任务名称"/>
      <el-table-column prop="assignee" label="办理人" width="100"/>
      <el-table-column prop="createTime" label="创建时间" width="160"/>
      <el-table-column prop="version" label="版本号" width="80"/>

      <el-table-column label="操作" width="100" align="center">
        <template #default="scope">
          <el-tooltip v-if="hasPerm('task_todo.handle')" effect="dark" content="派发任务" placement="bottom-start">
            <i class="el-icon-caret-right icon-layout-mini color-purple" @click="onStartSubInstance(scope.row)"></i>
          </el-tooltip>
          <el-tooltip v-if="hasPerm('task_todo.handle')" effect="dark" content="办理" placement="left-start">
            <i class="el-icon-plus icon-layout-mini color-green" @click="onComplete(scope.row)"></i>
          </el-tooltip>
          <el-tooltip v-if="hasPerm('task_history.list')" effect="dark" content="查看" placement="left-start">
            <i class="el-icon-view icon-layout-mini color-blue" @click="onViewBpmn(scope.row)"></i>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10, 20, 30, 40, 50, 100]"
      style="padding: 30px 0; text-align: center"
      layout="sizes, prev, pager, next, jumper, ->, total, slot"
      @current-change="fetchData"
      @size-change="changeSize"
    />

    <el-dialog
      v-model="bpmnVisible"
      title="流程图"
      width="80%"
      top="1vh"
      @opened="onOpened"
    >
      <div ref="refDialogDiv" class="dialog-layout">
        <bpmn-js ref="refBpmnJs"></bpmn-js>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="onExportImg">导出图片</el-button>
          <el-button @click="onExportBpmn">导出Bpmn</el-button>
          <el-button @click="onForward">前进</el-button>
          <el-button @click="onRetreat">撤销</el-button>
          <el-button type="primary" @click="onDeploy"> 部署</el-button>
          <el-button @click="onCancel">关闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

import userApi from "@/api/acl/user";
import activitiApi from "@/api/acl/activiti";
import BpmnJs from "@/components/BpmnJs/index.vue";

export default {
  components: {
    BpmnJs,
  },
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: null, // 讲师列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象
      multipleSelection: [], // 批量选择中选择的记录列表

      bpmnVisible: false,
      ifBpmnAdd: true,
      bpmnData: null,
    };
  },
  computed: {
    ...mapGetters({
      bpmnModeler: "app/bpmnModeler",
    }),
  },
  created() {
    this.fetchData();
  },
  mounted() {
  },
  methods: {
    //启动子流程实例
    onStartSubInstance(row) {
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
    },
    //完成任务
    onComplete(row) {
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
    },
    //导出svg
    onExportImg() {
      this.$refs.refBpmnJs.exportImg();
    },
    //导出bpmn
    onExportBpmn() {
      this.$refs.refBpmnJs.exportBpmn();
    },
    //前进
    onForward() {
      this.$refs.refBpmnJs.forward();
    },
    //后退
    onRetreat() {
      this.$refs.refBpmnJs.retreat();
    },
    //bpmn模态框打开事件
    onOpened() {
      this.$refs.refDialogDiv.style.height = parent.innerHeight * 0.7 + "px";
      if (this.ifBpmnAdd) {
        this.$refs.refBpmnJs.newDiagram();
      } else {
        this.$refs.refBpmnJs.viewColor(this.bpmnData);
      }
    },
    //关闭bpmn
    onCancel() {
      this.bpmnVisible = false;
    },
    //创建bpmn
    onOpenBpmn() {
      this.ifBpmnAdd = true;
      this.bpmnVisible = true;
    },
    //部署流程
    onDeploy() {
      this.$refs.refBpmnJs.deploy();
    },
    //查看流程
    onViewBpmn(row) {
      this.ifBpmnAdd = false;
      this.bpmnData = row;
      this.bpmnVisible = true;
    },
    onDeleteBpmn(row) {
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
    },
    changeSize(size) {
      this.limit = size;
      this.fetchData(1);
    },
    addUser() {
      this.$router.push({path: "/acl/user/add"});
    },
    // 加载讲师列表数据
    fetchData(page = 1) {
      // 异步获取远程数据（ajax）
      this.page = page;

      activitiApi
        .getTasks(this.page, this.limit, this.searchObj)
        .then((res) => {
          this.list = res.data.items;
          this.total = res.data.total;

          // 数据加载并绑定成功
          this.listLoading = false;
        });
    },

    // 重置查询表单
    resetData() {
      this.searchObj = {};
      this.fetchData();
    },

    // 根据id删除数据
    removeDataById(id) {
      // debugger
      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
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
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    },
    removeRows() {
      console.log("removeRows......");

      if (this.multipleSelection.length === 0) {
        this.$message({
          type: "warning",
          message: "请选择要删除的记录!",
        });
        return;
      }

      this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 遍历selection，将id取出放入id列表
          var idList = [];
          this.multipleSelection.forEach((item) => {
            idList.push(item.id);
            // console.log(idList)
          });
          // 调用api
          return userApi.removeRows(idList);
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
    },

    // 执行搜索
    // queryString：文本框中输入的值
    // cb：一个函数
    querySearch(queryString, cb) {
      console.log(queryString);
      console.log(cb);

      // teacher.selectNameByKey(queryString).then(response => {
      //   // 调用 callback 返回建议列表的数据
      //   cb(response.data.items)
      // })
    },
  },
};
</script>

<style scoped>
.dialog-layout {
  overflow: auto;
}
</style>
