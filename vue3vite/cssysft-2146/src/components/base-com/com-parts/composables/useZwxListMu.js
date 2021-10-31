import {getCurrentInstance, markRaw, reactive, ref} from "vue";
import Enums from "../../../../utils/enums";
import Exps from "../../../../utils/exps";

export default function useZwxListMu(api, pk) {
  const gp = getCurrentInstance().appContext.config.globalProperties;

  let refZwxListMu = ref(null);
  let refZwxFormMu = ref(null);

  const search = reactive({});
  const form = ref({formList: []});
  const searchRow = ref([]);
  const tableColumn = ref([]);
  let searchExp = markRaw({});

  let searchLoading = ref(false);
  let total = ref(0);
  let currentPage = ref(1);
  let pageSize = ref(10);

  let multipleSelection = ref([]);

  const onView = (data, refDialogMu) => {
    refDialogMu.open(data, Enums.formType.add);
  }

  const onAdd = (data, refDialogMu) => {
    refDialogMu.open(data, Enums.formType.add);
  }

  const onEdit = (row, refDialogMu) => {
    if (row || multipleSelection.value.length === 1) {
      let data = (row || multipleSelection.value.length === 1 && multipleSelection.value[0])
      refDialogMu.open(data, Enums.formType.edit);
    } else {
      gp.$message.warning("请选择单行");
    }
  }

  const onDelete = (row) => {
    let ids = [];
    if (row || multipleSelection.value.length > 0) {
      ids.push(row[pk]);
    } else if (multipleSelection.value.length > 0) {
      ids = multipleSelection.value.map(m => m[pk]);
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
      if (-1 !== multipleSelection.value.findIndex(f => f[pk] === e[pk])) {
        refZwxListMu.value.refZwxTable.refTable.toggleRowSelection(e, true);
      }
    })
  }

  // 处理查询参数
  const handleSearchParams = () => {
    let temp = {};
    for (const items of searchRow.value) {
      for (const item of items) {
        if (item.searchObj && search[item.domObj.model]) {
          if (Exps.between === item.searchObj[Exps.exp]) {
            if (-1 !== item.domObj.model.indexOf(Exps.pre)) {
              let key = item.domObj.model.substring(Exps.pre.length);
              if (temp[key]) {
                temp[key][Exps.preProp] = key;
                temp[key][Exps.preVal] = search[item.domObj.model];
              } else {
                temp[key] = {
                  [Exps.exp]: item.searchObj[Exps.exp],
                  [Exps.preProp]: key,
                  [Exps.preVal]: search[item.domObj.model]
                };
              }
            } else if (-1 !== item.domObj.model.indexOf(Exps.suf)) {
              let key = item.domObj.model.substring(Exps.suf.length);
              if (temp[key]) {
                temp[key][Exps.sufProp] = key;
                temp[key][Exps.sufVal] = search[item.domObj.model];
              } else {
                temp[key] = {
                  [Exps.exp]: item.searchObj[Exps.exp],
                  [Exps.sufProp]: key,
                  [Exps.sufVal]: search[item.domObj.model]
                };
              }
            }
          } else {
            let key = item.domObj.model;
            temp[key] = {
              [Exps.exp]: item.searchObj[Exps.exp],
              [Exps.prop]: key,
              [Exps.val]: search[item.domObj.model]
            };
          }
        }
      }
    }
    Object.assign(temp, searchExp);
    return temp;
  }

  const onSearch = (page = 1) => {
    currentPage.value = page;
    searchLoading.value = true;

    api.page(currentPage.value, pageSize.value, handleSearchParams()).then((res) => {
      if (res.success) {
        form.value.formList = res.data.items;
        total.value = res.data.total;
        echoListSelect(form.value.formList);

        // 业务操作频繁时,影响体验
        // gp.$message.success(res.message);
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
    searchExp,
    searchLoading,
    total,
    currentPage,
    pageSize,
    multipleSelection,
    onView,
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
