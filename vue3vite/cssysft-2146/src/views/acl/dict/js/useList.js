import {reactive} from "vue";

export default function useList(multipleSelection) {
    let tableColumn = reactive([
        {
            columnObj: {type: 'selection'}
        },
        {
            columnObj: {type: 'index', label: '序号'}
        },
        {
            columnObj: {prop: 'name', label: '名称', headerAk: true, headerEvent: 'header-event'}
        },
        {
            columnObj: {prop: 'code', label: '编码'}
        },
        {
            columnObj: {prop: 'sequence', label: '顺序'}
        },
        {
            columnObj: {label: '操作', rowSlot: true, rowSlotName: 'operation', fixed: 'right'}
        }
    ]);

    const onCurrentChange = (val) => {
        console.log('当前选择row改变');
    }

    const onSelect = (selection, row) => {
        multipleSelection.value = selection;
    }

    const onSelectionChange = (val) => {
        multipleSelection.value = val;
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
        tableColumn,
        onSelect,
        onCurrentChange,
        onSelectionChange,
        onHeaderEvent,
        onDomInputChange,
        onPageSelect
    }
}
