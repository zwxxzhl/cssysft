import {reactive} from "vue";

export default function useList(multipleSelection) {

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
            columnObj: {prop: 'sequence', label: '顺序格式化', formatter: sequenceFormatter}
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
