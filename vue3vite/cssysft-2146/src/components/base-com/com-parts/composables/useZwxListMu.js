import {getCurrentInstance, reactive, ref} from "vue";
import enums from "../../../../utils/enums";

export default function useZwxListMu(api) {
    const gp = getCurrentInstance().appContext.config.globalProperties;

    const refDialogMu = ref(null);
    let refBusForm = ref(null);
    let refZwxListMu = ref(null);
    let refZwxFormMu = ref(null);

    const search = reactive({});
    const form = ref({formList: []});

    let searchLoading = ref(false);
    let total = ref(0);
    let currentPage = ref(1);
    let pageSize = ref(10);

    let multipleSelection = ref([]);

    const onAdd = () => {
        refDialogMu.value.open(null, refBusForm.value, enums.formType.add);
    }

    const onEdit = (row) => {
        if (row || multipleSelection.value.length === 1) {
            let data = (row || multipleSelection.value.length === 1 && multipleSelection.value[0])
            refDialogMu.value.open(data, refBusForm.value, enums.formType.edit);
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

        api.remove(ids).then(res => {
            if (res.success) {
                onSearch();
            } else {
                gp.$message.error(res.message);
            }
        })
    }

    // 搜索后，回显选中项，若有
    const echoListSelect = (list) => {
        list.forEach(e => {
            if (-1 !== multipleSelection.value.findIndex(f => f.id === e.id)) {
                refZwxListMu.value.refZwxTable.refTable.toggleRowSelection(e, true);
            }
        })
    }

    const onSearch = (page = 1) => {
        currentPage.value = page;
        searchLoading.value = true;
        api.page(currentPage.value, pageSize.value, search).then((res) => {
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
        refDialogMu,
        refBusForm,
        refZwxListMu,
        refZwxFormMu,
        search,
        form,
        searchLoading,
        total,
        currentPage,
        pageSize,
        multipleSelection,
        onAdd,
        onEdit,
        onDelete,
        onSearch,
        onPageCurrentChange,
        onPageSizeChange
    }
}
