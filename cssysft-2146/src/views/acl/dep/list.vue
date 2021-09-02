<template>
  <div class="app-container">

    <el-row type="flex" style="padding-bottom: 10px">
      <el-col :span="4">
        <zwx-form-mu
          :form="search"
          :form-row="searchRow"
          @dep-name-change="onDepNameChange">
        </zwx-form-mu>
      </el-col>
      <el-col :span="6" style="padding-left: 30px">
        <zwx-cols-mu
          :col-list="buttonCols"
          @search-click="onSearch()"
          @add-click="onAdd">
        </zwx-cols-mu>
      </el-col>
    </el-row>

    <!--<zwx-table-mu
      :header-row-class-name="() => 'header-row-class'"
      :size="'medium'"
      stripe
      :form="form"
      :table-column="tableColumn"
      :selection-show="true"
      @select="onSelect"
      @header-event="onHeaderEvent"
      :pagination-show="true"
      :current-page="currentPage"
      :total="total"
      :page-size="pageSize"
      @page-current-change="onSearch"
      @size-change="onSizeChange">
    </zwx-table-mu>-->

    <zwx-list-mu
      :header-row-class-name="() => 'header-row-class'"
      :size="'medium'"
      stripe
      :form="form"
      :table-column="tableColumn"
      :selection-show="true"
      @select="onSelect"
      @current-change="ceshi"
      @header-event="onHeaderEvent"
      :pagination-show="true"
      :current-page="currentPage"
      :total="total"
      :page-size="pageSize"
      @page-current-change="onCurrentChange"
      @page-size-change="onSizeChange">
    </zwx-list-mu>

    <dialog-mu ref="refDialogMu" title="部门表单" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content="sp">
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
import depApi from "../../../api/acl/dep.js";

import {ref, onMounted, reactive, getCurrentInstance, provide, useContext} from "vue";
import enums from "../../../utils/enums";

const gp = getCurrentInstance().appContext.config.globalProperties;

let {dateYMDHmsFormat} = comUtils
provide('comMethod', {
  dateYMDHmsFormat
});

let search = reactive({});
const searchRow = reactive([
  [
    {
      rowObj: {span: 12},
      formItemObj: {prop: 'depName', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
      domObj: {model: 'depName', placeholder: '部门名称', change: 'dep-name-change', dom: 'input', type: 'text'},
    },
    {
      rowObj: {span: 12},
      formItemObj: {prop: '', labelWidth: '10px', size: 'medium', style: {marginBottom: '0'}},
      domObj: {model: 'depNo', placeholder: '部门编码', dom: 'input', type: 'text'},
    }
  ]
]);

const buttonCols = reactive([
  [
    {
      rowObj: {span: 6},
      domObj: {dom: 'button', label: '搜索', click: 'search-click', type: comCfg.searchBtnType, icon: comCfg.searchBtnIcon, size: comCfg.buttonSize},
    },
    {
      rowObj: {span: 6},
      domObj: {dom: 'button', label: '新增', click: 'add-click', type: comCfg.addBtnType, icon: comCfg.addBtnIcon, size: comCfg.buttonSize},
    },
    {
      rowObj: {span: 6},
      domObj: {dom: 'button', label: '编辑', click: 'edit-click', type: comCfg.editBtnType, icon: comCfg.editBtnIcon, size: comCfg.buttonSize},
    },
    {
      rowObj: {span: 6},
      domObj: {dom: 'button', label: '删除', click: 'delete-click', type: comCfg.deleteBtnType, icon: comCfg.deleteBtnIcon, size: comCfg.buttonSize},
    }
  ]
]);

const form = ref({formList: []});
let tableColumn = reactive([
  {
    columnObj: {prop: 'depName', label: '部门', headerAk: true, headerEvent: 'header-event'},
  },
  {
    columnObj: {prop: 'depNo', label: '编码', width: '90'},
  },
  {
    columnObj: {prop: 'sequence', label: '顺序'}
  },
  { //todo 换成input，事件是否冲突
    columnObj: {prop: 'sequence', label: '有效'},
    formItemObj: {prop: '', labelWidth: '0px', size: 'mini', style: {marginBottom: '0'}},
    domObj: {dom: 'checkbox', model: 'isDel', trueLabel: 1, falseLabel: 0}
  }
]);

let listLoading = ref(true);
let total = ref(0);
let currentPage = ref(1);
let pageSize = ref(10);
const multipleSelection = ref([]);

const refDialogMu = ref(null);
const refDepForm = ref(null);

/* ============================================= */

const onAdd = () => {
  refDialogMu.value.open(null, refDepForm.value, enums.formType.add);
}

const onSearch = (page = 1) => {
  currentPage.value = page;

  depApi
    .page(currentPage.value, pageSize.value, search)
    .then((res) => {
      form.value.formList = res.data.items;
      total.value = res.data.total;

      listLoading.value = false;
    });
}

const onCurrentChange = (val) => {
  debugger
  onSearch(val);
}
const onSizeChange = (val) => {
  debugger
  pageSize.value = val;
  onSearch();
}

const ceshi = (val) => {
  debugger
}

const onAfterSave = () => {
  onSearch();
}

const onSelect = (selection, row) => {
  console.log(selection)
  console.log(row)
  debugger
}

const onHeaderEvent = (val, e) => {
  console.log(val)
  console.log(e)
  debugger
}

const onDepNameChange = (val) => {
  console.log(val)
  debugger
}


onMounted(() => {
  onSearch();
})

</script>

<style scoped>

</style>
