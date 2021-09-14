<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.name" placeholder="流程名称" />
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
      <el-table-column type="selection" width="55" />

      <el-table-column label="序号" width="70" align="center">
        <template #default="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="processName" label="流程名称" />
      <el-table-column prop="name" label="任务名称" />
      <el-table-column prop="assignee" label="办理人" width="100" />
      <el-table-column prop="startTime" label="开始时间" width="160" />
      <el-table-column prop="endTime" label="结束时间" width="160" />
      <el-table-column prop="version" label="版本号" width="80"/>

      <el-table-column label="操作" width="80" align="center">
        <template #default="scope">
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

    <dialog-bpmn-js ref="refDialogBpmnJs"></dialog-bpmn-js>
  </div>
</template>

<script setup>
import DialogBpmnJs from "../../../components/BpmnJs/dialog_bpmnjs.vue";
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

const onViewBpmn = (row) => {
  refDialogBpmnJs.value.onOpenBpmn(row, enums.bpmnjs.viewer, enums.bpmnjs.detailColor);
}

const fetchData = (pages = 1) => {
  page.value = pages;
  activitiApi
    .getHistoricTaskInstance(page.value, limit.value, searchObj)
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
