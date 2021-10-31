<template>
  <div class="app-container">

    <zwx-form-mu
      ref="refZwxFormMu"
      :form="search"
      :form-row="searchRow"
      @search-click="onSearch()"
      @add-click="onAdd(null, refDialogMu)"
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
      :pagination-show="true"
      :total="total"
      :current-page="currentPage"
      :page-size="pageSize"
      @page-current-change="onPageCurrentChange"
      @page-size-change="onPageSizeChange">

      <template #operation="scope">
        <el-tooltip v-if="hasPerm('dict.list')" effect="dark" content="明细" placement="left-start">
          <i class="el-icon-view icon-layout-mini color-blue" @click="onView(scope.row, refDialogMuDictItemList)"></i>
        </el-tooltip>
        <el-tooltip v-if="hasPerm('dict.update')" effect="dark" content="编辑" placement="bottom">
          <i class="el-icon-edit icon-layout-mini color-orange" @click="onEdit(scope.row, refDialogMu)"></i>
        </el-tooltip>
        <el-tooltip v-if="hasPerm('dict.remove')" effect="dark" content="删除" placement="bottom-end">
          <i class="el-icon-delete icon-layout-mini color-gray" @click="onDelete(scope.row)"></i>
        </el-tooltip>
      </template>
    </zwx-list-mu>

    <zwx-dialog-mu
      ref="refDialogMu" title="字典表单"
      width="50%" top="10vh" :height-pct="0.6"
      :page-vm="refDictForm">
      <dict-form ref="refDictForm" @after-save="refDictForm.onAfterFormSave(onSearch)"></dict-form>
      <template #footer>
        <zwx-cols-mu
          type="flex" justify="center" :col-list="refDictForm.colList"
          @close-click="refDictForm.onPageClose"
          @save-click="refDictForm.onSaveOrUpdate">
        </zwx-cols-mu>
      </template>
    </zwx-dialog-mu>

    <zwx-dialog-mu
        ref="refDialogMuDictItemList" title="字典明细列表"
        width="80%" top="3vh" :height-pct="0.75"
        :page-vm="refDictItemList">
      <dict-item-list ref="refDictItemList"></dict-item-list>
      <template #footer>
        <zwx-cols-mu
            type="flex" justify="center" :col-list="refDictItemList.bottomList"
            @close-click="refDictItemList.onPageClose">
        </zwx-cols-mu>
      </template>
    </zwx-dialog-mu>

  </div>
</template>

<script setup>
import ZwxDialogMu from "../../../components/base-com/com-parts/zwx-dialog-mu.vue";
import ZwxListMu from "../../../components/base-com/com-parts/zwx-list-mu.vue";
import ZwxFormMu from "../../../components/base-com/com-parts/zwx-form-mu.vue";
import DictForm from "./component/form.vue";
import ZwxColsMu from "../../../components/base-com/com-parts/zwx-cols-mu.vue";
import DictItemList from "../dictItem/list.vue";

import comCfg from "../../../components/base-com/com-config/com-config";
import dictApi from "../../../api/acl/dict";

import useSearch from './js/useSearch';
import useList from './js/useList';
import useZwxListMu from "../../../components/base-com/com-parts/composables/useZwxListMu";

import {ref, onMounted, getCurrentInstance} from "vue";

const gp = getCurrentInstance().appContext.config.globalProperties;

let refDialogMu = ref(null);
let refDictForm = ref({});
let refDialogMuDictItemList = ref(null);
let refDictItemList = ref({});

const {
  refZwxListMu, refZwxFormMu, search, form, searchRow, tableColumn, searchExp,
  searchLoading, total, currentPage, pageSize, multipleSelection,
  onView, onAdd, onEdit, onDelete, onSelect, onSelectionChange,
  onSearch, onPageCurrentChange, onPageSizeChange
} = useZwxListMu(dictApi, 'id');

useList(tableColumn, multipleSelection);

useSearch(searchRow, searchExp, search, searchLoading, onSearch);



onMounted(() => {
  onSearch();
});

</script>

<style scoped>

</style>
