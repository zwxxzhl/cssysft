<template>
  <div class="app-container">

    <zwx-form-mu
      :form="search"
      :form-row="searchRow"
      @dep-name-change="onDepNameChange"
      @search-click="onSearch()"
      @add-click="onAdd"
      @edit-click="onEdit()"
      @delete-click="onDelete()">
    </zwx-form-mu>

    <zwx-list-mu
      ref="refZwxListMu"
      :header-row-class-name="() => comCfg.elTable.headerRowClassName"
      :size="comCfg.elTable.size"
      :stripe="comCfg.elTable.stripe"
      :form="form"
      :table-column="tableColumn"
      row-key="id"
      @select="onSelect"
      @current-change="onCurrentChange"
      @header-event="onHeaderEvent"
      @dom-input-change="onDomInputChange"
      :pagination-show="true"
      :total="total"
      :current-page="currentPage"
      :page-size="pageSize"
      @page-select="onPageSelect"
      @page-current-change="onPageCurrentChange"
      @page-size-change="onPageSizeChange">

      <template #operation="scope">
        <el-tooltip v-if="hasPerm('dep.update')" effect="dark" content="编辑" placement="left-start">
          <i class="el-icon-edit icon-layout-mini color-orange" @click="onEdit(scope.row)"></i>
        </el-tooltip>
        <el-tooltip v-if="hasPerm('dep.remove')" effect="dark" content="删除" placement="bottom-end">
          <i class="el-icon-delete icon-layout-mini color-gray" @click="onDelete(scope.row)"></i>
        </el-tooltip>
      </template>

    </zwx-list-mu>

    <dialog-mu ref="refDialogMu" title="部门表单" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content>
        <dep-form ref="refDepForm" @after-save="onAfterSave"></dep-form>
      </template>
    </dialog-mu>

  </div>
</template>

<script setup>
import DialogMu from "../../../components/DialogCom/dialog-mu.vue";
import ZwxListMu from "../../../components/base-com/com-parts/zwx-list-mu.vue";
import ZwxFormMu from "../../../components/base-com/com-parts/zwx-form-mu.vue";
import ZwxColsMu from "../../../components/base-com/com-parts/zwx-cols-mu.vue";
import DepForm from "./component/form.vue";

import comCfg from "../../../components/base-com/com-config/com-config.js";
import comUtils from "../../../utils/comUtils.js";

import {ref, onMounted, reactive, getCurrentInstance, provide} from "vue";

import useSearch from './js/useSearch.js'
import useList from './js/useList.js'

const gp = getCurrentInstance().appContext.config.globalProperties;

let {dateYMDHmsFormat} = comUtils
provide('comMethod', {
  dateYMDHmsFormat
});

let screenWidth = ref(0);
const refDialogMu = ref(null);
const refDepForm = ref(null);
const refZwxListMu = ref(null);

let search = reactive({});
const form = ref({formList: []});
let multipleSelection = ref([]);

const {
  searchRow, searchLoading, onAdd, onEdit, onDelete, onDepNameChange
} = useSearch(search, form, multipleSelection, refDialogMu, refDepForm);

const {
  tableColumn, total, currentPage, pageSize, onSearch, onSelect, onCurrentChange,
  onHeaderEvent, onDomInputChange, onPageSelect, onPageCurrentChange, onPageSizeChange
} = useList(search, form, multipleSelection, searchLoading);

const onAfterSave = () => {
  onSearch();
}

onMounted(() => {
  onSearch();
  window.onresize = () => {
    return (() => {
      screenWidth = window.innerWidth;
      console.log("=========================");
      console.log(screenWidth);
    })()
  }
})

</script>

<style scoped>

</style>
