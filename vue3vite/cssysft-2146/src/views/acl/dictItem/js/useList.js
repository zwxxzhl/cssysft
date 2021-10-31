import {reactive, ref} from "vue";
import comCfg from "../../../../components/base-com/com-config/com-config";
import Enums from "../../../../utils/enums";

export default function useList(tableColumn, multipleSelection) {

  tableColumn.value = [
    {
      columnObj: {type: 'selection'}
    },
    {
      columnObj: {type: 'index', label: '序号'}
    },
    {
      columnObj: {prop: 'code', label: '编码'}
    },
    {
      columnObj: {prop: 'name', label: '名称'}
    },
    {
      columnObj: {prop: 'name1', label: '扩展名1'}
    },
    {
      columnObj: {prop: 'name2', label: '扩展名2'}
    },
    {
      columnObj: {prop: 'sequence', label: '顺序'}
    },
    {
      columnObj: {label: '操作', rowSlot: true, rowSlotName: 'operation', fixed: 'right'}
    }
  ];

  // 自身模态框打开时需要
  let dialogOpenType = ref(Enums.formType.add);
  let dialogData = ref({});
  let dialogClose = null;

  const initData = (data, openType, close) => {
    dialogOpenType = openType;
    dialogClose = close;
    dialogData.value = data.value;
  }

  const onPageClose = () => {
    dialogClose();
  }

  const bottomList = reactive([
    [
      {
        rowObj: {span: 6, colStyle: {flex: '0 0 89px', margin: '0 10px'}},
        domObj: {
          dom: 'button', label: '关闭', click: 'close-click', type: comCfg.elButton.close,
          icon: comCfg.elButton.closeIcon, size: comCfg.elButton.size
        }
      }
    ]
  ]);

  return {
    initData,
    dialogData,
    onPageClose,
    bottomList
  };
}
