<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.name" placeholder="名称"/>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>


    <zwx-form
      :form="form"
      :form-row="formRow"
      @title-change="onTitleChange"
      @content-change="onContentChange"
      @select-change="onSelectChange">
    </zwx-form>

    <zwx-table-list
      :size="'medium'"
      stripe
      :form="form"
      :table-column="tableColumn"
      :selection-show="true"
      @select="onSelect"
      @header-click-cus="headerClickCus"
      @is-del-change="onIsDelChange"
      :pagination-show="true"
      :current-page="currentPage"
      :total="total"
      :page-size="pageSize"
      @page-current-change="fetchData"
      @size-change="sizeChange">
    </zwx-table-list>

    <dialog-com ref="refDialogCom" title="验光录入" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content="sp">
        <span>验光录入</span>
      </template>
    </dialog-com>
  </div>
</template>

<script setup>
import DialogCom from "../../components/DialogCom/dialog_com.vue";
import ZwxTableList from "../../components/base-com/com-parts/zwx-table-list.vue";
import ZwxForm from "../../components/base-com/com-parts/zwx-form.vue";

import comUtils from "../../utils/comUtils";

import {ref, onMounted, reactive, getCurrentInstance, provide, useContext} from "vue";

const gp = getCurrentInstance().appContext.config.globalProperties;

let {dateYMDHmsFormat} = comUtils
provide('comMethod', {
  dateYMDHmsFormat
});

// form组件
const form = ref({formList: []});
const formRow = reactive([
  [
    {span: 8, dom: 'input', type: 'text', label: '标题：', model: 'title', change: 'title-change'},
    {
      span: 8, dom: 'select', label: '选择：', model: 'title',
      options: [{depId: '1', depName: '区庄'}, {depId: '2', depName: '黄埔'}],
      opKey: 'depId', opLabel: 'depName', opValue: 'depId'
    },
  ],
  [
    {span: 8, dom: 'input', type: 'textarea ', label: '内容：', model: 'content', change: 'content-change'}
  ]
]);

const onTitleChange = (val) => {
  console.log(val)
  debugger
}

const onContentChange = (val) => {
  console.log(val)
  debugger
}

const onSelectChange = (val) => {
  console.log(val)
  debugger
}



// table组件
let listLoading = ref(true);
let total = ref(0);
let currentPage = ref(1);
let pageSize = ref(10);
let tableColumn = reactive([
  {prop: 'depName', label: '部门', hAk: true},
  {prop: 'name', label: '姓名', width: '90'},
  {prop: 'no', label: '单号'},
  {prop: 'date', label: '开单日期', minWidth: '140', formatter: 'dateYMDHmsFormat'},

  {prop: 'isDel', label: '有效', dom: 'checkbox', domObj: {labelWidth: '0px', model: 'isDel', trueLabel: 1, falseLabel: 0, change: 'is-del-change'}}
]);

let searchObj = ref({});
const multipleSelection = ref([]);

const refDialogCom = ref(null);

const fetchData = (page = 1) => {
  currentPage.value = page;

  setTimeout(() => {
    form.value.formList = [
      {depName: '区庄服务部', name: '张三', no: '0132107190002', date: '1627097163637', isDel: 1},
      {depName: '区庄服务部', name: '张会员', no: '0132107190001', date: '1627097163637', isDel: 0}
    ];
    total.value = 2;
    listLoading.value = false;
  }, 1000);
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

const onIsDelChange = (val) => {
  console.log(val)
  debugger
}


onMounted(() => {
  fetchData();
})

</script>

<style scoped>

</style>
