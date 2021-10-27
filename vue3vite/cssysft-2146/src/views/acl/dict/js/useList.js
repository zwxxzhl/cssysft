export default function useList(tableColumn, multipleSelection) {

    tableColumn.value = [
        {
            columnObj: {type: 'selection'}
        },
        {
            columnObj: {type: 'index', label: '序号'}
        },
        {
            columnObj: {prop: 'name', label: '名称'}
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
    ];

    return {};
}
