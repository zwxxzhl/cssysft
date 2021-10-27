import {getCurrentInstance, reactive, ref} from "vue";
import enums from "../../../../utils/enums";

export default function useZwxListMu(api) {
  const gp = getCurrentInstance().appContext.config.globalProperties;

  let refZwxListMu = ref(null);
  let refZwxFormMu = ref(null);

  const search = reactive({});
  const form = ref({formList: []});
  const searchRow = ref([]);
  const tableColumn = ref([]);

  let searchLoading = ref(false);
  let total = ref(0);
  let currentPage = ref(1);
  let pageSize = ref(10);

  let multipleSelection = ref([]);

  const onAdd = (refDialogMu) => {
    refDialogMu.open(null, enums.formType.add);
  }

  const onEdit = (row, refDialogMu) => {
    if (row || multipleSelection.value.length === 1) {
      let data = (row || multipleSelection.value.length === 1 && multipleSelection.value[0])
      refDialogMu.open(data, enums.formType.edit);
    } else {
      gp.$message.warning("请选择单行");
    }
  }

  const onDelete = (row) => {
    let ids = [];
    if (row || multipleSelection.value.length > 0) {
      ids.push(row.id);
    } else if (multipleSelection.value.length > 0) {
      ids = multipleSelection.value.map(m => m.id);
    } else {
      gp.$message.warning("请选择行");
      return;
    }

    gp.$confirm(
      '此操作将删除数据，是否继续?', '提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(() => {
      api.remove(ids).then(res => {
        if (res.success) {
          onSearch();
        } else {
          gp.$message.error(res.message);
        }
      })
    })

  }

  const onSelect = (selection, row) => {
    multipleSelection.value = selection;
  }

  const onSelectionChange = (val) => {
    multipleSelection.value = val;
  }

  // 搜索后，回显选中项，若有
  const echoListSelect = (list) => {
    list.forEach(e => {
      if (-1 !== multipleSelection.value.findIndex(f => f.id === e.id)) {
        refZwxListMu.value.refZwxTable.refTable.toggleRowSelection(e, true);
      }
    })
  }

  // 处理查询参数
  const handleSearchParams = () => {
    let searchCopy = JSON.parse(JSON.stringify(search));
    for (const items of searchRow.value) {
      for (const item of items) {
        if (item.searchObj) {
          if (item.searchObj.exp) {
            // 配置
            if (searchCopy[item.domObj.model]) {
              searchCopy[item.searchObj.exp + item.domObj.model] = searchCopy[item.domObj.model];
            }
            delete searchCopy[item.domObj.model];
          } else {
            // 默认
            if (searchCopy[item.domObj.model]) {
              searchCopy[enums.exp.eq + item.domObj.model] = searchCopy[item.domObj.model];
            }
            delete searchCopy[item.domObj.model];
          }
        } else {
          // 默认
          if (searchCopy[item.domObj.model]) {
            searchCopy[enums.exp.eq + item.domObj.model] = searchCopy[item.domObj.model];
          }
          delete searchCopy[item.domObj.model];
        }
      }
    }
    return searchCopy;
  }

  const onSearch = (page = 1) => {
    currentPage.value = page;
    searchLoading.value = true;

    api.page(currentPage.value, pageSize.value, handleSearchParams()).then((res) => {
      if (res.success) {
        form.value.formList = res.data.items;
        total.value = res.data.total;
        echoListSelect(form.value.formList);

        gp.$message.success(res.message);
        searchLoading.value = false;
      } else {
        gp.$message.error(res.message);
      }
    });
  }

  const onPageCurrentChange = (val) => {
    multipleSelection.value = [];
    refZwxListMu.value.refZwxTable.refTable.clearSelection();
    onSearch(val);
  }

  const onPageSizeChange = (val) => {
    pageSize.value = val;
    multipleSelection.value = [];
    refZwxListMu.value.refZwxTable.refTable.clearSelection();
    onSearch();
  }

  return {
    refZwxListMu,
    refZwxFormMu,
    search,
    form,
    searchRow,
    tableColumn,
    searchLoading,
    total,
    currentPage,
    pageSize,
    multipleSelection,
    onAdd,
    onEdit,
    onDelete,
    onSelect,
    onSelectionChange,
    onSearch,
    onPageCurrentChange,
    onPageSizeChange
  };
}
