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

      <el-table-column prop="startDate" label="创建时间" width="160"/>
      <el-table-column prop="name" label="实例名称"/>
      <el-table-column prop="processDefinitionKey" label="流程Key"/>
      <el-table-column prop="status" label="状态"/>
      <el-table-column prop="processDefinitionVersion" label="版本号"/>

      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-tooltip v-if="hasPerm('instance.update')" effect="dark" content="编辑任务表单" placement="left-start">
            <i class="el-icon-edit icon-layout-mini color-purple" @click="onEditForm(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.update')" effect="dark" content="任务汇报情况" placement="left-start">
            <i class="el-icon-view icon-layout-mini color-dark-green" @click="onOpenFormList(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.update')" effect="dark" content="挂起" placement="left-start">
            <i class="el-icon-warning-outline icon-layout-mini color-orange" @click="onSuspend(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.update')" effect="dark" content="激活" placement="bottom-start">
            <i class="el-icon-circle-check icon-layout-mini color-green" @click="onResume(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.list')" effect="dark" content="进展" placement="bottom-start">
            <i class="el-icon-view icon-layout-mini color-blue" @click="onViewBpmn(scope.row)"></i>
          </el-tooltip>

          <el-tooltip v-if="hasPerm('instance.remove')" effect="dark" content="删除" placement="bottom-start">
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

    <dialog-bpmn-js ref="refDialogBpmnJs"></dialog-bpmn-js>

    <dialog-com ref="refDialogCom" title="任务内容" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content="sp">
        <inst-form ref="refInstForm"></inst-form>
      </template>
    </dialog-com>

    <dialog-com ref="refDialogComFormList" title="任务汇报情况" width="90%" top="1vh" :heightPercent="0.88" :footer="false">
      <template #content="sp">
        <form-list ref="refFormList"></form-list>
      </template>
    </dialog-com>
  </div>
</template>

<script setup>
import DialogBpmnJs from "../../../components/BpmnJs/dialog_bpmnjs.vue";
import DialogCom from "../../../components/DialogCom/dialog_com.vue";
import InstForm from "../definition/component/inst_form.vue";
import FormList from "./components/form_list.vue";

import enums from "../../../utils/enums";

import activitiApi from "../../../api/acl/activiti";
import {ref, onMounted, reactive, getCurrentInstance} from "vue";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

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

const refDialogComFormList = ref(null);
const refFormList = ref(null);

const onEditForm = (row) => {
  refDialogCom.value.open(row, refInstForm.value, enums.formType.edit);
}

const onOpenFormList = (row) => {
  refDialogComFormList.value.open(row, refFormList.value, enums.formType.detail);
}

const onSuspend = (row) => {
  activitiApi.suspendInstance(
    row.id
  ).then(res => {
    if (res.success) {
      fetchData();
      globalProperties.$message.success(res.message);
    }
  })
}

const onResume = (row) => {
  activitiApi.resumeInstance(
    row.id
  ).then(res => {
    if (res.success) {
      fetchData();
      globalProperties.$message.success(res.message);
    }
  })
}

const onViewBpmn = (row) => {
  refDialogBpmnJs.value.onOpenBpmn(row, enums.bpmnjs.viewer, enums.bpmnjs.detailColor);
}

const onDelete = (row) => {
  activitiApi.deleteInstance(
    row.id
  ).then(res => {
    if (res.success) {
      fetchData();
      globalProperties.$message.success(res.message);
    }
  })
}

const fetchData = (pages = 1) => {
  page.value = pages;
  activitiApi
    .getInstances(page.value, limit.value, searchObj)
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

onMounted(() => {
  fetchData();
})

</script>

<style scoped>

</style>
