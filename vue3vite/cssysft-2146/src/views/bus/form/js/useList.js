import dictItemApi from "../../../../api/acl/dictItem";
import {ref} from "vue";

export default function useList(tableColumn, multipleSelection) {

    let depTypeList = ref([]);
    const getDictList = () => {
        dictItemApi.listBatch({codes: 'DepType'}).then(res => {
            depTypeList.value = res.data.items.DepType;
        })
    }
    getDictList();

    const depTypeFormatter = (row, column) => {
        let val = row[column.property];
        if (depTypeList.value.length > 0) {
            let obj = depTypeList.value.find(f => f.code === val);
            if (obj) return obj.name;
        } else {
            return '';
        }
    }

    tableColumn.value = [
        {
            columnObj: {type: 'selection'}
        },
        {
            columnObj: {type: 'index', label: '序号', width: '80'}
        },
        {
            columnObj: {prop: 'type', label: '部门类型', formatter: depTypeFormatter}
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
            formItemObj: {prop: '', labelWidth: '0px', size: 'small', style: {marginBottom: '0'}},
            domObj: {dom: 'input', type: 'text', model: 'sequence', change: 'dom-input-change'}
        },
        {
            columnObj: {label: '操作', rowSlot: true, rowSlotName: 'operation', fixed: 'right'}
        }
    ];

    const onCurrentChange = (val) => {
        console.log('当前选择row改变');
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
        onCurrentChange,
        onHeaderEvent,
        onDomInputChange,
        onPageSelect
    };
}
