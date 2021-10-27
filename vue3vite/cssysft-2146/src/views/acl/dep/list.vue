<template>
  <div class="app-container">

    <zwx-form-mu
      ref="refZwxFormMu"
      :form="search"
      :form-row="searchRow"
      @dep-name-change="onDepNameChange"
      @search-click="onSearch()"
      @add-click="onAdd(refDialogMu)"
      @edit-click="onEdit(null, refDialogMu)"
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
          <i class="el-icon-edit icon-layout-mini color-orange" @click="onEdit(scope.row, refDialogMu)"></i>
        </el-tooltip>
        <el-tooltip v-if="hasPerm('dep.remove')" effect="dark" content="删除" placement="bottom-end">
          <i class="el-icon-delete icon-layout-mini color-gray" @click="onDelete(scope.row)"></i>
        </el-tooltip>
      </template>
    </zwx-list-mu>

    <zwx-dialog-mu
      ref="refDialogMu" title="部门表单"
      width="50%" top="10vh" :height-pct="0.6"
      :page-vm="refDepForm">
      <dep-form ref="refDepForm" @after-save="onAfterFormSave"></dep-form>
      <template #footer>
        <zwx-cols-mu
          type="flex" justify="center" :col-list="refDepForm.colList"
          @close-click="refDepForm.onPageClose"
          @save-click="refDepForm.onSaveOrUpdate">
        </zwx-cols-mu>
      </template>
    </zwx-dialog-mu>

  </div>
</template>

<script setup>
import ZwxDialogMu from "../../../components/base-com/com-parts/zwx-dialog-mu.vue";
import ZwxListMu from "../../../components/base-com/com-parts/zwx-list-mu.vue";
import ZwxFormMu from "../../../components/base-com/com-parts/zwx-form-mu.vue";
import DepForm from "./component/form.vue";
import ZwxColsMu from "../../../components/base-com/com-parts/zwx-cols-mu.vue";

import comCfg from "../../../components/base-com/com-config/com-config";
import comUtils from "../../../utils/comUtils";
import depApi from "../../../api/acl/dep";

import useSearch from './js/useSearch';
import useList from './js/useList';
import useZwxListMu from "../../../components/base-com/com-parts/composables/useZwxListMu";

import {ref, provide, onMounted, getCurrentInstance} from "vue";
const gp = getCurrentInstance().appContext.config.globalProperties;

let {dateYMDHmsFormat} = comUtils;
provide('comMethod', {
  dateYMDHmsFormat
});

let screenWidth = ref(0);
let refDialogMu = ref(null);
let refDepForm = ref({});

const {
  refZwxListMu, refZwxFormMu, search, form, searchRow, tableColumn,
  searchLoading, total, currentPage, pageSize, multipleSelection,
  onAdd, onEdit, onDelete, onSelect, onSelectionChange,
  onSearch, onPageCurrentChange, onPageSizeChange
} = useZwxListMu(depApi);


const {
  onCurrentChange, onHeaderEvent, onDomInputChange, onPageSelect,
} = useList(tableColumn, multipleSelection);

const {
  onDepNameChange
} = useSearch(searchRow, searchLoading, onSearch);

onMounted(() => {
  onSearch();
  window.onresize = () => {
    return (() => {
      screenWidth = window.innerWidth;
      console.log("=========================");
      console.log(screenWidth);
    })()
  }
});

</script>

<style scoped>

</style>
