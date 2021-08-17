<template>
  <div class="app-container">

    <zwx-form-mu
      :form="search"
      :form-row="searchRow">
    </zwx-form-mu>

    <zwx-button-mu
      :button-row="buttonRow"
      @search-click="onSearch">
    </zwx-button-mu>

    <el-button type="primary" icon="el-icon-search" @click="onSearch()">查询</el-button>
    <el-button type="default" @click="resetData()">清空</el-button>

    <zwx-table-mu
      :size="'medium'"
      stripe
      :form="form"
      :table-column="tableColumn"
      :selection-show="true"
      @select="onSelect"
      @header-click-cus="headerClickCus"
      :pagination-show="true"
      :current-page="currentPage"
      :total="total"
      :page-size="pageSize"
      @page-current-change="onSearch"
      @size-change="sizeChange">
    </zwx-table-mu>

    <dialog-com ref="refDialogCom" title="验光录入" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content="sp">
        <span>验光录入</span>
      </template>
    </dialog-com>

  </div>
</template>

<script setup>
import DialogCom from "../../../components/DialogCom/dialog_com.vue";
import ZwxTableMu from "../../../components/base-com/com-parts/zwx-table-mu.vue";
import ZwxFormMu from "../../../components/base-com/com-parts/zwx-form-mu.vue";
import ZwxButtonMu from "../../../components/base-com/com-parts/zwx-button-mu.vue";

import comCfg from "../../../components/base-com/com-config/com-config.js";
import comUtils from "../../../utils/comUtils.js";
import depApi from "../../../api/acl/dep.js";

import {ref, onMounted, reactive, getCurrentInstance, provide, useContext} from "vue";
import activitiApi from "../../../api/acl/activiti";

const gp = getCurrentInstance().appContext.config.globalProperties;

let {dateYMDHmsFormat} = comUtils
provide('comMethod', {
  dateYMDHmsFormat
});

let search = reactive({});
const searchRow = reactive([
  [
    {span: 6, dom: 'input', type: 'text', model: 'depName', placeholder: '部门名称'},
    {span: 6, dom: 'input', type: 'text', model: 'depNo', placeholder: '部门编码'}
  ]
]);

const buttonRow = reactive([
  [
    {span: 6, size: comCfg.buttonSize, type: comCfg.searchBtnType, label: '搜索', icon: comCfg.searchBtnIcon, click: 'search-click'},
    {span: 6, size: comCfg.buttonSize, type: comCfg.addBtnType, label: '新增', icon: comCfg.addBtnIcon, click: 'add-click'},
    {span: 6, size: comCfg.buttonSize, type: comCfg.editBtnType, label: '编辑', icon: comCfg.editBtnIcon, click: 'edit-click'},
    {span: 6, size: comCfg.buttonSize, type: comCfg.deleteBtnType, label: '删除', icon: comCfg.deleteBtnIcon, click: 'delete-click'},
  ]
]);

const form = ref({formList: []});
let tableColumn = reactive([
  {prop: 'depName', label: '部门', hAk: true},
  {prop: 'depNo', label: '编码', width: '90'},
  {prop: 'sequence', label: '顺序'}
]);

let listLoading = ref(true);
let total = ref(0);
let currentPage = ref(1);
let pageSize = ref(10);
const multipleSelection = ref([]);

const refDialogCom = ref(null);

const onSearch = (page = 1) => {
  currentPage.value = page;

  depApi
    .page(page.value, pageSize.value, search)
    .then((res) => {
      form.value.formList = res.data.items;
      total.value = res.data.total;

      listLoading.value = false;
    });
}

const sizeChange = (val) => {
  debugger
  pageSize.value = val;
}

const onSelect = (selection, row) => {
  console.log(selection)
  console.log(row)
  debugger
}

const headerClickCus = (e, val, ...param) => {
  console.log(e)
  console.log(val)
  console.log(param)
  debugger
}


onMounted(() => {
  onSearch();
})

</script>

<style scoped>

</style>
