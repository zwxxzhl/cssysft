import {getCurrentInstance, reactive, ref} from "vue";
import depApi from "../../../../api/acl/dep";

export default function useList(search, form, multipleSelection, searchLoading, refZwxListMu) {
    const gp = getCurrentInstance().appContext.config.globalProperties;

    let total = ref(0);
    let currentPage = ref(1);
    let pageSize = ref(10);

    const sequenceFormatter = (row, column) => {
        let data = row[column.property];
        if (1 === data) {
            return '是';
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

    const onPageSelect = (val) => {
        console.log("pagination 中 onPageSelect 事件");
        console.log(val);
    }

    const onHeaderEvent = (val, e) => {
        console.log("table 中 onHeaderEvent 事件");
        console.log(val);
        console.log(e);
    }

    const onDomInputChange = (val, row) => {
        console.log("table 中 input 改变事件");
        console.log(val);
        console.log(row);
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
