import {reactive, ref} from 'vue'
import comCfg from "../../../../components/base-com/com-config/com-config";
import enums from "../../../../utils/enums";

export default function useSearch(search, form, refDialogMu, refDepForm) {
  const searchRow = reactive([
    [
      {
        rowObj: {colStyle: {flex: '0 0 100px', margin: '0 5px 5px 0px'}},
        formItemObj: {prop: 'depName', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
        domObj: {model: 'depName', placeholder: '部门名称', change: 'dep-name-change', dom: 'input', type: 'text'}
      },
      {
        rowObj: {colStyle: {flex: '0 0 100px', margin: '0 5px 5px 5px'}},
        formItemObj: {prop: '', labelWidth: '0px', size: 'medium', style: {marginBottom: '0'}},
        domObj: {model: 'depNo', placeholder: '部门编码', dom: 'input', type: 'text'}
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 15px'}},
        domObj: {
          dom: 'button', label: '搜索', click: 'search-click', type: comCfg.elButton.search,
          icon: comCfg.elButton.searchIcon, size: comCfg.elButton.size
        }
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
        domObj: {
          dom: 'button', label: '新增', click: 'add-click', type: comCfg.elButton.add,
          icon: comCfg.elButton.addIcon, size: comCfg.elButton.size
        }
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
        domObj: {
          dom: 'button', label: '编辑', click: 'edit-click', type: comCfg.elButton.edit,
          icon: comCfg.elButton.editIcon, size: comCfg.elButton.size
        }
      },
      {
        rowObj: {colStyle: {flex: '0 0 89px', margin: '0 5px 5px 5px'}},
        domObj: {
          dom: 'button', label: '删除', click: 'delete-click', type: comCfg.elButton.delete,
          icon: comCfg.elButton.deleteIcon, size: comCfg.elButton.size
        }
      }
    ]
  ]);

  let searchLoading = ref(false);

  const onAdd = () => {
    refDialogMu.value.open(null, refDepForm.value, enums.formType.add);
  }

  const onEdit = () => {
    form.value.formList[1].sequence++;
  }

  const onDelete = () => {
    form.value.formList.splice(1, 1);
  }

  const onDepNameChange = (val) => {
    console.log(val)
    debugger
  }

  return {
    searchRow,
    searchLoading,
    onAdd,
    onEdit,
    onDelete,
    onDepNameChange
  }
}