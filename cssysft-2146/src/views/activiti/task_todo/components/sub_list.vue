<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.name" placeholder="流程名称"/>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

    <div>
      <el-button v-if="hasPerm('definition.add')" type="danger" size="mini" @click="onOpenBpmn">创建转派流程</el-button>
      <el-button v-if="hasPerm('definition.add')" type="danger" size="mini" @click="onComplete">直接完成任务</el-button>
    </div>

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

      <el-table-column prop="key" label="流程key"/>
      <el-table-column prop="name" label="流程名称"/>
      <el-table-column prop="resourceName" label="资源名称" width="200"/>
      <el-table-column prop="version" label="版本号"/>

      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-tooltip v-if="hasPerm('definition.add')" effect="dark" content="以此流程转派任务" placement="bottom-start">
            <i class="el-icon-caret-right icon-layout-mini color-purple" @click="onStartSubInstance(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('definition.list')" effect="dark" content="查看流程" placement="bottom-start">
            <i class="el-icon-view icon-layout-mini color-blue" @click="onViewBpmn(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('definition.remove')" effect="dark" content="删除流程" placement="bottom-start">
            <i class="el-icon-delete icon-layout-mini color-gray" @click="onDeleteBpmn(scope.row)"></i>
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

    <dialog-bpmn-js ref="refDialogBpmnJs" @deployed="onDeployed"></dialog-bpmn-js>

    <dialog-com ref="refDialogCom" title="任务内容" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content="sp">
        <inst-form ref="refInstForm"></inst-form>
      </template>
    </dialog-com>
  </div>
</template>

<script setup>
import DialogBpmnJs from "../../../../components/BpmnJs/dialog_bpmnjs.vue";
import DialogCom from "../../../../components/DialogCom/dialog_com.vue";
import InstForm from "../../definition/component/inst_form.vue";

import enums from "../../../../utils/enums";
import activitiApi from "../../../../api/acl/activiti";

import {ref, onMounted, reactive, getCurrentInstance, inject, defineEmit, useContext} from "vue";

const {expose} = useContext();
const globalProperties = getCurrentInstance().appContext.config.globalProperties;

let emit = defineEmit(['close']);

const dialogData = inject('dialogData');
const openType = inject('openType');
const dialogClose = inject('dialogClose');

const listLoading = ref(true);
const list = ref([]);
const total = ref(0);
const page = ref(1);
const limit = ref(10);
let searchObj = reactive({});
const multipleSelection = ref([]);

const refDialogBpmnJs = ref(null);
const refDialogCom = ref(null);
const refInstForm = ref(null);

const onStartSubInstance = (row) => {
  let data = JSON.parse(JSON.stringify(row))
  data.SUBINSTANCE = true;
  data.processInstanceId = dialogData.value.processInstanceId;
  refDialogCom.value.open(data, refInstForm.value, enums.formType.add);
}

const onComplete = () => {
  activitiApi.completeTask(
    dialogData.value.id
  ).then(res => {
    if (res.success) {
      globalProperties.$message.success(res.message);
      dialogClose();
      emit('close');
    }
  })
}

const onOpenBpmn = () => {
  refDialogBpmnJs.value.onOpenBpmn({}, enums.bpmnjs.modeler, enums.bpmnjs.new);
}

const onViewBpmn = (row) => {
  refDialogBpmnJs.value.onOpenBpmn(row, enums.bpmnjs.modeler, enums.bpmnjs.detail);
}

const onDeleteBpmn = (row) => {
  globalProperties.$confirm("此操作将删除流程, 是否继续?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  }).then((res) => {
    activitiApi.deleteProcessDefinition({
      deploymentId: row.deploymentId,
      cascade: 1
    }).then(res => {
      if (res.success) {
        globalProperties.$message.success(res.message);
        fetchData();
      }
    })
  }).catch((err) => {
    globalProperties.$message.info("已取消删除");
  });
}

const onDeployed = () => {
  fetchData();
}

const fetchData = (pages = 1) => {
  page.value = pages;
  activitiApi
    .getProcessDefinition(page.value, limit.value, searchObj)
    .then((res) => {
      list.value = res.data.items;
      total.value = res.data.total;

      listLoading.value = false;
    });
}

const changeSize = (size) => {
  limit.value = size;
  fetchData(1);
}

const resetData = () => {
  searchObj = {};
  fetchData();
}

const handleSelectionChange = (selection) => {
  multipleSelection.value = selection;
}

const initData = () => {
  if (enums.formType.detail === openType.value) {
    fetchData();
  }
}

onMounted(() => {
  initData();
})

expose({
  initData
});

</script>

<style scoped>

</style>
