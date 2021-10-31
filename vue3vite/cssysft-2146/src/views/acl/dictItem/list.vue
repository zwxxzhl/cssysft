<template>
  <div class="app-container">

    <zwx-form-mu
      ref="refZwxFormMu"
      :form="search"
      :form-row="searchRow"
      @search-click="onSearch()"
      @add-click="onAdd(dialogData, refDialogMu)"
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
        <el-tooltip v-if="hasPerm('dict.update')" effect="dark" content="编辑" placement="left-start">
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
      :page-vm="refDictItemForm">
      <dict-item-form ref="refDictItemForm" @after-save="refDictItemForm.onAfterFormSave(onSearch)"></dict-item-form>
      <template #footer>
        <zwx-cols-mu
          type="flex" justify="center" :col-list="refDictItemForm.colList"
          @close-click="refDictItemForm.onPageClose"
          @save-click="refDictItemForm.onSaveOrUpdate">
        </zwx-cols-mu>
      </template>
    </zwx-dialog-mu>

  </div>
</template>

<script setup>
import ZwxDialogMu from "../../../components/base-com/com-parts/zwx-dialog-mu.vue";
import ZwxListMu from "../../../components/base-com/com-parts/zwx-list-mu.vue";
import ZwxFormMu from "../../../components/base-com/com-parts/zwx-form-mu.vue";
import DictItemForm from "./component/form.vue";
import ZwxColsMu from "../../../components/base-com/com-parts/zwx-cols-mu.vue";

import comCfg from "../../../components/base-com/com-config/com-config";
import dictItemApi from "../../../api/acl/dictItem";

import useSearch from './js/useSearch';
import useList from './js/useList';
import useZwxListMu from "../../../components/base-com/com-parts/composables/useZwxListMu";

import {ref, onMounted, getCurrentInstance, reactive} from "vue";
import Exps from "../../../utils/exps";

const gp = getCurrentInstance().appContext.config.globalProperties;

let refDialogMu = ref(null);
let refDictItemForm = ref({});

const {
  refZwxListMu, refZwxFormMu, search, form, searchRow, tableColumn, searchExp,
  searchLoading, total, currentPage, pageSize, multipleSelection,
  onView, onAdd, onEdit, onDelete, onSelect, onSelectionChange,
  onSearch, onPageCurrentChange, onPageSizeChange
} = useZwxListMu(dictItemApi, 'id');


const {initData, bottomList, dialogData, onPageClose} = useList(tableColumn, multipleSelection);

useSearch(searchRow, searchExp, search, dialogData, searchLoading, onSearch);

// todo 与onSearch有时差; 使用watchEffect
searchExp.pid = { [Exps.exp]: Exps.eq, [Exps.prop]: 'pid', [Exps.val]: dialogData.value.id };

onMounted(() => {
  onSearch();
});

defineExpose({
  initData, bottomList, onPageClose
});

</script>

<style scoped>

</style>
