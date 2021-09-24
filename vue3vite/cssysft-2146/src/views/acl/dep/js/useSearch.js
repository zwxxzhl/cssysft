import {getCurrentInstance, reactive, ref} from 'vue'
import comCfg from "../../../../components/base-com/com-config/com-config";
import enums from "../../../../utils/enums";
import depApi from "../../../../api/acl/dep";

export default function useSearch(search, form, multipleSelection, searchLoading, refDialogMu, refDepForm, onSearch) {
    const gp = getCurrentInstance().appContext.config.globalProperties;

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


    const onAdd = () => {
        refDialogMu.value.open(null, refDepForm.value, enums.formType.add);
    }

    const onEdit = (row) => {
        if (row || multipleSelection.value.length === 1) {
            let data = (row || multipleSelection.value.length === 1 && multipleSelection.value[0])
            refDialogMu.value.open(data, refDepForm.value, enums.formType.edit);
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

        depApi.remove(ids).then(res => {
            if (res.success) {
                onSearch();
            } else {
                gp.$message.error(res.message);
            }
        })
    }

    const onDepNameChange = (val) => {
        console.log("搜索条件 DepName 改变")
        console.log(val);
    }

    return {
        searchRow,
        onAdd,
        onEdit,
        onDelete,
        onDepNameChange
    }
}
