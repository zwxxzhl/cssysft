<template>
  <div class="app-container">
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item>
        <el-input v-model="searchObj.name" placeholder="名称"/>
      </el-form-item>

      <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
      <el-button type="default" @click="resetData()">清空</el-button>
    </el-form>

<!--    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      style="width: 100%"
      @selection-change="onSelectionChange"
    >
      <el-table-column type="selection" width="55"/>

      <el-table-column label="序号" width="70" align="center">
        <template #default="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="depId" label="服务部"/>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="no" label="验光单号"/>
      <el-table-column prop="date" label="日期"/>
    </el-table>-->

    <table-list
      :table-data="list"
      :table-column="tableColumn">
    </table-list>

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

    <dialog-com ref="refDialogCom" title="验光录入" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content="sp">
        <span>验光录入</span>
      </template>
    </dialog-com>
  </div>
</template>

<script setup>
import DialogCom from "../../components/DialogCom/dialog_com.vue";
import TableList from "../../components/base-com/com-parts/table-list.vue";

import {ref, onMounted, reactive, getCurrentInstance, provide} from "vue";
import comUtils from "../../utils/comUtils";

const globalProperties = getCurrentInstance().appContext.config.globalProperties;

provide('comMethod', {
  ...comUtils.dateYMDHmsFormat
});

const listLoading = ref(true);

const list = reactive([
  {depName: '区庄服务部', name: '张三', no: '0132107190002', date: '1626861102742'},
  {depName: '区庄服务部', name: '张会员', no: '0132107190001', date: '1626861102742'}
]);

const tableColumn = reactive([
  {prop: 'depId', label: '销售单号'},
  {prop: 'name', label: '姓名', width: '90'},
  {prop: 'no', label: '应收金额(元)'},
  {prop: 'date', label: '开单日期', minWidth: '140', formatter: 'dateYMDHmsFormat'},
]);


const total = ref(0);
const page = ref(1);
const limit = ref(10);
let searchObj = reactive({});
const multipleSelection = ref([]);

const refDialogCom = ref(null);

const fetchData = () => {
  listLoading.value = false;
}

const changeSize = () => {

}

const onSelectionChange = () => {

}


onMounted(() => {
  fetchData();
})

</script>

<style scoped>

</style>
