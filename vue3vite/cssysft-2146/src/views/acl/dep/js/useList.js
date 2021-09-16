import {reactive, ref} from "vue";

export default function useUserRepositories() {
  const sequenceFormatte = (row,column) => {
    let data = row[column.property];
    if (1 === data) {
      return '是'
    } else {
      return '否';
    }
  }

  const form = ref({formList: []});
  let tableColumn = reactive([
    {
      columnObj: {type: 'selection'}
    },
    {
      columnObj: {type: 'index', label: '序号'}
    },
    {
      columnObj: {prop: 'sequence', label: '时间', formatter: sequenceFormatte}
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

  return {
    form,
    tableColumn
  }
}