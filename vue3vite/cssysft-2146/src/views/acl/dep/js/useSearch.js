import comCfg from "../../../../components/base-com/com-config/com-config";

export default function useSearch(searchRow, searchLoading, onSearch) {

    searchRow.value = [
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
                    icon: comCfg.elButton.searchIcon, size: comCfg.elButton.size, loading: searchLoading
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
    ];

    const onDepNameChange = (val) => {
        console.log("搜索条件 DepName 改变")
        console.log(val);
    }

    return {
        onDepNameChange
    };
}
