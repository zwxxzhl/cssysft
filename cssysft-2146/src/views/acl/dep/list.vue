<template>
  <div class="app-container">

    <zwx-form-mu
      :form="search"
      :form-row="searchRow"
      @dep-name-change="onDepNameChange"
      @search-click="onSearch()"
      @add-click="onAdd"
      @edit-click="onEdit"
      @delete-click="onDelete">
    </zwx-form-mu>

    <zwx-list-mu
      :key="key"
      :header-row-class-name="() => 'header-row-class'"
      :size="'medium'"
      stripe
      :form="form"
      :table-column="tableColumn"
      :selection-show="selectionShow"
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

let screenWidth = ref(0);
let key = ref(0);

let search = reactive({});
const searchRow = reactive([
  [
    {
      rowObj: {colStyle: {width: '100px', margin: '0 5px 5px 0px'}},
      formItemObj: {prop: 'depName', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
      domObj: {model: 'depName', placeholder: '部门名称', change: 'dep-name-change', dom: 'input', type: 'text'},
    },
    {
      rowObj: {colStyle: {width: '100px', margin: '0 5px 5px 5px'}},
      formItemObj: {prop: '', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
      domObj: {model: 'depNo', placeholder: '部门编码', dom: 'input', type: 'text'},
    },
    {
      rowObj: {colStyle: {width: '89px', margin: '0 5px 5px 15px'}},
      domObj: {dom: 'button', label: '搜索', click: 'search-click', type: comCfg.searchBtnType, icon: comCfg.searchBtnIcon, size: comCfg.buttonSize},
    },
    {
      rowObj: {colStyle: {width: '89px', margin: '0 5px 5px 5px'}},
      domObj: {dom: 'button', label: '新增', click: 'add-click', type: comCfg.addBtnType, icon: comCfg.addBtnIcon, size: comCfg.buttonSize},
    },
    {
      rowObj: {colStyle: {width: '89px', margin: '0 5px 5px 5px'}},
      domObj: {dom: 'button', label: '编辑', click: 'edit-click', type: comCfg.editBtnType, icon: comCfg.editBtnIcon, size: comCfg.buttonSize},
    },
    {
      rowObj: {span: 0, colStyle: {width: '89px', margin: '0 5px 5px 5px'}},
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
    columnObj: {prop: 'depNo', label: '编码'},
  },
  {
    columnObj: {prop: 'sequence', label: '顺序'}
  },
  {
    columnObj: {prop: 'sequence', label: '有效'},
    formItemObj: {prop: '', labelWidth: '0px', size: 'mini', style: {marginBottom: '0'}},
    domObj: {dom: 'input', type: 'text', model: 'sequence', change: 'dom-input-change'}
  }
]);

let listLoading = ref(true);
let selectionShow = ref(false);
let total = ref(0);
let currentPage = ref(1);
let pageSize = ref(10);
const multipleSelection = ref([]);

const refDialogMu = ref(null);
const refDepForm = ref(null);

/* ============================================= */

const toRenderTable = () => {
  key.value++;
}

const onAdd = () => {
  refDialogMu.value.open(null, refDepForm.value, enums.formType.add);
}

const onEdit = () => {
  // selectionShow.value = !selectionShow.value;
  form.value.formList[1].sequence++;
  toRenderTable();
}
const onDelete = () => {
  form.value.formList.splice(1, 1);
  toRenderTable();
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

const onPageCurrentChange = (val) => {
  debugger
  onSearch(val);
}
const onPageSizeChange = (val) => {
  debugger
  pageSize.value = val;
  onSearch();
}

const onCurrentChange = (val) => {
  console.log('当前选择row改变');
}

const onAfterSave = () => {
  onSearch();
}

const onSelect = (selection, row) => {
  console.log(selection)
  console.log(row)
  debugger
}

const onPageSelect = (selection, row) => {
  console.log(selection)
  console.log(row)
  debugger
}

const onDomInputChange = (val, row) => {
  toRenderTable();
  console.log("table 中 input 改变事件");
  console.log(val);
  console.log(row);
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
