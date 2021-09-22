import {getCurrentInstance, reactive, ref} from "vue";
import depApi from "../../../../api/acl/dep";

export default function useList(search, form, multipleSelection, searchLoading) {
  const globalProperties = getCurrentInstance().appContext.config.globalProperties;

  let total = ref(0);
  let currentPage = ref(1);
  let pageSize = ref(10);

  const sequenceFormatter = (row, column) => {
    let data = row[column.property];
    if (1 === data) {
      return '是'
    } else {
      return '否';
    }
  }

  let tableColumn = reactive([
    {
      columnObj: {type: 'selection'}
    },
    {
      columnObj: {type: 'index', label: '序号'}
    },
    {
      columnObj: {prop: 'sequence', label: '时间', formatter: sequenceFormatter}
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

  const onSearch = (page = 1) => {
    currentPage.value = page;
    searchLoading.value = true;
    depApi
      .page(currentPage.value, pageSize.value, search)
      .then((res) => {
        if (res.success) {
          form.value.formList = res.data.items;
          total.value = res.data.total;

          globalProperties.$message.success(res.message);
          searchLoading.value = false;
        } else {
          globalProperties.$message.error(res.message);
        }
      });
  }

  const onSelect = (selection, row) => {debugger
    // todo 刷新，清空选择项，或保留选择项，但是点击分页时，又需要清空选择性
    multipleSelection.value = selection;
  }

  const onCurrentChange = (val) => {
    console.log('当前选择row改变');
  }

  const onSelectionChange = (val) => {
    multipleSelection.value = val;
  }

  const onHeaderEvent = (val, e) => {
    console.log(val)
    console.log(e)
    debugger
  }

  const onDomInputChange = (val, row) => {
    console.log("table 中 input 改变事件");
    console.log(val);
    console.log(row);
  }

  const onPageSelect = (selection, row) => {
    console.log(selection)
    console.log(row)
  }

  const onPageCurrentChange = (val) => {
    onSearch(val);
  }

  const onPageSizeChange = (val) => {
    pageSize.value = val;
    onSearch();
  }

  return {
    form,
    tableColumn,
    total,
    currentPage,
    pageSize,
    onSearch,
    onSelect,
    onCurrentChange,
    onSelectionChange,
    onHeaderEvent,
    onDomInputChange,
    onPageSelect,
    onPageCurrentChange,
    onPageSizeChange
  }
}