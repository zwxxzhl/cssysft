<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.name" placeholder="实例名称"/>
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

      <el-table-column prop="startDate" label="创建时间" width="150"/>
      <el-table-column prop="name" label="实例名称" width="150"/>
      <el-table-column prop="processDefinitionKey" label="流程定义Key" width="200"/>
      <el-table-column prop="status" label="状态"/>
      <el-table-column prop="processDefinitionVersion" label="版本号"/>

      <el-table-column label="操作" width="230" align="center">
        <template #default="scope">
          <el-tooltip v-if="hasPerm('instance.update')" effect="dark" content="挂起" placement="left-start">
            <i class="el-icon-warning-outline icon-layout-mini color-orange" @click="onSuspend(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.update')" effect="dark" content="激活" placement="left-start">
            <i class="el-icon-circle-check icon-layout-mini color-green" @click="onResume(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.list')" effect="dark" content="进展" placement="left-start">
            <i class="el-icon-view icon-layout-mini color-blue" @click="onViewBpmn(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.remove')" effect="dark" content="删除" placement="left-start">
            <i class="el-icon-delete icon-layout-mini color-gray" @click="onDelete(scope.row)"></i>
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
    //挂起流程实例
    onSuspend(row) {
      activitiApi.suspendInstance(
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
    //激活流程实例
    onResume(row) {
      activitiApi.resumeInstance(
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
    //删除流程实例
    onDelete(row) {
      activitiApi.deleteInstance(
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
      this.$refs.refBpmnJs.deploy(this);
    },
    //查看流程
    onViewBpmn(row) {
      this.ifBpmnAdd = false;
      this.bpmnData = row;
      this.bpmnVisible = true;
    },
    fetchData(page = 1) {
      this.page = page;
      activitiApi
        .getInstances(this.page, this.limit, this.searchObj)
        .then((res) => {
          this.list = res.data.items;
          this.total = res.data.total;

          this.listLoading = false;
        });
    },
    changeSize(size) {
      this.limit = size;
      this.fetchData(1);
    },
    resetData() {
      this.searchObj = {};
      this.fetchData();
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection;
    }
  },
};
</script>

<style scoped>
.dialog-layout {
  overflow: auto;
}
</style>