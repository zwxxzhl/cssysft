import {getCurrentInstance, reactive, ref} from "vue";
import depApi from "../../../../api/acl/dep";

export default function useList(search, form, multipleSelection, searchLoading, refZwxListMu) {
    const gp = getCurrentInstance().appContext.config.globalProperties;

    let total = ref(0);
    let currentPage = ref(1);
    let pageSize = ref(10);


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
        depApi
            .page(currentPage.value, pageSize.value, search)
            .then((res) => {
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

    const onCurrentChange = (val) => {
        console.log('当前选择row改变');
    }

    const onSelect = (selection, row) => {
        multipleSelection.value = selection;
    }

    const onSelectionChange = (val) => {
        multipleSelection.value = val;
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
        total,
        currentPage,
        pageSize,
        onSearch,
        onSelect,
        onCurrentChange,
        onSelectionChange,
        onPageCurrentChange,
        onPageSizeChange
    }
}
