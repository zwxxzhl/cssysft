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
        <span>编辑</span>
      </template>

    </zwx-list-mu>

   <!-- <el-form ref="refTableForm" :model="form">
      <el-table :data="form.formList" style="width: 100%" :header-row-class-name="() => 'header-row-class'">
        <template v-if="selectionShow">
          <el-table-column type="selection" header-align="center" align="center" fixed="left"></el-table-column>
        </template>

        &lt;!&ndash;<template v-if="false">
          <el-table-column prop="depNo" label="编码" width="180"></el-table-column>
          <el-table-column prop="depName" label="名称" width="180"></el-table-column>
          <el-table-column prop="sequence" label="排序"></el-table-column>
        </template>
        <template v-else>
        </template>&ndash;&gt;
          <el-table-column prop="depNo" label="编码" width="180"></el-table-column>
          <el-table-column prop="depName" label="名称" width="180">
            &lt;!&ndash;<span>111</span>&ndash;&gt;
          </el-table-column>
          &lt;!&ndash;<el-table-column prop="depName" label="名称" width="180">
            <template #default="scope">
              <el-form-item label="aa">
                <el-input v-model="scope.row.sequence"></el-input>
              </el-form-item>
            </template>
          </el-table-column>&ndash;&gt;
          <el-table-column prop="sequence" label="排序"></el-table-column>
      </el-table>
    </el-form>-->

    <!--<el-input v-model="abcVal"></el-input>-->

    <dialog-mu ref="refDialogMu" title="部门表单" width="50%" top="10vh" :heightPercent="0.6" :footer="false">
      <template #content="sp">
        <dep-form ref="refDepForm" @after-save="onAfterSave"></dep-form>
      </template>
    </dialog-mu>


    <!--<el-form>
      <el-row>
        <el-col :span="6" style="flex: 0 0 100px;">
          <zwx-button :config="{ label: '测试' }"></zwx-button>
        </el-col>
        <el-col :span="6" style="flex: 0 0 100px;">
          <zwx-form-item :config="{prop: 'depName', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}}">
            <zwx-input :config="{ type: 'text', placeholder: '测试' }" :form="{}"></zwx-input>
          </zwx-form-item>
        </el-col>
      </el-row>
    </el-form>-->

  </div>
</template>

<script setup>
import ZwxFormItem from '../../../components/base-com/com-el/zwx-form-item.vue';
import ZwxButton from '../../../components/base-com/com-el/zwx-button.vue';
import ZwxInput from "../../../components/base-com/com-el/zwx-input.vue";

import DialogMu from "../../../components/DialogCom/dialog-mu.vue";
import ZwxListMu from "../../../components/base-com/com-parts/zwx-list-mu.vue";
import ZwxFormMu from "../../../components/base-com/com-parts/zwx-form-mu.vue";
import ZwxColsMu from "../../../components/base-com/com-parts/zwx-cols-mu.vue";
import DepForm from "./component/form.vue";

import comCfg from "../../../components/base-com/com-config/com-config.js";
import comUtils from "../../../utils/comUtils.js";
import depApi from "../../../api/acl/dep.js";

import {ref, onMounted, reactive, getCurrentInstance, provide} from "vue";
import enums from "../../../utils/enums";
import dayjs from "dayjs";

const gp = getCurrentInstance().appContext.config.globalProperties;

let {dateYMDHmsFormat} = comUtils
provide('comMethod', {
  dateYMDHmsFormat
});

const sequenceFormatte = (row,column) => {
  let data = row[column.property];
  if (1 === data) {
    return '是'
  } else {
    return '否';
  }
}

let screenWidth = ref(0);
let key = ref(0);
let abcVal = ref('aaaa');

let search = reactive({});
const searchRow = reactive([
  [
    {
      rowObj: {colStyle: {flex: '0 0 100px', margin: '0 5px 5px 0px'}},
      formItemObj: {prop: 'depName', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
      domObj: {model: 'depName', placeholder: '部门名称', change: 'dep-name-change', dom: 'input', type: 'text'}
    },
    {
      rowObj: {colStyle: {flex: '0 0 100px', margin: '0 5px 5px 5px'}},
      formItemObj: {prop: '', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
      domObj: {model: 'depNo', placeholder: '部门编码', dom: 'input', type: 'text'}
    },
    {
      rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 15px'}},
      domObj: {
        dom: 'button',
        label: '搜索',
        click: 'search-click',
        type: comCfg.elButton.search,
        icon: comCfg.elButton.searchIcon,
        size: comCfg.elButton.size
      }
    },
    {
      rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
      domObj: {
        dom: 'button',
        label: '新增',
        click: 'add-click',
        type: comCfg.elButton.add,
        icon: comCfg.elButton.addIcon,
        size: comCfg.elButton.size
      }
    },
    {
      rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
      domObj: {
        dom: 'button',
        label: '编辑',
        click: 'edit-click',
        type: comCfg.elButton.edit,
        icon: comCfg.elButton.editIcon,
        size: comCfg.elButton.size
      }
    },
    {
      rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
      domObj: {
        dom: 'button',
        label: '删除',
        click: 'delete-click',
        type: comCfg.elButton.delete,
        icon: comCfg.elButton.deleteIcon,
        size: comCfg.elButton.size
      }
    }
  ]
]);

const form = ref({formList: []});
let tableColumn = reactive([
  {
    columnObj: {type: 'selection'}
  },
  {
    columnObj: {type: 'index', label: '序号'}
  },
  {
    columnObj: {prop: 'sequence', label: '时间', formatter: sequenceFormatte}
  },
  {
    columnObj: {prop: 'depName', label: '部门', headerAk: true, headerEvent: 'header-event'}
  },
  {
    columnObj: {prop: 'depNo', label: '编码'}
  },
  {
    columnObj: {prop: 'sequence', label: '顺序'}
  },
  {
    columnObj: {prop: 'sequence', label: '有效'},
    formItemObj: {prop: '', labelWidth: '0px', size: 'mini', style: {marginBottom: '0'}},
    domObj: {dom: 'input', type: 'text', model: 'sequence', change: 'dom-input-change'}
  },
  {
    columnObj: {label: '操作', rowSlot: true, rowSlotName: 'operation', fixed: 'right'}
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

const refZwxListMu = ref(null);

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

  // refZwxListMu.value.refZwxTable.ceshiForm.formList[1].sequence++;
  // refZwxListMu.value.refZwxTable.selectionShowCeshi = !refZwxListMu.value.refZwxTable.selectionShowCeshi;
  // refZwxListMu.value.refZwxTable.key++;
}
const onDelete = () => {
  form.value.formList.splice(1, 1);
  // toRenderTable();
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
  // toRenderTable();
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
